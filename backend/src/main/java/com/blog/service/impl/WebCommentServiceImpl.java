package com.blog.service.impl;

import com.blog.exception.BusinessException;
import com.blog.mapper.ArticleMapper;
import com.blog.mapper.CommentMapper;
import com.blog.pojo.dto.web.WebCommentSubmitDTO;
import com.blog.pojo.po.Article;
import com.blog.pojo.po.Comment;
import com.blog.pojo.vo.web.WebCommentVO;
import com.blog.service.WebCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 前台评论服务实现
 */
@Service
@RequiredArgsConstructor
public class WebCommentServiceImpl implements WebCommentService {

    private static final int NO_PARENT_ID = 0;
    private static final int STATUS_PENDING = 0;
    private static final int STATUS_APPROVED = 1;
    private static final int STATUS_PUBLISHED = 1;
    private static final int MAX_NICKNAME_LENGTH = 50;
    private static final int MAX_EMAIL_LENGTH = 100;
    private static final int MAX_CONTENT_LENGTH = 500;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private final CommentMapper commentMapper;

    private final ArticleMapper articleMapper;

    @Override
    public List<WebCommentVO> listApproved(Integer articleId) {
        if (articleId == null) {
            throw new BusinessException("文章不存在");
        }
        List<WebCommentVO> comments = commentMapper.selectApprovedCommentsByArticleId(articleId);
        Map<Integer, WebCommentVO> commentMap = new LinkedHashMap<>();
        for (WebCommentVO comment : comments) {
            comment.setChildren(new ArrayList<>());
            commentMap.put(comment.getId(), comment);
        }
        List<WebCommentVO> roots = new ArrayList<>();
        for (WebCommentVO comment : comments) {
            if (comment.getParentId() == null || comment.getParentId() == NO_PARENT_ID) {
                roots.add(comment);
                continue;
            }
            WebCommentVO parent = commentMap.get(comment.getParentId());
            if (parent == null) {
                continue;
            }
            if (parent.getParentId() != null && parent.getParentId() != NO_PARENT_ID) {
                WebCommentVO root = commentMap.get(parent.getParentId());
                if (root != null) {
                    root.getChildren().add(comment);
                }
                continue;
            }
            parent.getChildren().add(comment);
        }
        return roots;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submit(Integer articleId, WebCommentSubmitDTO dto) {
        if (articleId == null) {
            throw new BusinessException("文章不存在");
        }
        Article article = articleMapper.selectById(articleId);
        if (article == null || article.getStatus() == null
                || article.getStatus() != STATUS_PUBLISHED) {
            throw new BusinessException("文章不存在或未发布");
        }
        if (dto == null) {
            throw new BusinessException("评论内容不能为空");
        }

        String nickname = normalizeNickname(dto.getNickname());
        String email = normalizeEmail(dto.getEmail());
        String content = normalizeContent(dto.getContent());
        int replyToId = dto.getReplyToId() == null ? 0 : dto.getReplyToId();
        int parentId = NO_PARENT_ID;
        if (replyToId > 0) {
            Comment target = commentMapper.selectById(replyToId);
            if (target == null
                    || !articleId.equals(target.getArticleId())
                    || target.getStatus() == null
                    || target.getStatus() != STATUS_APPROVED) {
                throw new BusinessException("回复的评论不存在或未通过审核");
            }
            parentId = target.getParentId() == null || target.getParentId() == NO_PARENT_ID
                    ? target.getId()
                    : target.getParentId();
        }

        Comment comment = new Comment();
        comment.setArticleId(articleId);
        comment.setParentId(parentId);
        comment.setReplyToId(replyToId);
        comment.setNickname(nickname);
        comment.setEmail(email);
        comment.setContent(content);
        comment.setStatus(STATUS_PENDING);
        commentMapper.insert(comment);
    }

    /**
     * 校验评论者昵称
     */
    private String normalizeNickname(String nickname) {
        String trimmed = nickname == null ? "" : nickname.trim();
        if (trimmed.isEmpty()) {
            throw new BusinessException("评论者昵称不能为空");
        }
        if (trimmed.length() > MAX_NICKNAME_LENGTH) {
            throw new BusinessException("评论者昵称不能超过 50 个字符");
        }
        return trimmed;
    }

    /**
     * 校验评论者邮箱，空值转 null
     */
    private String normalizeEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        String trimmed = email.trim();
        if (trimmed.length() > MAX_EMAIL_LENGTH) {
            throw new BusinessException("邮箱不能超过 100 个字符");
        }
        if (!EMAIL_PATTERN.matcher(trimmed).matches()) {
            throw new BusinessException("邮箱格式不正确");
        }
        return trimmed;
    }

    /**
     * 校验评论内容
     */
    private String normalizeContent(String content) {
        String trimmed = content == null ? "" : content.trim();
        if (trimmed.isEmpty()) {
            throw new BusinessException("评论内容不能为空");
        }
        if (trimmed.length() > MAX_CONTENT_LENGTH) {
            throw new BusinessException("评论内容不能超过 500 个字符");
        }
        return trimmed;
    }
}
