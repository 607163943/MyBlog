package com.my.blog.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionEnums {

    ADMIN_USER_NOT_EXIST(1001, "用户不存在"),
    ADMIN_USER_PASSWORD_ERROR(1002, "密码错误"),
    ADMIN_USER_CAPTCHA_CODE_ERROR(1003, "验证码错误"),
    ADMIN_USER_CAPTCHA_EXPIRED(1004, "验证码已过期"),
    ADMIN_USER_LOGIN_TIMEOUT(1005, "登录超时"),
    ADMIN_USER_NOT_LOGIN(1006, "用户未登录"),

    ADMIN_TAG_EXIST(2001, "标签已存在"),

    ADMIN_CATEGORY_EXIST(3001, "分类已存在"),
    ADMIN_CATEGORY_COVER_NOT_EXIST(3002, "分类封面不存在"),
    ADMIN_UPLOAD_MD5_CREATE_ERROR(4001, "创建文件MD5值生成失败");

    private final Integer code;
    private final String msg;
}
