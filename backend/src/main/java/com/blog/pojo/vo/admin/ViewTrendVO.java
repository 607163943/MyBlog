package com.blog.pojo.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 文章每日浏览量趋势点
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewTrendVO {

    /**
     * 快照日期
     */
    private LocalDate snapshotDate;

    /**
     * 当日新增浏览量：当天快照总量减去前一日快照总量，无前一日快照时为 null
     */
    private Long dailyViewCount;
}
