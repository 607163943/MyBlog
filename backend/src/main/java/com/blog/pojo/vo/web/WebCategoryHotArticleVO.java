package com.blog.pojo.vo.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类热门文章查询结果，用于按分类分组回填
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebCategoryHotArticleVO {

    /**
     * 分类 ID
     */
    private Integer categoryId;

    /**
     * 文章 ID
     */
    private Integer id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 浏览量
     */
    private Integer viewCount;
}
