package com.blog.pojo.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 文章每日贡献统计
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleContributionVO {

    /**
     * 贡献日期
     */
    private LocalDate contributionDate;

    /**
     * 当日贡献次数
     */
    private Integer contributionCount;
}
