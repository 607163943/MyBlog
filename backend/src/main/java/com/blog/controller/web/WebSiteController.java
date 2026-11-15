package com.blog.controller.web;

import com.blog.common.Result;
import com.blog.pojo.vo.web.WebSiteStatisticsVO;
import com.blog.service.web.WebSiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前台站点信息接口
 */
@RestController
@RequestMapping("/web/site")
@RequiredArgsConstructor
public class WebSiteController {

    private final WebSiteService webSiteService;

    /**
     * 查询站点统计信息
     */
    @GetMapping("/statistics")
    public Result<WebSiteStatisticsVO> statistics() {
        return Result.success(webSiteService.getStatistics());
    }
}
