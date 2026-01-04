package com.my.blog.server.controller.user;

import com.my.blog.common.result.Result;
import com.my.blog.common.utils.PageResult;
import com.my.blog.pojo.vo.user.BlogHomeVO;
import com.my.blog.server.service.BlogService;
import com.my.blog.server.service.ConfigService;
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
    private ConfigService configService;

    @GetMapping
    public Result<BlogHomeVO> home() {
        // TODO：这里需要修改分页查询逻辑，暂时写死
        PageResult blogPageResult = blogService.getBlogsForIndexPage(1);
        if (blogPageResult == null) {
            return Result.error(404, "页面不存在");
        }

        BlogHomeVO blogHomeVO = BlogHomeVO.builder()
                .blogPageResult(blogPageResult)
                .newBlogs(blogService.getBlogListForIndexPage(1))
                .hotBlogs(blogService.getBlogListForIndexPage(0))
                .pageName("首页")
                .configurations(configService.getAllConfigs())
                .build();
        return Result.success(blogHomeVO);
    }
}
