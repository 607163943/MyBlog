package com.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 分类模块异常枚举（模块编号 3）
 */
@Getter
@AllArgsConstructor
public enum CategoryExceptionEnum implements ExceptionEnum {

    /**
     * 分类不存在
     */
    CATEGORY_NOT_FOUND(301, "分类不存在", 404),

    /**
     * 分类 ID 为空
     */
    CATEGORY_ID_REQUIRED(302, "分类ID不能为空", 400),

    /**
     * 批量删除时未选择分类
     */
    CATEGORY_DELETE_IDS_REQUIRED(303, "请选择要删除的分类", 400),

    /**
     * 分类名称为空
     */
    CATEGORY_NAME_REQUIRED(304, "分类名称不能为空", 400),

    /**
     * 分类名称已存在
     */
    CATEGORY_NAME_EXISTS(305, "分类名称已存在", 409),

    /**
     * 文章所选分类不存在
     */
    CATEGORY_SELECTED_NOT_FOUND(306, "所选分类不存在", 404);

    private final Integer code;

    private final String msg;

    private final int httpCode;
}
