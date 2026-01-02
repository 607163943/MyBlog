package com.my.blog.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionEnums {

    ADMIN_USER_NOT_EXIST(1001, "用户不存在"),
    ADMIN_USER_PASSWORD_ERROR(1002, "密码错误"),
    ADMIN_USER_CAPTCHA_CODE_ERROR(1003, "验证码错误"),
    ADMIN_USER_CAPTCHA_EXPIRED(1004, "验证码已过期");

    private final Integer code;
    private final String msg;
}
