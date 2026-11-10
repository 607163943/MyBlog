package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.exception.BusinessException;
import com.blog.mapper.ArticleMapper;
import com.blog.mapper.ArticleTagMapper;
import com.blog.mapper.CategoryMapper;
import com.blog.mapper.TagMapper;
import com.blog.pojo.dto.ArticleDTO;
import com.blog.pojo.dto.ArticlePageQueryDTO;
import com.blog.pojo.po.Article;
import com.blog.pojo.po.ArticleTag;
import com.blog.pojo.vo.ArticleVO;
import com.blog.pojo.vo.PageVO;
import com.blog.service.ArticleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文章服务实现
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 暂无登录鉴权模块，新增文章默认归属管理员
     */
    private static final int DEFAULT_AUTHOR_ID = 1;

    private static final int STATUS_DRAFT = 0;
    private static final int STATUS_PUBLISHED = 1;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private ArticleTagMapper articleTagMapper;

    @Override
    public PageVO<ArticleVO> pageQuery(ArticlePageQueryDTO dto) {
        int pageNum = dto.getPage() == null || dto.getPage() < 1 ? DEFAULT_PAGE_NUM : dto.getPage();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? DEFAULT_PAGE_SIZE : dto.getPageSize();

        Page<ArticleVO> page = new Page<>(pageNum, pageSize);
        IPage<ArticleVO> pageResult = baseMapper.selectArticlePage(page, dto.getTitle(), dto.getStatus());

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
            throw new BusinessException("文章不存在");
        }
        ArticleVO articleVO = baseMapper.selectArticleDetailById(id);
        if (articleVO == null) {
            throw new BusinessException("文章不存在");
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
        article.setAuthorId(DEFAULT_AUTHOR_ID);
        article.setStatus(status);
        article.setViewCount(0);
        save(article);
        saveArticleTags(article.getId(), dto.getTagIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(ArticleDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("文章ID不能为空");
        }
        Article existing = getById(dto.getId());
        if (existing == null) {
            throw new BusinessException("文章不存在");
        }

        String title = normalizeTitle(dto.getTitle());
        String content = normalizeContent(dto.getContent());
        Integer categoryId = dto.getCategoryId() == null ? existing.getCategoryId() : dto.getCategoryId();
        requireCategory(categoryId);
        int status = dto.getStatus() == null
                ? existing.getStatus()
                : normalizeStatus(dto.getStatus());

        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<Article>()
                .eq(Article::getId, dto.getId())
                .set(Article::getTitle, title)
                .set(Article::getDescription, normalizeOptionalText(dto.getDescription()))
                .set(Article::getContent, content)
                .set(Article::getCover, normalizeOptionalText(dto.getCover()))
                .set(Article::getCategoryId, categoryId)
                .set(Article::getStatus, status);
        baseMapper.update(null, updateWrapper);

        if (dto.getTagIds() != null) {
            replaceArticleTags(dto.getId(), dto.getTagIds());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticle(Integer id) {
        if (id == null || !removeById(id)) {
            throw new BusinessException("文章不存在");
        }
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getArticleId, id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticles(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("请选择要删除的文章");
        }
        removeByIds(ids);
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getArticleId, ids));
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
            throw new BusinessException("所选标签不存在");
        }
        return normalizedTagIds;
    }

    /**
     * 去除标题首尾空格并校验非空与长度
     */
    private String normalizeTitle(String title) {
        String trimmed = title == null ? "" : title.trim();
        if (trimmed.isEmpty()) {
            throw new BusinessException("文章标题不能为空");
        }
        if (trimmed.length() > 200) {
            throw new BusinessException("文章标题不能超过 200 个字符");
        }
        return trimmed;
    }

    /**
     * 校验正文非空
     */
    private String normalizeContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new BusinessException("文章正文不能为空");
        }
        return content;
    }

    /**
     * 校验分类存在
     */
    private Integer requireCategory(Integer categoryId) {
        if (categoryId == null) {
            throw new BusinessException("请选择文章分类");
        }
        if (categoryMapper.selectById(categoryId) == null) {
            throw new BusinessException("所选分类不存在");
        }
        return categoryId;
    }

    /**
     * 校验文章状态取值
     */
    private int normalizeStatus(Integer status) {
        if (status != STATUS_DRAFT && status != STATUS_PUBLISHED) {
            throw new BusinessException("文章状态不正确");
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
