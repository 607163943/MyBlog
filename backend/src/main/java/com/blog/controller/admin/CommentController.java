package com.blog.controller.admin;

import com.blog.annotation.OperationLog;
import com.blog.common.Result;
import com.blog.pojo.dto.admin.CommentDTO;
import com.blog.pojo.dto.admin.CommentPageQueryDTO;
import com.blog.pojo.vo.admin.CommentVO;
import com.blog.pojo.vo.PageVO;
import com.blog.service.admin.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 评论管理接口
 */
@RestController
@RequestMapping("/admin/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 分页查询评论
     */
    @GetMapping("/page")
    public Result<PageVO<CommentVO>> page(CommentPageQueryDTO dto) {
        return Result.success(commentService.pageQuery(dto));
    }

    /**
     * 根据 ID 查询评论
     */
    @GetMapping("/{id}")
    public Result<CommentVO> getById(@PathVariable Integer id) {
        return Result.success(commentService.getCommentById(id));
    }

    /**
     * 修改评论
     */
    @OperationLog(module = "comment", action = "修改评论", key = "#a0.id")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody CommentDTO dto) {
        commentService.updateComment(dto);
        return Result.success();
    }

    /**
     * 删除单个评论
     */
    @OperationLog(module = "comment", action = "删除评论", key = "#a0")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        commentService.deleteComment(id);
        return Result.success();
    }

    /**
     * 批量删除评论
     */
    @OperationLog(module = "comment", action = "批量删除评论", key = "#a0")
    @DeleteMapping("/batch")
    public Result<Void> deleteBatch(@RequestBody List<Integer> ids) {
        commentService.deleteComments(ids);
        return Result.success();
    }
}
