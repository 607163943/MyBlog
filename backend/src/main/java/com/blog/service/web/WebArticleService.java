package com.blog.service.web;

import com.blog.pojo.dto.web.WebArticlePageQueryDTO;
import com.blog.pojo.vo.PageVO;
import com.blog.pojo.vo.web.WebArticleBriefVO;
import com.blog.pojo.vo.web.WebArticleDetailVO;
import com.blog.pojo.vo.web.WebArticleVO;

import java.util.List;

/**
 * 前台文章服务
 */
public interface WebArticleService {

    /**
     * 分页查询已发布文章
     */
    PageVO<WebArticleVO> pagePublished(WebArticlePageQueryDTO dto);

    /**
     * 查询最新发布的文章
     */
    List<WebArticleBriefVO> listLatest(int limit);

    /**
     * 查询指定分类下的全部已发布文章
     */
    List<WebArticleBriefVO> listByCategory(Integer categoryId);

    /**
     * 查询指定年月的已发布文章
     */
    List<WebArticleVO> listByMonth(int year, int month);

    /**
     * 查询文章详情并自增浏览量
     */
    WebArticleDetailVO getDetail(Integer id);
}
