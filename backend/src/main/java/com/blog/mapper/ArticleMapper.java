package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blog.pojo.po.Article;
import com.blog.pojo.vo.admin.ArticleRankVO;
import com.blog.pojo.vo.admin.ArticleVO;
import com.blog.pojo.vo.admin.DashboardStatisticsVO;
import com.blog.pojo.vo.web.WebArchiveMonthVO;
import com.blog.pojo.vo.web.WebArticleBriefVO;
import com.blog.pojo.vo.web.WebArticleDetailVO;
import com.blog.pojo.vo.web.WebArticleVO;
import com.blog.pojo.vo.web.WebSiteStatisticsVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章 Mapper
 */
public interface ArticleMapper extends BaseMapper<Article> {

    /* ==================== 后台管理 ==================== */

    /**
     * 分页查询文章，复杂查询下沉到 XML 实现
     */
    IPage<ArticleVO> selectArticlePage(IPage<ArticleVO> page,
                                       @Param("title") String title,
                                       @Param("status") Integer status);

    /**
     * 根据 ID 查询文章详情（含分类名称、作者昵称）
     */
    ArticleVO selectArticleDetailById(@Param("id") Integer id);

    /* ==================== 前台展示 ==================== */

    /**
     * 前台分页查询已发布文章
     */
    IPage<WebArticleVO> selectWebArticlePage(IPage<WebArticleVO> page,
                                             @Param("categoryId") Integer categoryId,
                                             @Param("tagId") Integer tagId,
                                             @Param("keyword") String keyword);

    /**
     * 前台根据 ID 查询已发布文章详情
     */
    WebArticleDetailVO selectWebArticleDetailById(@Param("id") Integer id);

    /**
     * 自增文章浏览量，返回影响行数
     */
    int incrementViewCount(@Param("id") Integer id);

    /**
     * 查询较新的上一篇已发布文章
     */
    WebArticleBriefVO selectNewerPublished(@Param("createTime") LocalDateTime createTime,
                                           @Param("id") Integer id);

    /**
     * 查询较旧的下一篇已发布文章
     */
    WebArticleBriefVO selectOlderPublished(@Param("createTime") LocalDateTime createTime,
                                           @Param("id") Integer id);

    /**
     * 查询最新发布的文章
     */
    List<WebArticleBriefVO> selectLatestPublished(@Param("limit") Integer limit);

    /**
     * 查询指定分类下的全部已发布文章
     */
    List<WebArticleBriefVO> selectPublishedByCategory(@Param("categoryId") Integer categoryId);

    /**
     * 查询归档概览：按年月统计已发布文章数
     */
    List<WebArchiveMonthVO> selectWebArchiveOverview();

    /**
     * 查询指定时间范围内的已发布文章
     */
    List<WebArticleVO> selectPublishedByTimeRange(@Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime);

    /* ==================== 数据统计 ==================== */

    /**
     * 查询后台统计卡片数据：文章总数、草稿数、已发布数，以及分类数、标签数
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
     * 查询前台站点统计信息
     */
    WebSiteStatisticsVO selectWebSiteStatistics();
}
