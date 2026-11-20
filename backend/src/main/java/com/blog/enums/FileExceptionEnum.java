package com.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件模块异常枚举（模块编号 8）
 */
@Getter
@AllArgsConstructor
public enum FileExceptionEnum implements ExceptionEnum {

    /**
     * 文件不存在
     */
    FILE_NOT_FOUND(801, "文件不存在", 404),

    /**
     * 日期范围非法
     */
    FILE_DATE_RANGE_INVALID(802, "开始日期不能晚于结束日期", 400),

    /**
     * 文件元数据不合法
     */
    FILE_METADATA_INVALID(803, "文件信息不合法", 400),

    /**
     * 文件仍被业务数据引用
     */
    FILE_REFERENCED(804, "文件正在使用中，无法删除", 409),

    /**
     * 物理文件删除失败
     */
    FILE_DELETE_IO_ERROR(805, "文件删除失败，请稍后重试", 500);

    private final Integer code;

    private final String msg;

    private final int httpCode;
}
