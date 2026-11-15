package com.blog.pojo.vo.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 前台评论信息（二级树，不包含邮箱）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebCommentVO {

    /**
     * 评论 ID
     */
    private Integer id;

    /**
     * 所属一级评论 ID：0 表示自身为一级评论
     */
    private Integer parentId;

    /**
     * 被回复的评论 ID：0 表示无回复对象
     */
    private Integer replyToId;

    /**
     * 评论者昵称
     */
    private String nickname;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 被回复者昵称
     */
    private String replyToNickname;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 二级回复列表
     */
    private List<WebCommentVO> children;
}
