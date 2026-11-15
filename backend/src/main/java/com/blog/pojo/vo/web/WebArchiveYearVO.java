package com.blog.pojo.vo.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 前台归档年份信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebArchiveYearVO {

    /**
     * 年份
     */
    private Integer year;

    /**
     * 该年份下有文章的月份
     */
    private List<WebArchiveMonthVO> months;
}
