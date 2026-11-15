package com.blog.controller.web;

import com.blog.common.Result;
import com.blog.pojo.dto.web.WebCommentSubmitDTO;
import com.blog.pojo.vo.web.WebCommentVO;
import com.blog.service.WebCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前台评论接口
 */
@RestController
@RequestMapping("/web/article")
@RequiredArgsConstructor
public class WebCommentController {

    private final WebCommentService webCommentService;

    /**
     * 查询文章下已通过的评论
     */
    @GetMapping("/{articleId}/comments")
    public Result<List<WebCommentVO>> list(@PathVariable Integer articleId) {
        return Result.success(webCommentService.listApproved(articleId));
    }

    /**
     * 提交评论（默认待审核）
     */
    @PostMapping("/{articleId}/comments")
    public Result<Void> submit(@PathVariable Integer articleId,
                               @RequestBody WebCommentSubmitDTO dto) {
        webCommentService.submit(articleId, dto);
        return Result.success();
    }
}
