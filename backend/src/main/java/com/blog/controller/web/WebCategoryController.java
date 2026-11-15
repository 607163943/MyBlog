package com.blog.controller.web;

import com.blog.common.Result;
import com.blog.pojo.vo.web.WebArticleBriefVO;
import com.blog.pojo.vo.web.WebCategoryVO;
import com.blog.service.WebArticleService;
import com.blog.service.WebCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前台分类接口
 */
@RestController
@RequestMapping("/web/category")
@RequiredArgsConstructor
public class WebCategoryController {

    private final WebCategoryService webCategoryService;

    private final WebArticleService webArticleService;

    /**
     * 查询全部分类及每类热门文章
     */
    @GetMapping("/list")
    public Result<List<WebCategoryVO>> list() {
        return Result.success(webCategoryService.listWithHotArticles());
    }

    /**
     * 按文章数查询热门分类
     */
    @GetMapping("/top")
    public Result<List<WebCategoryVO>> top(
            @RequestParam(value = "limit", defaultValue = "5") Integer limit) {
        return Result.success(webCategoryService.listTop(limit));
    }

    /**
     * 查询分类详情
     */
    @GetMapping("/{id}")
    public Result<WebCategoryVO> detail(@PathVariable Integer id) {
        return Result.success(webCategoryService.getDetail(id));
    }

    /**
     * 查询分类下的全部文章（文章详情页分类目录）
     */
    @GetMapping("/{id}/articles")
    public Result<List<WebArticleBriefVO>> articles(@PathVariable Integer id) {
        return Result.success(webArticleService.listByCategory(id));
    }
}
