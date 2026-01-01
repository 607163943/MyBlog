package com.site.blog.my.core.controller.admin;

import com.site.blog.my.core.pojo.vo.HomeTotalVO;
import com.site.blog.my.core.common.result.Result;
import com.site.blog.my.core.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "后台首页")
@RestController
@RequestMapping("/admin/home")
public class HomeController {
    @Resource
    private BlogService blogService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private LinkService linkService;
    @Resource
    private TagService tagService;
    @Resource
    private CommentService commentService;

    @ApiOperation("获取首页统计数据")
    @GetMapping("/total")
    public Result<HomeTotalVO> adminHomeTotal() {
        HomeTotalVO homeTotalVO = HomeTotalVO.builder()
                .categoryCount(categoryService.getTotalCategories())
                .blogCount(blogService.getTotalBlogs())
                .linkCount(linkService.getTotalLinks())
                .tagCount(tagService.getTotalTags())
                .commentCount(commentService.getTotalComments())
                .build();
        return Result.success(homeTotalVO);
    }
}
