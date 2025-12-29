package com.site.blog.my.core.controller.admin;

import com.site.blog.my.core.pojo.vo.ArticleEditVO;
import com.site.blog.my.core.result.Result;
import com.site.blog.my.core.service.CategoryService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "文章管理")
@RestController
@RequestMapping("/admin/article")
public class ArticleController {

    @Resource
    private CategoryService categoryService;
    @GetMapping("/edit")
    public Result<ArticleEditVO> edit() {
        ArticleEditVO articleEditVO = ArticleEditVO.builder()
                .path("edit")
                .categories(categoryService.getAllCategories())
                .build();
        return Result.success(articleEditVO);
    }
}
