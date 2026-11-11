package com.blog.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 评论返回结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVO {

    /**
     * 评论 ID
     */
    private Integer id;

    /**
     * 所属文章 ID
     */
    private Integer articleId;

    /**
     * 所属文章标题
     */
    private String articleTitle;

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
     * 评论者邮箱
     */
    private String email;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 审核状态：0-待审核，1-已通过
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
