package com.my.blog.pojo.vo.user;

import com.my.blog.common.result.Result;
import com.my.blog.common.utils.PageResult;
import com.my.blog.server.service.BlogService;
import com.my.blog.server.service.ConfigService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags =  "分类")
@RestController("user-category-controller")
@RequestMapping("/category")
public class CategoryController {
    @Resource
    private BlogService blogService;
    @Resource
    private ConfigService configService;
    /**
     * 分类列表页
     *
     * @return
     */
    @GetMapping("/{categoryName}")
    public Result<BlogCategoryVO> category(@PathVariable("categoryName") String categoryName) {
        PageResult blogPageResult = blogService.getBlogsPageByCategory(categoryName, 1);
        BlogCategoryVO blogCategoryVO = BlogCategoryVO.builder()
                .blogPageResult(blogPageResult)
                .pageName("分类")
                .pageUrl("category")
                .keyword(categoryName)
                .newBlogs(blogService.getBlogListForIndexPage(1))
                .hotBlogs(blogService.getBlogListForIndexPage(0))
                .configurations(configService.getAllConfigs())
                .build();
        return Result.success(blogCategoryVO);
    }
}
