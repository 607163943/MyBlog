package com.blog.pojo.dto.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
    @NotBlank(message = "评论者昵称不能为空")
    @Size(max = 50, message = "评论者昵称不能超过 50 个字符")
    private String nickname;

    /**
     * 评论者邮箱（选填，不对外展示）
     */
    @Size(max = 100, message = "邮箱不能超过 100 个字符")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空")
    @Size(max = 500, message = "评论内容不能超过 500 个字符")
    private String content;

    /**
     * 被回复的评论 ID：为空或 0 表示发表一级评论
     */
    private Integer replyToId;
}
