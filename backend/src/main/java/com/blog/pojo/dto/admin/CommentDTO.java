package com.blog.pojo.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @NotNull(message = "评论ID不能为空")
    private Integer id;

    /**
     * 评论者昵称
     */
    @NotBlank(message = "评论者昵称不能为空")
    @Size(max = 50, message = "评论者昵称不能超过 50 个字符")
    private String nickname;

    /**
     * 评论者邮箱
     */
    @Size(max = 100, message = "邮箱不能超过 100 个字符")
    private String email;

    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空")
    private String content;

    /**
     * 审核状态：0-待审核，1-已通过
     */
    @Min(value = 0, message = "审核状态不正确")
    @Max(value = 1, message = "审核状态不正确")
    private Integer status;
}
