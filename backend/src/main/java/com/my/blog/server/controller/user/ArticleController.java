package com.my.blog.server.controller.user;

import com.my.blog.common.result.Result;
import com.my.blog.pojo.vo.BlogDetailVO;
import com.my.blog.pojo.vo.user.ArticleDetailVO;
import com.my.blog.server.service.BlogService;
import com.my.blog.server.service.ConfigService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController("user-article-controller")
@RequestMapping("/article")
public class ArticleController {
    public static String theme = "amaze";
    @Resource
    private BlogService blogService;
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
        }

        return Result.success(articleDetailVO);
    }
}
