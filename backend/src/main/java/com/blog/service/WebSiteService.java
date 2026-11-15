package com.blog.service;

import com.blog.pojo.vo.web.WebSiteStatisticsVO;

/**
 * 前台站点信息服务
 */
public interface WebSiteService {

    /**
     * 查询站点统计信息
     */
    WebSiteStatisticsVO getStatistics();
}
