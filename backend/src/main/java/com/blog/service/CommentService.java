package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.pojo.dto.CommentDTO;
import com.blog.pojo.dto.CommentPageQueryDTO;
import com.blog.pojo.po.Comment;
import com.blog.pojo.vo.CommentVO;
import com.blog.pojo.vo.PageVO;

import java.util.List;

/**
 * 评论服务
 */
public interface CommentService extends IService<Comment> {

    /**
     * 分页查询评论
     */
    PageVO<CommentVO> pageQuery(CommentPageQueryDTO dto);

    /**
     * 根据 ID 查询评论
     */
    CommentVO getCommentById(Integer id);

    /**
     * 修改评论
     */
    void updateComment(CommentDTO dto);

    /**
     * 删除单个评论，一级评论连同其下回复一并删除
     */
    void deleteComment(Integer id);

    /**
     * 批量删除评论，一级评论连同其下回复一并删除
     */
    void deleteComments(List<Integer> ids);
}
