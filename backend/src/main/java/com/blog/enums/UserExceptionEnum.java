package com.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户模块异常枚举（模块编号 1）
 */
@Getter
@AllArgsConstructor
public enum UserExceptionEnum implements ExceptionEnum {

    /**
     * 用户名或密码为空
     */
    USERNAME_OR_PASSWORD_EMPTY(101, "用户名和密码不能为空", 400),

    /**
     * 用户名或密码错误
     */
    USERNAME_OR_PASSWORD_ERROR(102, "用户名或密码错误", 401),

    /**
     * 用户不存在
     */
    USER_NOT_FOUND(103, "用户不存在", 404);

    private final Integer code;

    private final String msg;

    private final int httpCode;
}
