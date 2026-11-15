package com.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据总览模块异常枚举（模块编号 6）
 */
@Getter
@AllArgsConstructor
public enum DashboardExceptionEnum implements ExceptionEnum {

    /**
     * 最小文章数为负数
     */
    DASHBOARD_MIN_COUNT_NEGATIVE(601, "最小文章数不能为负数", 400),

    /**
     * 最大文章数为负数
     */
    DASHBOARD_MAX_COUNT_NEGATIVE(602, "最大文章数不能为负数", 400),

    /**
     * 文章数范围不正确
     */
    DASHBOARD_COUNT_RANGE_INVALID(603, "最小文章数不能大于最大文章数", 400);

    private final Integer code;

    private final String msg;

    private final int httpCode;
}
