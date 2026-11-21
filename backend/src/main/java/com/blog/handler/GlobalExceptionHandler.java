package com.blog.handler;

import cn.dev33.satoken.exception.NotLoginException;
import com.blog.common.Result;
import com.blog.enums.CommonExceptionEnum;
import com.blog.enums.ExceptionEnum;
import com.blog.enums.UploadExceptionEnum;
import com.blog.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import javax.validation.ConstraintViolationException;
import java.util.Objects;

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

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Result<Void>> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException e) {
        log.warn("上传文件超过大小限制：{}", e.getMessage());
        return buildResponse(UploadExceptionEnum.UPLOAD_FILE_TOO_LARGE);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<Result<Void>> handleMultipartException(MultipartException e) {
        if (isSizeLimitExceeded(e)) {
            log.warn("上传文件超过大小限制：{}", e.getMessage());
            return buildResponse(UploadExceptionEnum.UPLOAD_FILE_TOO_LARGE);
        }
        log.warn("上传请求解析失败：{}", e.getMessage(), e);
        return buildResponse(UploadExceptionEnum.UPLOAD_MULTIPART_INVALID);
    }

    /**
     * 请求体参数校验失败
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("参数校验失败");
        return Result.error(msg);
    }

    /**
     * 方法参数校验失败
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
        String msg = e.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("参数校验失败");
        return Result.error(msg);
    }

    /**
     * 查询参数绑定失败（如 page=abc）
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBindException(BindException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .filter(error -> !error.isBindingFailure())
                .map(FieldError::getDefaultMessage)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("参数格式不正确");
        return Result.error(msg);
    }

    /**
     * 请求体缺失或 JSON 格式不正确
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return Result.error("请求体格式不正确");
    }

    /**
     * 请求参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return Result.error("参数格式不正确");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleException(Exception e) {
        log.error("系统异常", e);
        return buildResponse(CommonExceptionEnum.SYSTEM_ERROR);
    }

    /**
     * 兼容不同 Servlet 容器把大小超限包装成 MultipartException 的情况
     */
    private boolean isSizeLimitExceeded(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof MaxUploadSizeExceededException) {
                return true;
            }
            if (current.getClass().getName().endsWith("SizeLimitExceededException")) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    /**
     * 按异常枚举设置响应体业务码、消息与 HTTP 状态码
     */
    private ResponseEntity<Result<Void>> buildResponse(ExceptionEnum exceptionEnum) {
        return ResponseEntity.status(exceptionEnum.getHttpCode())
                .body(Result.<Void>of(exceptionEnum.getCode(), exceptionEnum.getMsg(), null));
    }
}
