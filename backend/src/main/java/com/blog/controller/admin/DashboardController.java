package com.blog.controller.admin;

import com.blog.common.Result;
import com.blog.pojo.dto.admin.ArticleRankQueryDTO;
import com.blog.pojo.dto.admin.CategoryRankQueryDTO;
import com.blog.pojo.vo.admin.ArticleRankVO;
import com.blog.pojo.vo.admin.CategoryRankVO;
import com.blog.pojo.vo.admin.DashboardStatisticsVO;
import com.blog.pojo.vo.admin.ViewTrendVO;
import com.blog.pojo.vo.PageVO;
import com.blog.service.admin.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据总览接口
 */
@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * 统计卡片数据
     */
    @GetMapping("/statistics")
    public Result<DashboardStatisticsVO> statistics() {
        return Result.success(dashboardService.getStatistics());
    }

    /**
     * 分页查询文章浏览量排行
     */
    @GetMapping("/article-rank/page")
    public Result<PageVO<ArticleRankVO>> articleRankPage(ArticleRankQueryDTO dto) {
        return Result.success(dashboardService.pageArticleRank(dto));
    }

    /**
     * 分页查询分类文章数排行
     */
    @GetMapping("/category-rank/page")
    public Result<PageVO<CategoryRankVO>> categoryRankPage(CategoryRankQueryDTO dto) {
        return Result.success(dashboardService.pageCategoryRank(dto));
    }

    /**
     * 查询文章每日浏览量趋势（最近 90 天）
     */
    @GetMapping("/view-trend")
    public Result<List<ViewTrendVO>> viewTrend() {
        return Result.success(dashboardService.getViewTrend());
    }
}
