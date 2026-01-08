package com.my.blog.server.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import com.my.blog.common.result.PageResult;
import com.my.blog.common.result.Result;
import com.my.blog.pojo.dto.admin.AdminArticlePageQueryDTO;
import com.my.blog.pojo.po.Article;
import com.my.blog.pojo.vo.admin.AdminArticlePageQueryVO;
import com.my.blog.pojo.vo.admin.AdminArticleVO;
import com.my.blog.server.service.IArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "文章管理")
@RestController
@RequestMapping("/admin/article")
public class ArticleController {
    @Resource
    private IArticleService articleService;

    @ApiOperation("分页查询文章")
    @GetMapping
    public Result<PageResult<AdminArticlePageQueryVO>> pageQuery(AdminArticlePageQueryDTO adminArticlePageQueryDTO) {
        PageResult<AdminArticlePageQueryVO> pageResult = articleService.pageQuery(adminArticlePageQueryDTO);
        return Result.success(pageResult);
    }

    @ApiOperation("查询所有文章")
    @GetMapping("/all")
    public Result<List<AdminArticleVO>> categoryAll() {
        List<Article> dictList = articleService.list();
        List<AdminArticleVO> adminCategoryVOS = BeanUtil.copyToList(dictList, AdminArticleVO.class);
        return Result.success(adminCategoryVOS);
    }

    @ApiOperation("根据id查询文章")
    @GetMapping("/{id}")
    public Result<AdminArticleVO> getById(@PathVariable Long id) {
        Article category = articleService.getById(id);
        AdminArticleVO adminArticleVO = BeanUtil.copyProperties(category, AdminArticleVO.class);
        return Result.success(adminArticleVO);
    }

    //@ApiOperation("添加文章")
    //@PostMapping
    //public Result<Object> addArticle(@Validated @RequestBody AdminArticleDTO adminArticleDTO) {
    //    articleService.addArticle(adminArticleDTO);
    //    return Result.success();
    //}
    //
    //@ApiOperation("修改文章")
    //@PutMapping
    //public Result<Object> updateArticle(@Validated({Default.class, UpdateValidGroup.class}) @RequestBody AdminArticleDTO adminArticleDTO) {
    //    articleService.updateArticle(adminArticleDTO);
    //    return Result.success();
    //}
    //
    //@ApiOperation("根据id修改文章状态")
    //@PatchMapping("/{id}/status")
    //public Result<Object> updateStatus(@PathVariable Long id) {
    //    articleService.updateStatus(id);
    //    return Result.success();
    //}

    @ApiOperation("根据id删除文章")
    @DeleteMapping("/{id}")
    public Result<Object> deleteById(@PathVariable Long id) {
        articleService.removeById(id);
        return Result.success();
    }

    @ApiOperation("批量删除文章")
    @DeleteMapping
    public Result<Object> deleteByIds(@RequestParam List<Long> ids) {
        // 空集合不处理
        if(ids.isEmpty()) {
            return Result.success();
        }
        articleService.removeBatchByIds(ids);
        return Result.success();
    }
}
