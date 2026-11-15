package com.blog.controller.web;

import com.blog.common.Result;
import com.blog.pojo.vo.web.WebTagVO;
import com.blog.service.web.WebTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前台标签接口
 */
@RestController
@RequestMapping("/web/tag")
@RequiredArgsConstructor
public class WebTagController {

    private final WebTagService webTagService;

    /**
     * 查询全部标签
     */
    @GetMapping("/list")
    public Result<List<WebTagVO>> list() {
        return Result.success(webTagService.listAll());
    }

    /**
     * 按文章数查询热门标签
     */
    @GetMapping("/top")
    public Result<List<WebTagVO>> top(
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        return Result.success(webTagService.listTop(limit));
    }

    /**
     * 查询标签详情
     */
    @GetMapping("/{id}")
    public Result<WebTagVO> detail(@PathVariable Integer id) {
        return Result.success(webTagService.getDetail(id));
    }
}
