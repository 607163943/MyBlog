package com.blog.service.web.impl;

import com.blog.mapper.ArticleMapper;
import com.blog.pojo.vo.web.WebSiteStatisticsVO;
import com.blog.service.web.WebSiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 前台站点信息服务实现
 */
@Service
@RequiredArgsConstructor
public class WebSiteServiceImpl implements WebSiteService {

    private final ArticleMapper articleMapper;

    @Override
    public WebSiteStatisticsVO getStatistics() {
        WebSiteStatisticsVO statistics = articleMapper.selectWebSiteStatistics();
        if (statistics == null) {
            return new WebSiteStatisticsVO(0L, 0L, 0L, 0L);
        }
        return statistics;
    }
}
