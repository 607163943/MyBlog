package com.site.blog.my.core.controller.user;

import cn.hutool.captcha.ShearCaptcha;
import com.site.blog.my.core.entity.BlogComment;
import com.site.blog.my.core.common.result.Result;
import com.site.blog.my.core.service.CommentService;
import com.site.blog.my.core.common.utils.MyBlogUtils;
import com.site.blog.my.core.common.utils.PatternUtil;
import io.swagger.annotations.Api;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Api(tags = "评论")
@RestController
@RequestMapping("/comment")
public class CommonController {
    public static String theme = "amaze";
    @Resource
    private CommentService commentService;
    /**
     * 评论操作
     */
    @PostMapping
    @ResponseBody
    public Result<Boolean> comment(HttpServletRequest request, HttpSession session,
                          @RequestParam Long blogId, @RequestParam String verifyCode,
                          @RequestParam String commentator, @RequestParam String email,
                          @RequestParam String websiteUrl, @RequestParam String commentBody) {
        if (!StringUtils.hasText(verifyCode)) {
            return Result.error(400,"验证码不能为空");
        }
        ShearCaptcha shearCaptcha = (ShearCaptcha) session.getAttribute("verifyCode");
        if (shearCaptcha == null || !shearCaptcha.verify(verifyCode)) {
            return Result.error(400,"验证码错误");
        }
        String ref = request.getHeader("Referer");
        if (!StringUtils.hasText(ref)) {
            return Result.error(400,"非法请求");
        }
        if (null == blogId || blogId < 0) {
            return Result.error(400,"非法请求");
        }
        if (!StringUtils.hasText(commentator)) {
            return Result.error(400,"请输入称呼");
        }
        if (!StringUtils.hasText(email)) {
            return Result.error(400,"请输入邮箱地址");
        }
        if (!PatternUtil.isEmail(email)) {
            return Result.error(400,"请输入正确的邮箱地址");
        }
        if (!StringUtils.hasText(commentBody)) {
            return Result.error(400,"请输入评论内容");
        }
        if (commentBody.trim().length() > 200) {
            return Result.error(400,"评论内容过长");
        }
        BlogComment comment = new BlogComment();
        comment.setBlogId(blogId);
        comment.setCommentator(MyBlogUtils.cleanString(commentator));
        comment.setEmail(email);
        if (PatternUtil.isURL(websiteUrl)) {
            comment.setWebsiteUrl(websiteUrl);
        }
        comment.setCommentBody(MyBlogUtils.cleanString(commentBody));
        return Result.success(commentService.addComment(comment));
    }
}
