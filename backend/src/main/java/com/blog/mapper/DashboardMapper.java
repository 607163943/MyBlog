package com.blog.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blog.pojo.vo.ArticleRankVO;
import com.blog.pojo.vo.ArticleTagNameVO;
import com.blog.pojo.vo.CategoryRankVO;
import com.blog.pojo.vo.DashboardStatisticsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据总览聚合查询 Mapper
 */
public interface DashboardMapper {

    /**
     * 查询统计卡片数据
     */
    DashboardStatisticsVO selectStatistics();

    /**
     * 分页查询文章浏览量排行
     */
    IPage<ArticleRankVO> selectArticleRankPage(IPage<ArticleRankVO> page,
                                               @Param("title") String title,
                                               @Param("categoryId") Integer categoryId,
                                               @Param("tagId") Integer tagId);

    /**
     * 查询文章标签名称
     */
    List<ArticleTagNameVO> selectTagNamesByArticleIds(@Param("articleIds") List<Integer> articleIds);

    /**
     * 分页查询分类文章数排行
     */
    IPage<CategoryRankVO> selectCategoryRankPage(IPage<CategoryRankVO> page,
                                                 @Param("name") String name,
                                                 @Param("minCount") Integer minCount,
                                                 @Param("maxCount") Integer maxCount);
}
