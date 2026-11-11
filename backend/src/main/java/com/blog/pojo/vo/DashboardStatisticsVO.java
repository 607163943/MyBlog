package com.blog.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据总览统计卡片
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatisticsVO {

    /**
     * 文章总数
     */
    private Long articleTotal;

    /**
     * 草稿数
     */
    private Long draftCount;

    /**
     * 已发布数
     */
    private Long publishedCount;

    /**
     * 分类总数
     */
    private Long categoryCount;

    /**
     * 标签总数
     */
    private Long tagCount;
}
