package com.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.exception.BusinessException;
import com.blog.mapper.ArticleMapper;
import com.blog.mapper.CategoryMapper;
import com.blog.pojo.dto.ArticleDTO;
import com.blog.pojo.dto.ArticlePageQueryDTO;
import com.blog.pojo.po.Article;
import com.blog.pojo.vo.ArticleVO;
import com.blog.pojo.vo.PageVO;
import com.blog.service.ArticleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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

        Article article = new Article();
        article.setId(dto.getId());
        article.setTitle(title);
        article.setDescription(normalizeOptionalText(dto.getDescription()));
        article.setContent(content);
        article.setCover(normalizeOptionalText(dto.getCover()));
        article.setCategoryId(categoryId);
        article.setStatus(status);
        updateById(article);
    }

    @Override
    public void deleteArticle(Integer id) {
        if (id == null || !removeById(id)) {
            throw new BusinessException("文章不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticles(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("请选择要删除的文章");
        }
        removeByIds(ids);
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
