package com.my.blog.server.service.impl;

import com.my.blog.common.constants.ArticleStatus;
import com.my.blog.pojo.vo.admin.AdminChartCardKPIVO;
import com.my.blog.pojo.vo.admin.AdminChartTrendCardVO;
import com.my.blog.pojo.vo.admin.RatioChartData;
import com.my.blog.pojo.vo.admin.TrendChartData;
import com.my.blog.server.service.IArticleService;
import com.my.blog.server.service.ICategoryService;
import com.my.blog.server.service.IChartService;
import com.my.blog.server.service.ITagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ChartServiceImpl implements IChartService {
    @Resource
    private IArticleService articleService;
    @Resource
    private ICategoryService categoryService;
    @Resource
    private ITagService tagService;

    /**
     * 获取卡片数据
     *
     * @return 卡片数据
     */
    @Override
    public AdminChartCardKPIVO chartCardKPI() {
        // 最近新增文章数
        Long addArticleCount = articleService.countArticle7Day(null);
        // 最近发布文章数
        Long publishArticleCount = articleService.countArticle7Day(ArticleStatus.PUBLISH);
        // 文章总数
        long totalArticleCount = articleService.count();
        // 分类总数
        long categoryCount = categoryService.count();
        // 标签总数
        long tagCount = tagService.count();

        return AdminChartCardKPIVO.builder()
                .addArticleCount(addArticleCount)
                .publishArticleCount(publishArticleCount)
                .totalArticleCount(totalArticleCount)
                .categoryCount(categoryCount)
                .tagCount(tagCount)
                .build();
    }

    /**
     * 获取趋势数据
     *
     * @return 趋势数据
     */
    @Override
    public AdminChartTrendCardVO chartCardTrend() {
        // 新增文章趋势
        List<TrendChartData> trendChartData = articleService.trend7Day();
        // 文章状态占比
        List<RatioChartData> articleStatusRatio = articleService.countGroupByStatus();
        // 分类状态占比
        List<RatioChartData> categoryStatusRatio = categoryService.countGroupByStatus();
        // 标签状态占比
        List<RatioChartData> tagStatusRatio = tagService.countGroupByStatus();

        return AdminChartTrendCardVO.builder()
                .addArticleTrendData(trendChartData)
                .articleStatusRatioData(articleStatusRatio)
                .categoryStatusRatioData(categoryStatusRatio)
                .tagStatusRatioData(tagStatusRatio)
                .build();
    }
}
