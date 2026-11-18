package com.blog.service.admin;

import com.blog.pojo.dto.admin.ArticleRankQueryDTO;
import com.blog.pojo.dto.admin.CategoryRankQueryDTO;
import com.blog.pojo.vo.admin.ArticleContributionVO;
import com.blog.pojo.vo.admin.ArticleRankVO;
import com.blog.pojo.vo.admin.CategoryRankVO;
import com.blog.pojo.vo.admin.DashboardStatisticsVO;
import com.blog.pojo.vo.admin.ViewTrendVO;
import com.blog.pojo.vo.PageVO;

import java.util.List;

/**
 * 数据总览服务
 */
public interface DashboardService {

    /**
     * 查询统计卡片数据
     */
    DashboardStatisticsVO getStatistics();

    /**
     * 分页查询文章浏览量排行
     */
    PageVO<ArticleRankVO> pageArticleRank(ArticleRankQueryDTO dto);

    /**
     * 分页查询分类文章数排行
     */
    PageVO<CategoryRankVO> pageCategoryRank(CategoryRankQueryDTO dto);

    /**
     * 查询最近 30 天的文章每日浏览量趋势
     */
    List<ViewTrendVO> getViewTrend();

    /**
     * 查询当前用户最近一年的文章每日贡献
     */
    List<ArticleContributionVO> getArticleContribution();

    /**
     * 记录当天已发布文章总浏览量快照：不存在则插入，存在则更新
     */
    void recordViewSnapshot();
}
