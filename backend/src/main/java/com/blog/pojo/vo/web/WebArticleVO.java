package com.blog.pojo.vo.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 前台文章列表项
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebArticleVO {

    /**
     * 文章 ID
     */
    private Integer id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章描述 / 摘要
     */
    private String description;

    /**
     * 封面图片地址
     */
    private String cover;

    /**
     * 所属分类 ID
     */
    private Integer categoryId;

    /**
     * 所属分类名称
     */
    private String categoryName;

    /**
     * 标签列表
     */
    private List<WebTagBriefVO> tags;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 首次发布时间
     */
    private LocalDateTime publishTime;
}
