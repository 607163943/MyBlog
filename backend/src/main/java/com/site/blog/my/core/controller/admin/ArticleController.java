package com.site.blog.my.core.controller.admin;

import com.site.blog.my.core.pojo.vo.ArticleEditVO;
import com.site.blog.my.core.result.Result;
import com.site.blog.my.core.service.BlogService;
import com.site.blog.my.core.service.CategoryService;
import com.site.blog.my.core.util.PageQueryUtil;
import com.site.blog.my.core.util.PageResult;
import io.swagger.annotations.Api;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Api(tags = "文章管理")
@RestController
@RequestMapping("/admin/article")
public class ArticleController {

    @Resource
    private CategoryService categoryService;

    @Resource
    private BlogService blogService;
    @GetMapping("/edit")
    public Result<ArticleEditVO> edit() {
        ArticleEditVO articleEditVO = ArticleEditVO.builder()
                .categories(categoryService.getAllCategories())
                .build();
        return Result.success(articleEditVO);
    }

    @GetMapping("/list")
    @ResponseBody
    public Result<PageResult> list(@RequestParam Map<String, Object> params) {
        if (ObjectUtils.isEmpty(params.get("page")) || ObjectUtils.isEmpty(params.get("limit"))) {
            return Result.error(400,"参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return Result.success(blogService.getBlogsPage(pageUtil));
    }
}
