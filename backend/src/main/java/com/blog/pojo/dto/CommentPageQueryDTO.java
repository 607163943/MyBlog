package com.blog.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 评论分页查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommentPageQueryDTO extends PageQueryDTO {

    /**
     * 所属文章标题，模糊搜索
     */
    private String articleTitle;

    /**
     * 审核状态：0-待审核，1-已通过，为空表示全部
     */
    private Integer status;
}
