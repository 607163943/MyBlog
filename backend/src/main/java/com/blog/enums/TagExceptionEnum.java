package com.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 标签模块异常枚举（模块编号 4）
 */
@Getter
@AllArgsConstructor
public enum TagExceptionEnum implements ExceptionEnum {

    /**
     * 标签不存在
     */
    TAG_NOT_FOUND(401, "标签不存在", 404),

    /**
     * 标签 ID 为空
     */
    TAG_ID_REQUIRED(402, "标签ID不能为空", 400),

    /**
     * 批量删除时未选择标签
     */
    TAG_DELETE_IDS_REQUIRED(403, "请选择要删除的标签", 400),

    /**
     * 标签名称为空
     */
    TAG_NAME_REQUIRED(404, "标签名称不能为空", 400),

    /**
     * 标签名称已存在
     */
    TAG_NAME_EXISTS(405, "标签名称已存在", 409),

    /**
     * 文章所选标签不存在
     */
    TAG_SELECTED_NOT_FOUND(406, "所选标签不存在", 404);

    private final Integer code;

    private final String msg;

    private final int httpCode;
}
