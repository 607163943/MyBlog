package com.blog.service.web;

import com.blog.pojo.vo.web.WebArchiveYearVO;

import java.util.List;

/**
 * 前台归档服务
 */
public interface WebArchiveService {

    /**
     * 查询归档概览
     */
    List<WebArchiveYearVO> listOverview();
}
