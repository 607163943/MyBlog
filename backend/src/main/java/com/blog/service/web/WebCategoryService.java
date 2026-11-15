package com.blog.service.web;

import com.blog.pojo.vo.web.WebCategoryVO;

import java.util.List;

/**
 * 前台分类服务
 */
public interface WebCategoryService {

    /**
     * 查询全部分类及每类热门文章
     */
    List<WebCategoryVO> listWithHotArticles();

    /**
     * 查询分类详情
     */
    WebCategoryVO getDetail(Integer id);

    /**
     * 按文章数查询热门分类
     */
    List<WebCategoryVO> listTop(int limit);
}
