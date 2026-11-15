package com.blog.pojo.dto.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 前台评论提交参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebCommentSubmitDTO {

    /**
     * 评论者昵称
     */
    private String nickname;

    /**
     * 评论者邮箱（选填，不对外展示）
     */
    private String email;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 被回复的评论 ID：为空或 0 表示发表一级评论
     */
    private Integer replyToId;
}
