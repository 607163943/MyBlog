package com.site.blog.my.core.controller.user;

import com.site.blog.my.core.pojo.vo.user.BlogHomeVO;
import com.site.blog.my.core.result.Result;
import com.site.blog.my.core.service.BlogService;
import com.site.blog.my.core.service.ConfigService;
import com.site.blog.my.core.service.TagService;
import com.site.blog.my.core.util.PageResult;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "博客首页")
@RestController("user-home-controller")
@RequestMapping("/home")
public class HomeController {
    @Resource
    private BlogService blogService;
    @Resource
    private TagService tagService;
    @Resource
    private ConfigService configService;

    @GetMapping
    public Result<BlogHomeVO> home() {
        PageResult blogPageResult = blogService.getBlogsForIndexPage(1);
        if (blogPageResult == null) {
            return Result.error(404, "页面不存在");
        }

        BlogHomeVO blogHomeVO = BlogHomeVO.builder()
                .blogPageResult(blogPageResult)
                .newBlogs(blogService.getBlogListForIndexPage(1))
                .hotBlogs(blogService.getBlogListForIndexPage(0))
                .hotTags(tagService.getBlogTagCountForIndex())
                .pageName("首页")
                .configurations(configService.getAllConfigs())
                .build();
        return Result.success(blogHomeVO);
    }
}
