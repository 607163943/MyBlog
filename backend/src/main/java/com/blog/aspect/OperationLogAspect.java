package com.blog.aspect;

import cn.dev33.satoken.stp.StpUtil;
import com.blog.annotation.OperationLog;
import com.blog.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 操作日志切面：统一记录高价值接口的模块、操作人、来源、关键参数、结果与耗时。
 * <p>
 * 日志以 {@code [OP]} 前缀输出，成功为 INFO，业务/系统失败为 WARN，
 * 系统异常堆栈仍由 GlobalExceptionHandler 统一记录。
 */
@Slf4j
@Aspect
@Component
public class OperationLogAspect {

    private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

    /**
     * 业务参数最大记录长度，避免超长内容污染日志
     */
    private static final int MAX_KEY_LENGTH = 200;

    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperationLog operationLog) throws Throwable {
        long startTime = System.currentTimeMillis();
        String prefix = buildPrefix(joinPoint, operationLog);
        try {
            Object result = joinPoint.proceed();
            log.info("{} result=SUCCESS cost={}ms", prefix, System.currentTimeMillis() - startTime);
            return result;
        } catch (BusinessException e) {
            log.warn("{} result=BUSINESS_ERROR({}:{}) cost={}ms", prefix,
                    e.getExceptionEnum().getCode(), e.getExceptionEnum().getMsg(),
                    System.currentTimeMillis() - startTime);
            throw e;
        } catch (Throwable e) {
            log.warn("{} result=ERROR({}:{}) cost={}ms", prefix,
                    e.getClass().getSimpleName(), e.getMessage(),
                    System.currentTimeMillis() - startTime);
            throw e;
        }
    }

    /**
     * 组装日志公共前缀：模块、操作、操作人、关键参数、IP、请求方法与路径
     */
    private String buildPrefix(ProceedingJoinPoint joinPoint, OperationLog operationLog) {
        return String.format("[OP] module=%s action=%s operator=%s key=%s ip=%s uri=%s",
                operationLog.module(), operationLog.action(), currentOperator(),
                resolveKey(joinPoint, operationLog.key()), clientIp(), requestUri());
    }

    /**
     * 按 SpEL 从方法参数中提取关键业务参数
     */
    private String resolveKey(ProceedingJoinPoint joinPoint, String expressionText) {
        if (expressionText == null || expressionText.trim().isEmpty()) {
            return "-";
        }
        StandardEvaluationContext context = new StandardEvaluationContext();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            context.setVariable("a" + i, args[i]);
            context.setVariable("p" + i, args[i]);
        }
        try {
            Expression expression = EXPRESSION_PARSER.parseExpression(expressionText);
            Object value = expression.getValue(context);
            return truncate(value == null ? "null" : String.valueOf(value));
        } catch (Exception e) {
            log.warn("[OP] key 表达式解析失败：{}", expressionText);
            return "-";
        }
    }

    /**
     * 当前登录用户 ID，前台匿名接口记为 "-"
     */
    private String currentOperator() {
        try {
            return StpUtil.isLogin() ? StpUtil.getLoginIdAsString() : "-";
        } catch (Exception e) {
            return "-";
        }
    }

    /**
     * 客户端 IP：优先代理头，其次连接地址
     */
    private String clientIp() {
        HttpServletRequest request = currentRequest();
        if (request == null) {
            return "-";
        }
        String ip = request.getHeader("X-Forwarded-For");
        if (isBlankOrUnknown(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (isBlankOrUnknown(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip == null || ip.isEmpty() ? "-" : ip;
    }

    private String requestUri() {
        HttpServletRequest request = currentRequest();
        return request == null ? "-" : request.getMethod() + " " + request.getRequestURI();
    }

    private HttpServletRequest currentRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes == null ? null : attributes.getRequest();
    }

    private boolean isBlankOrUnknown(String value) {
        return value == null || value.trim().isEmpty() || "unknown".equalsIgnoreCase(value);
    }

    /**
     * 单行化并截断，防止换行与超长参数破坏日志格式
     */
    private String truncate(String value) {
        String singleLine = value.replace('\r', ' ').replace('\n', ' ');
        if (singleLine.length() <= MAX_KEY_LENGTH) {
            return singleLine;
        }
        return singleLine.substring(0, MAX_KEY_LENGTH) + "...";
    }
}
