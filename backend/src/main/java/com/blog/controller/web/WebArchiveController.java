package com.blog.controller.web;

import com.blog.common.Result;
import com.blog.pojo.vo.web.WebArchiveYearVO;
import com.blog.pojo.vo.web.WebArticleVO;
import com.blog.service.web.WebArchiveService;
import com.blog.service.web.WebArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前台归档接口
 */
@RestController
@RequestMapping("/web/archive")
@RequiredArgsConstructor
public class WebArchiveController {

    private final WebArchiveService webArchiveService;

    private final WebArticleService webArticleService;

    /**
     * 查询归档概览
     */
    @GetMapping("/overview")
    public Result<List<WebArchiveYearVO>> overview() {
        return Result.success(webArchiveService.listOverview());
    }

    /**
     * 查询指定年月的文章
     */
    @GetMapping("/{year}/{month}")
    public Result<List<WebArticleVO>> month(@PathVariable Integer year,
                                            @PathVariable Integer month) {
        return Result.success(webArticleService.listByMonth(year, month));
    }
}
