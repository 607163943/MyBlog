package com.blog.handler;

import cn.dev33.satoken.exception.NotLoginException;
import com.blog.common.Result;
import com.blog.enums.CommonExceptionEnum;
import com.blog.enums.ExceptionEnum;
import com.blog.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Void>> handleBusinessException(BusinessException e) {
        return buildResponse(e.getExceptionEnum());
    }

    @ExceptionHandler(NotLoginException.class)
    public ResponseEntity<Result<Void>> handleNotLoginException(NotLoginException e) {
        log.warn("未登录或登录已过期：{}", e.getMessage());
        return buildResponse(CommonExceptionEnum.NOT_LOGIN);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Result<Void>> handleDuplicateKeyException(DuplicateKeyException e) {
        log.warn("唯一约束冲突", e);
        return buildResponse(CommonExceptionEnum.DUPLICATE_KEY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleException(Exception e) {
        log.error("系统异常", e);
        return buildResponse(CommonExceptionEnum.SYSTEM_ERROR);
    }

    /**
     * 按异常枚举设置响应体业务码、消息与 HTTP 状态码
     */
    private ResponseEntity<Result<Void>> buildResponse(ExceptionEnum exceptionEnum) {
        return ResponseEntity.status(exceptionEnum.getHttpCode())
                .body(Result.<Void>of(exceptionEnum.getCode(), exceptionEnum.getMsg(), null));
    }
}
