package com.blog.service.admin;

import com.blog.pojo.dto.admin.ArticleRankQueryDTO;
import com.blog.pojo.dto.admin.CategoryRankQueryDTO;
import com.blog.pojo.vo.admin.ArticleRankVO;
import com.blog.pojo.vo.admin.CategoryRankVO;
import com.blog.pojo.vo.admin.DashboardStatisticsVO;
import com.blog.pojo.vo.PageVO;

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
}
