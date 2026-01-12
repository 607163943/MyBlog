package com.my.blog.server.service;

import com.my.blog.pojo.vo.admin.AdminChartCardKPIVO;
import com.my.blog.pojo.vo.admin.AdminChartTrendCardVO;

public interface IChartService {
    /**
     * 获取首页图表数据
     * @return 管理端首页图表数据
     */
    AdminChartCardKPIVO chartCardKPI();

    /**
     * 获取首页图表趋势数据
     * @return 管理端首页图表趋势数据
     */
    AdminChartTrendCardVO chartCardTrend();
}
