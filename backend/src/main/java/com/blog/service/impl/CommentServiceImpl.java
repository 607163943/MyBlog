package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.exception.BusinessException;
import com.blog.mapper.CommentMapper;
import com.blog.pojo.dto.CommentDTO;
import com.blog.pojo.dto.CommentPageQueryDTO;
import com.blog.pojo.po.Comment;
import com.blog.pojo.vo.CommentVO;
import com.blog.pojo.vo.PageVO;
import com.blog.service.CommentService;
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

    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;

    private static final int NO_PARENT_ID = 0;

    private static final int STATUS_PENDING = 0;
    private static final int STATUS_APPROVED = 1;

    @Override
    public PageVO<CommentVO> pageQuery(CommentPageQueryDTO dto) {
        int pageNum = dto.getPage() == null || dto.getPage() < 1 ? DEFAULT_PAGE_NUM : dto.getPage();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? DEFAULT_PAGE_SIZE : dto.getPageSize();

        Page<CommentVO> page = new Page<>(pageNum, pageSize);
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
            throw new BusinessException("评论不存在");
        }
        CommentVO commentVO = baseMapper.selectCommentDetailById(id);
        if (commentVO == null) {
            throw new BusinessException("评论不存在");
        }
        return commentVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateComment(CommentDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("评论ID不能为空");
        }
        Comment existing = getById(dto.getId());
        if (existing == null) {
            throw new BusinessException("评论不存在");
        }

        String nickname = normalizeNickname(dto.getNickname());
        String email = normalizeEmail(dto.getEmail());
        String content = normalizeContent(dto.getContent());
        int status = dto.getStatus() == null
                ? existing.getStatus()
                : normalizeStatus(dto.getStatus());

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
            throw new BusinessException("评论不存在");
        }
        removeCommentsWithCascade(Collections.singletonList(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComments(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("请选择要删除的评论");
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
     * 去除昵称首尾空格并校验非空与长度
     */
    private String normalizeNickname(String nickname) {
        String trimmed = nickname == null ? "" : nickname.trim();
        if (trimmed.isEmpty()) {
            throw new BusinessException("评论者昵称不能为空");
        }
        if (trimmed.length() > 50) {
            throw new BusinessException("评论者昵称不能超过 50 个字符");
        }
        return trimmed;
    }

    /**
     * 邮箱去除首尾空格，空值保留为 null
     */
    private String normalizeEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        String trimmed = email.trim();
        if (trimmed.length() > 100) {
            throw new BusinessException("邮箱不能超过 100 个字符");
        }
        return trimmed;
    }

    /**
     * 校验评论内容非空
     */
    private String normalizeContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new BusinessException("评论内容不能为空");
        }
        return content;
    }

    /**
     * 校验审核状态取值
     */
    private int normalizeStatus(Integer status) {
        if (status != STATUS_PENDING && status != STATUS_APPROVED) {
            throw new BusinessException("审核状态不正确");
        }
        return status;
    }
}
