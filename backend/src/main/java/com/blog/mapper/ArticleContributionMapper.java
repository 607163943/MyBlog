package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.pojo.po.ArticleContribution;
import com.blog.pojo.vo.admin.ArticleContributionVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 文章每日贡献统计 Mapper
 */
public interface ArticleContributionMapper extends BaseMapper<ArticleContribution> {

    /**
     * 当天贡献次数 +1：无记录时新增，已有记录时累加
     */
    int increaseDailyContribution(@Param("userId") Integer userId,
                                  @Param("contributionDate") LocalDate contributionDate);

    /**
     * 查询指定用户在日期范围内的每日贡献
     */
    List<ArticleContributionVO> selectDailyContribution(@Param("userId") Integer userId,
                                                        @Param("startDate") LocalDate startDate,
                                                        @Param("endDate") LocalDate endDate);
}
