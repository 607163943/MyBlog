package com.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 评论模块异常枚举（模块编号 5）
 */
@Getter
@AllArgsConstructor
public enum CommentExceptionEnum implements ExceptionEnum {

    /**
     * 评论不存在
     */
    COMMENT_NOT_FOUND(501, "评论不存在", 404),

    /**
     * 评论 ID 为空
     */
    COMMENT_ID_REQUIRED(502, "评论ID不能为空", 400),

    /**
     * 批量删除时未选择评论
     */
    COMMENT_DELETE_IDS_REQUIRED(503, "请选择要删除的评论", 400),

    /**
     * 评论者昵称为空
     */
    COMMENT_NICKNAME_REQUIRED(504, "评论者昵称不能为空", 400),

    /**
     * 评论者昵称超长
     */
    COMMENT_NICKNAME_TOO_LONG(505, "评论者昵称不能超过 50 个字符", 400),

    /**
     * 邮箱超长
     */
    COMMENT_EMAIL_TOO_LONG(506, "邮箱不能超过 100 个字符", 400),

    /**
     * 邮箱格式不正确
     */
    COMMENT_EMAIL_INVALID(507, "邮箱格式不正确", 400),

    /**
     * 评论内容为空
     */
    COMMENT_CONTENT_REQUIRED(508, "评论内容不能为空", 400),

    /**
     * 评论内容超长
     */
    COMMENT_CONTENT_TOO_LONG(509, "评论内容不能超过 500 个字符", 400),

    /**
     * 审核状态取值不正确
     */
    COMMENT_STATUS_INVALID(510, "审核状态不正确", 400),

    /**
     * 回复的评论不存在或未通过审核
     */
    COMMENT_REPLY_TARGET_INVALID(511, "回复的评论不存在或未通过审核", 404);

    private final Integer code;

    private final String msg;

    private final int httpCode;
}
