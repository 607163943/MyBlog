package com.site.blog.my.core.controller.user;

import com.site.blog.my.core.pojo.vo.user.BlogTagVO;
import com.site.blog.my.core.result.Result;
import com.site.blog.my.core.service.BlogService;
import com.site.blog.my.core.service.ConfigService;
import com.site.blog.my.core.service.TagService;
import com.site.blog.my.core.util.PageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("user-tag-controller")
@RequestMapping("/tag")
public class TagController {
    public static String theme = "amaze";
    @Resource
    private BlogService blogService;
    @Resource
    private TagService tagService;
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
                .hotTags(tagService.getBlogTagCountForIndex())
                .pageName("标签")
                .pageUrl("tag")
                .keyword(tagName)
                .configurations(configService.getAllConfigs())
                .build();
        return Result.success(blogTagVO);
    }
}
