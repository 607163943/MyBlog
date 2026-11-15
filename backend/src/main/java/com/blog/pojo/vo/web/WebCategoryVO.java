package com.blog.pojo.vo.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 前台分类信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebCategoryVO {

    /**
     * 分类 ID
     */
    private Integer id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类描述
     */
    private String description;

    /**
     * 已发布文章数
     */
    private Long articleCount;

    /**
     * 分类下浏览量 Top3 文章
     */
    private List<WebArticleBriefVO> hotArticles;
}
