package com.blog.service.web.impl;

import com.blog.enums.ArticleExceptionEnum;
import com.blog.enums.CommentExceptionEnum;
import com.blog.exception.BusinessException;
import com.blog.mapper.ArticleMapper;
import com.blog.mapper.CommentMapper;
import com.blog.pojo.dto.web.WebCommentSubmitDTO;
import com.blog.pojo.po.Article;
import com.blog.pojo.po.Comment;
import com.blog.pojo.vo.web.WebCommentVO;
import com.blog.service.web.WebCommentService;
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
            throw new BusinessException(ArticleExceptionEnum.ARTICLE_NOT_FOUND);
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
            throw new BusinessException(ArticleExceptionEnum.ARTICLE_NOT_FOUND);
        }
        Article article = articleMapper.selectById(articleId);
        if (article == null || article.getStatus() == null
                || article.getStatus() != STATUS_PUBLISHED) {
            throw new BusinessException(ArticleExceptionEnum.ARTICLE_NOT_PUBLISHED);
        }
        if (dto == null) {
            throw new BusinessException(CommentExceptionEnum.COMMENT_CONTENT_REQUIRED);
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
                throw new BusinessException(CommentExceptionEnum.COMMENT_REPLY_TARGET_INVALID);
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
            throw new BusinessException(CommentExceptionEnum.COMMENT_NICKNAME_REQUIRED);
        }
        if (trimmed.length() > MAX_NICKNAME_LENGTH) {
            throw new BusinessException(CommentExceptionEnum.COMMENT_NICKNAME_TOO_LONG);
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
            throw new BusinessException(CommentExceptionEnum.COMMENT_EMAIL_TOO_LONG);
        }
        if (!EMAIL_PATTERN.matcher(trimmed).matches()) {
            throw new BusinessException(CommentExceptionEnum.COMMENT_EMAIL_INVALID);
        }
        return trimmed;
    }

    /**
     * 校验评论内容
     */
    private String normalizeContent(String content) {
        String trimmed = content == null ? "" : content.trim();
        if (trimmed.isEmpty()) {
            throw new BusinessException(CommentExceptionEnum.COMMENT_CONTENT_REQUIRED);
        }
        if (trimmed.length() > MAX_CONTENT_LENGTH) {
            throw new BusinessException(CommentExceptionEnum.COMMENT_CONTENT_TOO_LONG);
        }
        return trimmed;
    }
}
