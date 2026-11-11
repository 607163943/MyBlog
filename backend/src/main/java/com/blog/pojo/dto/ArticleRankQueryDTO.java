package com.blog.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 文章浏览量排行查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ArticleRankQueryDTO extends PageQueryDTO {

    /**
     * 文章标题，模糊搜索
     */
    private String title;

    /**
     * 分类 ID
     */
    private Integer categoryId;

    /**
     * 标签 ID
     */
    private Integer tagId;
}
