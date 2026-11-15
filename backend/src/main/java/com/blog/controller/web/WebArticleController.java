package com.blog.controller.web;

import com.blog.common.Result;
import com.blog.pojo.dto.web.WebArticlePageQueryDTO;
import com.blog.pojo.vo.PageVO;
import com.blog.pojo.vo.web.WebArticleBriefVO;
import com.blog.pojo.vo.web.WebArticleDetailVO;
import com.blog.pojo.vo.web.WebArticleVO;
import com.blog.service.web.WebArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前台文章接口
 */
@RestController
@RequestMapping("/web/article")
@RequiredArgsConstructor
public class WebArticleController {

    private final WebArticleService webArticleService;

    /**
     * 分页查询已发布文章（首页 / 分类 / 标签 / 搜索共用）
     */
    @GetMapping("/page")
    public Result<PageVO<WebArticleVO>> page(WebArticlePageQueryDTO dto) {
        return Result.success(webArticleService.pagePublished(dto));
    }

    /**
     * 查询最新发布的文章
     */
    @GetMapping("/latest")
    public Result<List<WebArticleBriefVO>> latest(
            @RequestParam(value = "limit", defaultValue = "8") Integer limit) {
        return Result.success(webArticleService.listLatest(limit));
    }

    /**
     * 查询文章详情
     */
    @GetMapping("/{id}")
    public Result<WebArticleDetailVO> detail(@PathVariable Integer id) {
        return Result.success(webArticleService.getDetail(id));
    }
}
