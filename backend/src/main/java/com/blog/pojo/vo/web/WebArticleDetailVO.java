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
     * 上一篇（较新的已发布文章）
     */
    private WebArticleBriefVO prevArticle;

    /**
     * 下一篇（较旧的已发布文章）
     */
    private WebArticleBriefVO nextArticle;
}
