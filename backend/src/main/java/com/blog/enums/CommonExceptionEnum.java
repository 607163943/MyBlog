package com.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 公共异常枚举（模块编号 9）
 */
@Getter
@AllArgsConstructor
public enum CommonExceptionEnum implements ExceptionEnum {

    /**
     * 未登录或登录已过期
     */
    NOT_LOGIN(901, "未登录或登录已过期，请重新登录", 401),

    /**
     * 唯一约束冲突
     */
    DUPLICATE_KEY(902, "数据已存在，请勿重复添加", 409),

    /**
     * 未预期的系统异常
     */
    SYSTEM_ERROR(903, "系统繁忙，请稍后重试", 500);

    private final Integer code;

    private final String msg;

    private final int httpCode;
}
