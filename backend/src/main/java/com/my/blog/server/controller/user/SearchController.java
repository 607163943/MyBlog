package com.my.blog.server.controller.user;

import com.my.blog.common.result.Result;
import com.my.blog.common.utils.PageResult;
import com.my.blog.pojo.vo.user.BlogSearchVO;
import com.my.blog.server.service.BlogService;
import com.my.blog.server.service.ConfigService;
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
                .configurations(configService.getAllConfigs())
                .build();
        return Result.success(blogSearchVO);
    }
}
