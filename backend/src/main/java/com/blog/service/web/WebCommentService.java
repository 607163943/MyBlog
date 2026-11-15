package com.blog.service.web;

import com.blog.pojo.dto.web.WebCommentSubmitDTO;
import com.blog.pojo.vo.web.WebCommentVO;

import java.util.List;

/**
 * 前台评论服务
 */
public interface WebCommentService {

    /**
     * 查询文章下已通过的评论（二级树）
     */
    List<WebCommentVO> listApproved(Integer articleId);

    /**
     * 提交评论（默认待审核）
     */
    void submit(Integer articleId, WebCommentSubmitDTO dto);
}
