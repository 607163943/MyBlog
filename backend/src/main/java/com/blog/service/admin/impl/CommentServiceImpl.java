package com.blog.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.enums.CommentExceptionEnum;
import com.blog.exception.BusinessException;
import com.blog.mapper.CommentMapper;
import com.blog.pojo.dto.admin.CommentDTO;
import com.blog.pojo.dto.admin.CommentPageQueryDTO;
import com.blog.pojo.dto.PageQueryDTO;
import com.blog.pojo.po.Comment;
import com.blog.pojo.vo.admin.CommentVO;
import com.blog.pojo.vo.PageVO;
import com.blog.service.admin.CommentService;
import com.blog.util.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 评论服务实现
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private static final int NO_PARENT_ID = 0;

    @Override
    public PageVO<CommentVO> pageQuery(CommentPageQueryDTO dto) {
        PageQueryDTO query = PageUtil.checkPage(dto);

        Page<CommentVO> page = new Page<>(query.getPage(), query.getPageSize());
        IPage<CommentVO> pageResult = baseMapper.selectCommentPage(page, dto.getArticleTitle(), dto.getStatus());

        PageVO<CommentVO> pageVO = new PageVO<>();
        pageVO.setRecords(pageResult.getRecords());
        pageVO.setTotal(pageResult.getTotal());
        pageVO.setSize(pageResult.getSize());
        pageVO.setPages(pageResult.getPages());
        return pageVO;
    }

    @Override
    public CommentVO getCommentById(Integer id) {
        if (id == null) {
            throw new BusinessException(CommentExceptionEnum.COMMENT_NOT_FOUND);
        }
        CommentVO commentVO = baseMapper.selectCommentDetailById(id);
        if (commentVO == null) {
            throw new BusinessException(CommentExceptionEnum.COMMENT_NOT_FOUND);
        }
        return commentVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateComment(CommentDTO dto) {
        Comment existing = getById(dto.getId());
        if (existing == null) {
            throw new BusinessException(CommentExceptionEnum.COMMENT_NOT_FOUND);
        }

        String nickname = normalizeNickname(dto.getNickname());
        String email = normalizeEmail(dto.getEmail());
        String content = dto.getContent();
        int status = dto.getStatus() == null ? existing.getStatus() : dto.getStatus();

        LambdaUpdateWrapper<Comment> updateWrapper = new LambdaUpdateWrapper<Comment>()
                .eq(Comment::getId, dto.getId())
                .set(Comment::getNickname, nickname)
                .set(Comment::getEmail, email)
                .set(Comment::getContent, content)
                .set(Comment::getStatus, status);
        baseMapper.update(null, updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Integer id) {
        if (id == null || getById(id) == null) {
            throw new BusinessException(CommentExceptionEnum.COMMENT_NOT_FOUND);
        }
        removeCommentsWithCascade(Collections.singletonList(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComments(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(CommentExceptionEnum.COMMENT_DELETE_IDS_REQUIRED);
        }
        removeCommentsWithCascade(ids);
    }

    /**
     * 删除评论：一级评论连同其下的二级回复一并删除，并清理指向被删评论的回复关系
     */
    private void removeCommentsWithCascade(List<Integer> ids) {
        List<Comment> targets = listByIds(ids);
        if (targets.isEmpty()) {
            return;
        }

        List<Integer> deleteIds = new ArrayList<>(targets.stream()
                .map(Comment::getId)
                .collect(Collectors.toList()));

        List<Integer> parentIds = targets.stream()
                .filter(item -> Objects.equals(item.getParentId(), NO_PARENT_ID))
                .map(Comment::getId)
                .collect(Collectors.toList());
        if (!parentIds.isEmpty()) {
            List<Integer> replyIds = list(new LambdaQueryWrapper<Comment>()
                    .select(Comment::getId)
                    .in(Comment::getParentId, parentIds))
                    .stream()
                    .map(Comment::getId)
                    .collect(Collectors.toList());
            deleteIds.addAll(replyIds);
        }

        deleteIds = deleteIds.stream().distinct().collect(Collectors.toList());

        LambdaUpdateWrapper<Comment> clearReplyWrapper = new LambdaUpdateWrapper<Comment>()
                .in(Comment::getReplyToId, deleteIds)
                .set(Comment::getReplyToId, NO_PARENT_ID);
        update(clearReplyWrapper);
        removeByIds(deleteIds);
    }

    /**
     * 去除昵称首尾空格
     */
    private String normalizeNickname(String nickname) {
        return nickname.trim();
    }

    /**
     * 邮箱去除首尾空格，空值保留为 null
     */
    private String normalizeEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        return email.trim();
    }
}
