package com.blog.controller;

import com.blog.common.Result;
import com.blog.pojo.dto.ArticleDTO;
import com.blog.pojo.dto.ArticlePageQueryDTO;
import com.blog.pojo.vo.ArticleVO;
import com.blog.pojo.vo.PageVO;
import com.blog.service.ArticleService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 文章管理接口
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    /**
     * 分页查询文章
     */
    @GetMapping("/page")
    public Result<PageVO<ArticleVO>> page(ArticlePageQueryDTO dto) {
        return Result.success(articleService.pageQuery(dto));
    }

    /**
     * 根据 ID 查询文章
     */
    @GetMapping("/{id}")
    public Result<ArticleVO> getById(@PathVariable Integer id) {
        return Result.success(articleService.getArticleById(id));
    }

    /**
     * 新增文章
     */
    @PostMapping
    public Result<Void> add(@RequestBody ArticleDTO dto) {
        articleService.addArticle(dto);
        return Result.success();
    }

    /**
     * 修改文章
     */
    @PutMapping
    public Result<Void> update(@RequestBody ArticleDTO dto) {
        articleService.updateArticle(dto);
        return Result.success();
    }

    /**
     * 删除单个文章
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        articleService.deleteArticle(id);
        return Result.success();
    }

    /**
     * 批量删除文章
     */
    @DeleteMapping("/batch")
    public Result<Void> deleteBatch(@RequestBody List<Integer> ids) {
        articleService.deleteArticles(ids);
        return Result.success();
    }
}
