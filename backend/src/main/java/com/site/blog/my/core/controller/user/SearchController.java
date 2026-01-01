package com.site.blog.my.core.controller.user;

import com.site.blog.my.core.pojo.vo.user.BlogSearchVO;
import com.site.blog.my.core.common.result.Result;
import com.site.blog.my.core.service.BlogService;
import com.site.blog.my.core.service.ConfigService;
import com.site.blog.my.core.service.TagService;
import com.site.blog.my.core.common.utils.PageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/search")
public class SearchController {
    public static String theme = "amaze";
    @Resource
    private BlogService blogService;
    @Resource
    private TagService tagService;
    @Resource
    private ConfigService configService;

    /**
     * 搜索列表页
     *
     * @return
     */
    @GetMapping({"/{keyword}"})
    public Result<BlogSearchVO> search(@PathVariable("keyword") String keyword) {
        PageResult blogPageResult = blogService.getBlogsPageBySearch(keyword, 1);
        BlogSearchVO blogSearchVO = BlogSearchVO.builder()
                .blogPageResult(blogPageResult)
                .pageName("搜索")
                .pageUrl("search")
                .keyword(keyword)
                .newBlogs(blogService.getBlogListForIndexPage(1))
                .hotBlogs(blogService.getBlogListForIndexPage(0))
                .hotTags(tagService.getBlogTagCountForIndex())
                .configurations(configService.getAllConfigs())
                .build();
        return Result.success(blogSearchVO);
    }
}
