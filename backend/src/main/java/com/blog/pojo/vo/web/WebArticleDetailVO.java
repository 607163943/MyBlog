package com.blog.pojo.vo.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 前台文章详情
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WebArticleDetailVO extends WebArticleVO {

    /**
     * 文章正文（Markdown 格式）
     */
    private String content;

    /**
     * 分类内排序值：值越小越靠前，null 表示未编号
     */
    private Integer categorySort;

    /**
     * 上一篇（同分类中排序值更小的已发布文章）
     */
    private WebArticleBriefVO prevArticle;

    /**
     * 下一篇（同分类中排序值更大的已发布文章）
     */
    private WebArticleBriefVO nextArticle;
}
