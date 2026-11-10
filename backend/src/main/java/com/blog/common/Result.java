package com.blog.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    public static final int SUCCESS_CODE = 200;
    public static final int ERROR_CODE = 500;

    /**
     * 业务状态码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 创建响应结果
     */
    public static <T> Result<T> of(Integer code, String msg, T data) {
        return new Result<>(code, msg, data);
    }

    public static <T> Result<T> success() {
        return of(SUCCESS_CODE, "操作成功", null);
    }

    public static <T> Result<T> success(T data) {
        return of(SUCCESS_CODE, "操作成功", data);
    }

    public static <T> Result<T> error(String msg) {
        return of(ERROR_CODE, msg, null);
    }

    public static <T> Result<T> error(Integer code, String msg) {
        return of(code, msg, null);
    }
}
