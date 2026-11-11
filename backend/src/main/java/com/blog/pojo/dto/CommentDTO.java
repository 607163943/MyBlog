package com.blog.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论修改参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    /**
     * 评论 ID，修改时必填
     */
    private Integer id;

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
}
