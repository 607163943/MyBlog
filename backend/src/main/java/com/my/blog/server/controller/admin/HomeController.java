package com.my.blog.server.controller.admin;

import com.my.blog.pojo.vo.HomeTotalVO;
import com.my.blog.common.result.Result;
import com.my.blog.server.service.*;
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
    private TagService tagService;

    @ApiOperation("获取首页统计数据")
    @GetMapping("/total")
    public Result<HomeTotalVO> adminHomeTotal() {
        HomeTotalVO homeTotalVO = HomeTotalVO.builder()
                .categoryCount(categoryService.getTotalCategories())
                .blogCount(blogService.getTotalBlogs())
                .tagCount(tagService.getTotalTags())
                .build();
        return Result.success(homeTotalVO);
    }
}
