package com.site.blog.my.core.controller.user;

import com.site.blog.my.core.pojo.vo.BlogDetailVO;
import com.site.blog.my.core.pojo.vo.user.ArticleDetailVO;
import com.site.blog.my.core.result.Result;
import com.site.blog.my.core.service.BlogService;
import com.site.blog.my.core.service.CommentService;
import com.site.blog.my.core.service.ConfigService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController("user-article-controller")
@RequestMapping("/article")
public class ArticleController {
    public static String theme = "amaze";
    @Resource
    private BlogService blogService;
    @Resource
    private CommentService commentService;
    @Resource
    private ConfigService configService;
    /**
     * 详情页
     *
     * @return
     */
    @GetMapping("/{articleId}")
    public Result<ArticleDetailVO> detail(@PathVariable("articleId") Long blogId, @RequestParam(value = "commentPage", required = false, defaultValue = "1") Integer commentPage) {
        ArticleDetailVO articleDetailVO = ArticleDetailVO.builder()
                .pageName("详情")
                .configurations(configService.getAllConfigs())
                .build();

        BlogDetailVO blogDetailVO = blogService.getBlogDetail(blogId);
        if (blogDetailVO != null) {
            articleDetailVO.setBlogDetailVO(blogDetailVO);
            articleDetailVO.setCommentPageResult(commentService.getCommentPageByBlogIdAndPageNum(blogId, commentPage));
        }

        return Result.success(articleDetailVO);
    }
}
