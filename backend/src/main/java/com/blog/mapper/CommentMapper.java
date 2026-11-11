package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blog.pojo.po.Comment;
import com.blog.pojo.vo.CommentVO;
import org.apache.ibatis.annotations.Param;

/**
 * 评论 Mapper
 */
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 分页查询评论，复杂查询下沉到 XML 实现
     */
    IPage<CommentVO> selectCommentPage(IPage<CommentVO> page,
                                       @Param("articleTitle") String articleTitle,
                                       @Param("status") Integer status);

    /**
     * 根据 ID 查询评论详情（含所属文章标题）
     */
    CommentVO selectCommentDetailById(@Param("id") Integer id);
}
