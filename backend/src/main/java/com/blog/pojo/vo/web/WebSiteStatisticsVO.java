package com.blog.pojo.vo.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 前台站点统计信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSiteStatisticsVO {

    /**
     * 已发布文章数
     */
    private Long articleCount;

    /**
     * 分类总数
     */
    private Long categoryCount;

    /**
     * 标签总数
     */
    private Long tagCount;

    /**
     * 已发布文章总浏览量
     */
    private Long viewCount;
}
