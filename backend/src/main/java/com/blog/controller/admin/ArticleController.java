package com.blog.controller.admin;

import com.blog.annotation.OperationLog;
import com.blog.common.Result;
import com.blog.pojo.dto.admin.ArticleDTO;
import com.blog.pojo.dto.admin.ArticlePageQueryDTO;
import com.blog.pojo.vo.admin.ArticleVO;
import com.blog.pojo.vo.PageVO;
import com.blog.service.admin.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 文章管理接口
 */
@RestController
@RequestMapping("/admin/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

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
    @OperationLog(module = "article", action = "新增文章", key = "#a0.title")
    @PostMapping
    public Result<Void> add(@RequestBody ArticleDTO dto) {
        articleService.addArticle(dto);
        return Result.success();
    }

    /**
     * 修改文章
     */
    @OperationLog(module = "article", action = "修改文章", key = "#a0.id")
    @PutMapping
    public Result<Void> update(@RequestBody ArticleDTO dto) {
        articleService.updateArticle(dto);
        return Result.success();
    }

    /**
     * 删除单个文章
     */
    @OperationLog(module = "article", action = "删除文章", key = "#a0")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        articleService.deleteArticle(id);
        return Result.success();
    }

    /**
     * 批量删除文章
     */
    @OperationLog(module = "article", action = "批量删除文章", key = "#a0")
    @DeleteMapping("/batch")
    public Result<Void> deleteBatch(@RequestBody List<Integer> ids) {
        articleService.deleteArticles(ids);
        return Result.success();
    }
}
