package com.my.blog.server.controller.user;

import com.my.blog.common.result.Result;
import com.my.blog.common.utils.PageResult;
import com.my.blog.pojo.vo.user.BlogTagVO;
import com.my.blog.server.service.BlogService;
import com.my.blog.server.service.ConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("user-tag-controller")
@RequestMapping("/tag")
public class TagController {
    @Resource
    private BlogService blogService;
    @Resource
    private ConfigService configService;
    /**
     * 标签列表页
     *
     * @return
     */
    @GetMapping({"/{tagName}"})
    public Result<BlogTagVO> tag(@PathVariable("tagName") String tagName) {
        // TODO：这里需要修改分页查询逻辑，暂时写死
        PageResult blogPageResult = blogService.getBlogsPageByTag(tagName, 1);
        BlogTagVO blogTagVO = BlogTagVO.builder()
                .blogPageResult(blogPageResult)
                .newBlogs(blogService.getBlogListForIndexPage(1))
                .hotBlogs(blogService.getBlogListForIndexPage(0))
                .pageName("标签")
                .pageUrl("tag")
                .keyword(tagName)
                .configurations(configService.getAllConfigs())
                .build();
        return Result.success(blogTagVO);
    }
}
