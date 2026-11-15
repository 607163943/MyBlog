package com.blog.pojo.vo.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 前台归档月份信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebArchiveMonthVO {

    /**
     * 年份
     */
    private Integer year;

    /**
     * 月份（1-12）
     */
    private Integer month;

    /**
     * 该月已发布文章数
     */
    private Long articleCount;
}
