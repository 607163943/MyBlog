package com.blog.service.web;

import com.blog.pojo.vo.web.WebTagVO;

import java.util.List;

/**
 * 前台标签服务
 */
public interface WebTagService {

    /**
     * 查询全部标签
     */
    List<WebTagVO> listAll();

    /**
     * 查询标签详情
     */
    WebTagVO getDetail(Integer id);

    /**
     * 按文章数查询热门标签
     */
    List<WebTagVO> listTop(int limit);
}
