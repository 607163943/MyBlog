package com.blog.service.admin.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.enums.ArticleExceptionEnum;
import com.blog.enums.CategoryExceptionEnum;
import com.blog.enums.TagExceptionEnum;
import com.blog.exception.BusinessException;
import com.blog.mapper.ArticleMapper;
import com.blog.mapper.ArticleTagMapper;
import com.blog.mapper.CategoryMapper;
import com.blog.mapper.TagMapper;
import com.blog.pojo.dto.admin.ArticleDTO;
import com.blog.pojo.dto.admin.ArticlePageQueryDTO;
import com.blog.pojo.dto.PageQueryDTO;
import com.blog.pojo.po.Article;
import com.blog.pojo.po.ArticleTag;
import com.blog.pojo.vo.admin.ArticleVO;
import com.blog.pojo.vo.PageVO;
import com.blog.service.admin.ArticleService;
import com.blog.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文章服务实现
 */
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private static final int STATUS_DRAFT = 0;
    private static final int STATUS_PUBLISHED = 1;

    private final CategoryMapper categoryMapper;

    private final TagMapper tagMapper;

    private final ArticleTagMapper articleTagMapper;

    @Override
    public PageVO<ArticleVO> pageQuery(ArticlePageQueryDTO dto) {
        PageQueryDTO query = PageUtil.checkPage(dto);

        Page<ArticleVO> page = new Page<>(query.getPage(), query.getPageSize());
        IPage<ArticleVO> pageResult = baseMapper.selectArticlePage(
                page, dto.getTitle(), dto.getStatus(), dto.getCategoryId());

        PageVO<ArticleVO> pageVO = new PageVO<>();
        pageVO.setRecords(pageResult.getRecords());
        pageVO.setTotal(pageResult.getTotal());
        pageVO.setSize(pageResult.getSize());
        pageVO.setPages(pageResult.getPages());
        return pageVO;
    }

    @Override
    public ArticleVO getArticleById(Integer id) {
        if (id == null) {
            throw new BusinessException(ArticleExceptionEnum.ARTICLE_NOT_FOUND);
        }
        ArticleVO articleVO = baseMapper.selectArticleDetailById(id);
        if (articleVO == null) {
            throw new BusinessException(ArticleExceptionEnum.ARTICLE_NOT_FOUND);
        }
        articleVO.setTagIds(listTagIds(id));
        return articleVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addArticle(ArticleDTO dto) {
        String title = normalizeTitle(dto.getTitle());
        String content = normalizeContent(dto.getContent());
        Integer categoryId = requireCategory(dto.getCategoryId());
        int status = normalizeStatus(dto.getStatus() == null ? STATUS_DRAFT : dto.getStatus());

        Article article = new Article();
        article.setTitle(title);
        article.setDescription(normalizeOptionalText(dto.getDescription()));
        article.setContent(content);
        article.setCover(normalizeOptionalText(dto.getCover()));
        article.setCategoryId(categoryId);
        article.setAuthorId(StpUtil.getLoginIdAsInt());
        article.setStatus(status);
        article.setViewCount(0);

        // 后台可手动指定分类内排序值；未填写时已发布文章自动追加到分类末尾
        Integer categorySort = dto.getCategorySort();
        if (categorySort != null) {
            validateCategorySort(categoryId, categorySort, null);
        } else if (status == STATUS_PUBLISHED) {
            categorySort = nextCategorySort(categoryId);
        }
        article.setCategorySort(categorySort);
        if (status == STATUS_PUBLISHED) {
            article.setPublishTime(LocalDateTime.now());
        }
        save(article);
        saveArticleTags(article.getId(), dto.getTagIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(ArticleDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException(ArticleExceptionEnum.ARTICLE_ID_REQUIRED);
        }
        Article existing = getById(dto.getId());
        if (existing == null) {
            throw new BusinessException(ArticleExceptionEnum.ARTICLE_NOT_FOUND);
        }

        String title = normalizeTitle(dto.getTitle());
        String content = normalizeContent(dto.getContent());
        Integer categoryId = dto.getCategoryId() == null ? existing.getCategoryId() : dto.getCategoryId();
        requireCategory(categoryId);
        int status = dto.getStatus() == null
                ? existing.getStatus()
                : normalizeStatus(dto.getStatus());

        // 首次发布时写入发布时间，之后编辑、重新发布均不再变更
        LocalDateTime publishTime = existing.getPublishTime();
        if (publishTime == null && status == STATUS_PUBLISHED) {
            publishTime = LocalDateTime.now();
        }

        // 后台可手动修改分类内排序值；未填写时沿用原有自动规则：
        // 换分类后已发布文章重新追加到新分类末尾，已发布但未编号的补到分类末尾
        Integer categorySort = dto.getCategorySort();
        boolean categoryChanged = !Objects.equals(existing.getCategoryId(), categoryId);
        if (categorySort != null) {
            validateCategorySort(categoryId, categorySort, dto.getId());
        } else {
            categorySort = existing.getCategorySort();
            if (categoryChanged) {
                categorySort = status == STATUS_PUBLISHED ? nextCategorySort(categoryId) : null;
            } else if (status == STATUS_PUBLISHED && categorySort == null) {
                categorySort = nextCategorySort(categoryId);
            }
        }

        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<Article>()
                .eq(Article::getId, dto.getId())
                .set(Article::getTitle, title)
                .set(Article::getDescription, normalizeOptionalText(dto.getDescription()))
                .set(Article::getContent, content)
                .set(Article::getCover, normalizeOptionalText(dto.getCover()))
                .set(Article::getCategoryId, categoryId)
                .set(Article::getCategorySort, categorySort)
                .set(Article::getStatus, status)
                .set(Article::getPublishTime, publishTime);
        baseMapper.update(null, updateWrapper);

        if (dto.getTagIds() != null) {
            replaceArticleTags(dto.getId(), dto.getTagIds());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticle(Integer id) {
        if (id == null || !removeById(id)) {
            throw new BusinessException(ArticleExceptionEnum.ARTICLE_NOT_FOUND);
        }
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getArticleId, id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticles(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(ArticleExceptionEnum.ARTICLE_DELETE_IDS_REQUIRED);
        }
        removeByIds(ids);
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getArticleId, ids));
    }

    /**
     * 取分类内下一个排序值：当前最大值 + 1，空分类从 1 开始
     */
    private Integer nextCategorySort(Integer categoryId) {
        Integer maxSort = baseMapper.selectMaxCategorySort(categoryId);
        return (maxSort == null ? 0 : maxSort) + 1;
    }

    /**
     * 校验手动指定的分类内排序值：必须为正整数且同分类内唯一
     */
    private void validateCategorySort(Integer categoryId, Integer categorySort, Integer excludeId) {
        if (categorySort <= 0) {
            throw new BusinessException(ArticleExceptionEnum.ARTICLE_CATEGORY_SORT_INVALID);
        }
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getCategoryId, categoryId)
                .eq(Article::getCategorySort, categorySort)
                .ne(excludeId != null, Article::getId, excludeId));
        if (count != null && count > 0) {
            throw new BusinessException(ArticleExceptionEnum.ARTICLE_CATEGORY_SORT_DUPLICATED);
        }
    }

    /**
     * 查询文章关联的标签 ID 列表
     */
    private List<Integer> listTagIds(Integer articleId) {
        return articleTagMapper.selectList(new LambdaQueryWrapper<ArticleTag>()
                        .eq(ArticleTag::getArticleId, articleId)
                        .orderByAsc(ArticleTag::getTagId))
                .stream()
                .map(ArticleTag::getTagId)
                .collect(Collectors.toList());
    }

    /**
     * 保存文章标签关联
     */
    private void saveArticleTags(Integer articleId, List<Integer> tagIds) {
        for (Integer tagId : normalizeTagIds(tagIds)) {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticleId(articleId);
            articleTag.setTagId(tagId);
            articleTagMapper.insert(articleTag);
        }
    }

    /**
     * 覆盖文章标签关联：先删除旧关联，再保存新关联
     */
    private void replaceArticleTags(Integer articleId, List<Integer> tagIds) {
        List<Integer> normalizedTagIds = normalizeTagIds(tagIds);
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getArticleId, articleId));
        saveArticleTags(articleId, normalizedTagIds);
    }

    /**
     * 去重并校验标签 ID 是否存在
     */
    private List<Integer> normalizeTagIds(List<Integer> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<Integer> normalizedTagIds = tagIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (normalizedTagIds.isEmpty()) {
            return Collections.emptyList();
        }
        if (tagMapper.selectBatchIds(normalizedTagIds).size() != normalizedTagIds.size()) {
            throw new BusinessException(TagExceptionEnum.TAG_SELECTED_NOT_FOUND);
        }
        return normalizedTagIds;
    }

    /**
     * 去除标题首尾空格并校验非空与长度
     */
    private String normalizeTitle(String title) {
        String trimmed = title == null ? "" : title.trim();
        if (trimmed.isEmpty()) {
            throw new BusinessException(ArticleExceptionEnum.ARTICLE_TITLE_REQUIRED);
        }
        if (trimmed.length() > 200) {
            throw new BusinessException(ArticleExceptionEnum.ARTICLE_TITLE_TOO_LONG);
        }
        return trimmed;
    }

    /**
     * 校验正文非空
     */
    private String normalizeContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new BusinessException(ArticleExceptionEnum.ARTICLE_CONTENT_REQUIRED);
        }
        return content;
    }

    /**
     * 校验分类存在
     */
    private Integer requireCategory(Integer categoryId) {
        if (categoryId == null) {
            throw new BusinessException(ArticleExceptionEnum.ARTICLE_CATEGORY_REQUIRED);
        }
        if (categoryMapper.selectById(categoryId) == null) {
            throw new BusinessException(CategoryExceptionEnum.CATEGORY_SELECTED_NOT_FOUND);
        }
        return categoryId;
    }

    /**
     * 校验文章状态取值
     */
    private int normalizeStatus(Integer status) {
        if (status != STATUS_DRAFT && status != STATUS_PUBLISHED) {
            throw new BusinessException(ArticleExceptionEnum.ARTICLE_STATUS_INVALID);
        }
        return status;
    }

    /**
     * 可选文本去除首尾空格，空值保留为 null
     */
    private String normalizeOptionalText(String text) {
        return text == null ? null : text.trim();
    }
}
