package com.blog.pojo.vo.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 前台文章简要信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebArticleBriefVO {

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
