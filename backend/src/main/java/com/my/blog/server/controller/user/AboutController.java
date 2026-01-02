package com.my.blog.server.controller.user;

import com.my.blog.pojo.vo.BlogDetailVO;
import com.my.blog.pojo.vo.user.BlogAboutVO;
import com.my.blog.common.result.Result;
import com.my.blog.server.service.BlogService;
import com.my.blog.server.service.ConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/about")
public class AboutController {
    public static String theme = "amaze";
    @Resource
    private BlogService blogService;
    @Resource
    private ConfigService configService;

    /**
     * 关于页面 以及其他配置了subUrl的文章页
     *
     * @return
     */
    @GetMapping
    public Result<BlogAboutVO> detail() {
        // TODO：这里原本是查询特殊文章，但特殊文章只有关于一篇，之后会修改成关于页面并删除查询其他特殊文章的功能
        BlogDetailVO blogDetailVO = blogService.getBlogDetailBySubUrl("about");
        if (blogDetailVO != null) {
            BlogAboutVO blogAboutVO = BlogAboutVO.builder()
                    .pageName("关于")
                    .blogDetailVO(blogDetailVO)
                    .configurations(configService.getAllConfigs())
                    .build();
            return Result.success(blogAboutVO);
        } else {
            return Result.error(400, "文章不存在");
        }
    }
}
