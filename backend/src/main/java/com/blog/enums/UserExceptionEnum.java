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
    USER_NOT_FOUND(103, "用户不存在", 404),

    /**
     * 昵称为空
     */
    NICKNAME_EMPTY(104, "昵称不能为空", 400),

    /**
     * 昵称长度超出限制
     */
    NICKNAME_TOO_LONG(105, "昵称长度不能超过 20 个字符", 400),

    /**
     * 邮箱格式不正确
     */
    EMAIL_INVALID(106, "邮箱格式不正确", 400),

    /**
     * 头像地址过长
     */
    AVATAR_TOO_LONG(107, "头像地址长度不能超过 255 个字符", 400),

    /**
     * 旧密码为空
     */
    OLD_PASSWORD_EMPTY(108, "旧密码不能为空", 400),

    /**
     * 新密码长度不符合要求
     */
    NEW_PASSWORD_LENGTH_INVALID(109, "新密码长度需为 6-20 个字符", 400),

    /**
     * 新密码与旧密码相同
     */
    NEW_PASSWORD_SAME(110, "新密码不能与旧密码相同", 400),

    /**
     * 旧密码错误
     */
    OLD_PASSWORD_ERROR(111, "旧密码错误", 400),

    /**
     * 验证码为空
     */
    CAPTCHA_EMPTY(112, "验证码不能为空", 400),

    /**
     * 验证码错误或已过期
     */
    CAPTCHA_ERROR(113, "验证码错误或已过期", 400);

    private final Integer code;

    private final String msg;

    private final int httpCode;
}
