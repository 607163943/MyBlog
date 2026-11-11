package com.blog.controller;

import com.blog.common.Result;
import com.blog.pojo.dto.ArticleRankQueryDTO;
import com.blog.pojo.dto.CategoryRankQueryDTO;
import com.blog.pojo.vo.ArticleRankVO;
import com.blog.pojo.vo.CategoryRankVO;
import com.blog.pojo.vo.DashboardStatisticsVO;
import com.blog.pojo.vo.PageVO;
import com.blog.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据总览接口
 */
@RestController
@RequestMapping("/dashboard")
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
}
