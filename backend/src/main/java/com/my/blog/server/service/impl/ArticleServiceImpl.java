package com.my.blog.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.common.constants.BizTypeConstant;
import com.my.blog.common.constants.UploadFileRefStatus;
import com.my.blog.common.enums.ExceptionEnums;
import com.my.blog.common.exception.admin.AdminArticleException;
import com.my.blog.common.result.PageResult;
import com.my.blog.common.utils.PageQueryUtils;
import com.my.blog.pojo.dto.admin.AdminArticleDTO;
import com.my.blog.pojo.dto.admin.AdminArticlePageQueryDTO;
import com.my.blog.pojo.po.*;
import com.my.blog.pojo.vo.admin.*;
import com.my.blog.server.mapper.ArticleMapper;
import com.my.blog.server.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {
    @Resource
    private PageQueryUtils pageQueryUtils;

    @Resource
    private IUploadFileRefService uploadFileRefService;

    @Resource
    private IArticleTagService articleTagService;
    @Resource
    private ICategoryService categoryService;
    @Resource
    private ITagService tagService;


    /**
     * 分页查询文章
     *
     * @param adminArticlePageQueryDTO 查询条件
     * @return 分页结果
     */
    @Override
    public PageResult<AdminArticlePageQueryVO> pageQuery(AdminArticlePageQueryDTO adminArticlePageQueryDTO) {
        // 参数校验和初始化
        pageQueryUtils.checkAndInitPageQuery(adminArticlePageQueryDTO);

        // 构建分页条件
        Page<Article> page = new Page<>();
        page.setCurrent(adminArticlePageQueryDTO.getPageNum());
        page.setSize(adminArticlePageQueryDTO.getPageSize());

        // 获取标签对应文章id
        List<ArticleTag> articleTags = articleTagService.lambdaQuery()
                .eq(adminArticlePageQueryDTO.getTagId() != null,
                        ArticleTag::getTagId,
                        adminArticlePageQueryDTO.getTagId())
                .list();
        List<Long> articleIds = articleTags.stream().map(ArticleTag::getArticleId).collect(Collectors.toList());

        // 查询
        page = lambdaQuery()
                .like(StrUtil.isNotEmpty(adminArticlePageQueryDTO.getTitle()),
                        Article::getTitle,
                        adminArticlePageQueryDTO.getTitle())
                .eq(adminArticlePageQueryDTO.getStatus() != null,
                        Article::getStatus,
                        adminArticlePageQueryDTO.getStatus())
                .eq(adminArticlePageQueryDTO.getCategoryId() != null,
                        Article::getCategoryId,
                        adminArticlePageQueryDTO.getCategoryId())
                .in(!articleIds.isEmpty(),
                        Article::getId,
                        articleIds)
                .page(page);

        // 构建VO数据
        List<Article> articleList = page.getRecords();
        List<AdminArticlePageQueryVO> adminArticlePageQueryVOS = new ArrayList<>(articleList.size());
        List<Category> categories = categoryService.list();
        // 构建分类和文章id映射
        Map<Long, Category> categoryMap = categories.stream().collect(Collectors.toMap(Category::getId, category -> category));
        // 构建标签和文章id映射
        List<Tag> tags = tagService.list();
        Map<Long, Tag> tagMap = tags.stream().collect(Collectors.toMap(Tag::getId, tag -> tag));

        // 构建文章id和文章标签关联映射
        List<ArticleTag> articleTagList = articleTagService.list();
        Map<Long, List<ArticleTag>> longListMap = articleTagList.stream().collect(Collectors.groupingBy(ArticleTag::getArticleId));

        // 补充分类和标签数据
        for (Article article : articleList) {
            AdminArticlePageQueryVO adminArticlePageQueryVO = BeanUtil.copyProperties(article, AdminArticlePageQueryVO.class);
            // 补充分类数据
            adminArticlePageQueryVO.setCategoryName(
                    categoryMap.get(adminArticlePageQueryVO.getCategoryId()).getName());

            // 补充标签数据
            List<ArticleTag> tempArticleTagList = longListMap.get(article.getId());
            List<AdminTagPageQueryVO> adminTagPageQueryVOS = new ArrayList<>(tempArticleTagList.size());
            for (ArticleTag articleTag : tempArticleTagList) {
                Tag tag = tagMap.get(articleTag.getTagId());
                AdminTagPageQueryVO adminTagPageQueryVO = BeanUtil.copyProperties(tag, AdminTagPageQueryVO.class);
                adminTagPageQueryVOS.add(adminTagPageQueryVO);
            }
            adminArticlePageQueryVO.setTags(adminTagPageQueryVOS);

            adminArticlePageQueryVOS.add(adminArticlePageQueryVO);
        }

        return PageResult.<AdminArticlePageQueryVO>builder()
                .pageSize(page.getPages())
                .pageNum(page.getCurrent())
                .total(page.getTotal())
                .result(adminArticlePageQueryVOS)
                .build();
    }

    /**
     * 根据id查询文章
     *
     * @param id 文章id
     * @return 文章数据
     */
    @Override
    public AdminArticleVO getById(Long id) {
        Article article = super.getById(id);
        List<ArticleTag> articleTags = articleTagService.lambdaQuery()
                .eq(ArticleTag::getArticleId, id)
                .list();
        List<Long> tagIds = articleTags.stream().map(ArticleTag::getTagId).collect(Collectors.toList());
        AdminArticleVO adminArticleVO = BeanUtil.copyProperties(article, AdminArticleVO.class);
        adminArticleVO.setTagIds(tagIds);
        return adminArticleVO;
    }

    /**
     * 添加分类
     *
     * @param adminArticleDTO 分类数据
     */
    @Transactional
    @Override
    public void addArticle(AdminArticleDTO adminArticleDTO) {
        // 检测是否存在同名分类
        Long count = lambdaQuery()
                .eq(Article::getCategoryId, adminArticleDTO.getCategoryId())
                .eq(Article::getTitle, adminArticleDTO.getTitle())
                .count();
        if (count > 0) {
            throw new AdminArticleException(ExceptionEnums.ADMIN_ARTICLE_EXIST);
        }

        // 保存文章
        Article article = BeanUtil.copyProperties(adminArticleDTO, Article.class);
        save(article);

        // 保存文章标签关联数据
        List<ArticleTag> articleTags = new ArrayList<>(adminArticleDTO.getTagIds().size());
        for (Long tagId : adminArticleDTO.getTagIds()) {
            ArticleTag articleTag = ArticleTag.builder()
                    .articleId(article.getId())
                    .tagId(tagId)
                    .build();
            articleTags.add(articleTag);
        }
        articleTagService.saveBatch(articleTags);

        // 标记引用文件状态为已使用，同时更新业务标记
        if (adminArticleDTO.getUploadFileRefId() != null) {
            UploadFileRef uploadFileRef = uploadFileRefService.lambdaQuery()
                    .eq(UploadFileRef::getId, adminArticleDTO.getUploadFileRefId())
                    .one();

            if (uploadFileRef == null) {
                throw new AdminArticleException(ExceptionEnums.ADMIN_ARTICLE_COVER_NOT_EXIST);
            }

            // 更新为使用
            uploadFileRef.setStatus(UploadFileRefStatus.USE);
            // 补充业务数据
            uploadFileRef.setBizType(BizTypeConstant.ARTICLE_COVER);
            uploadFileRef.setBizId(article.getId());

            uploadFileRefService.updateById(uploadFileRef);
        }
    }

    /**
     * 修改分类
     *
     * @param adminArticleDTO 分类数据
     */
    @Transactional
    @Override
    public void updateArticle(AdminArticleDTO adminArticleDTO) {
        // 检测修改后是否存在同名标签
        Long count = lambdaQuery()
                .eq(Article::getCategoryId, adminArticleDTO.getCategoryId())
                .eq(Article::getTitle, adminArticleDTO.getTitle())
                .notIn(Article::getId, adminArticleDTO.getId())
                .count();
        if (count > 0) {
            throw new AdminArticleException(ExceptionEnums.ADMIN_ARTICLE_EXIST);
        }

        Article article = BeanUtil.copyProperties(adminArticleDTO, Article.class);
        updateById(article);

        // 标记引用文件状态为已使用，同时更新业务标记，旧文件标记为未使用
        if (adminArticleDTO.getUploadFileRefId() != null) {
            UploadFileRef uploadFileRef = uploadFileRefService.lambdaQuery()
                    .eq(UploadFileRef::getBizId, adminArticleDTO.getId())
                    .one();
            // 旧文件标记为未使用
            if (uploadFileRef != null) {
                uploadFileRef.setStatus(UploadFileRefStatus.NOT_USE);
                uploadFileRefService.updateById(uploadFileRef);
            }

            // 标记引用文件状态为已使用，同时更新业务标记
            uploadFileRef = uploadFileRefService.lambdaQuery()
                    .eq(UploadFileRef::getId, adminArticleDTO.getUploadFileRefId())
                    .one();

            if (uploadFileRef == null) {
                throw new AdminArticleException(ExceptionEnums.ADMIN_ARTICLE_COVER_NOT_EXIST);
            }

            // 更新为使用
            uploadFileRef.setStatus(UploadFileRefStatus.USE);
            // 补充业务数据
            uploadFileRef.setBizType(BizTypeConstant.ARTICLE_COVER);
            uploadFileRef.setBizId(article.getId());

            uploadFileRefService.updateById(uploadFileRef);
        }
    }

    /**
     * 修改分类状态
     *
     * @param id     文章id
     * @param status 文章状态
     */
    @Override
    public void updateStatus(Long id, Integer status) {
        List<Integer> statusList = Arrays.asList(0, 1, 2);
        if(!statusList.contains(status)) {
            throw new AdminArticleException(ExceptionEnums.ADMIN_ARTICLE_STATUS_ERROR);
        }
        Article article = super.getById(id);
        article.setStatus(status);
        updateById(article);
    }

    /**
     * 预览文章
     * @param id 文章id
     * @return 文章数据
     */
    @Override
    public AdminArticlePreviewVO preview(Long id) {
        // 查询文章
        Article article = super.getById(id);
        AdminArticlePreviewVO adminArticlePreviewVO = BeanUtil.copyProperties(article, AdminArticlePreviewVO.class);
        // 标签数据补充
        List<ArticleTag> articleTags = articleTagService.lambdaQuery()
                .eq(ArticleTag::getArticleId, article.getId())
                .list();
        List<Long> tagIds = articleTags.stream().map(ArticleTag::getTagId).collect(Collectors.toList());
        List<Tag> tagList = tagService.listByIds(tagIds);
        List<AdminTagVO> adminTagVOS = BeanUtil.copyToList(tagList, AdminTagVO.class);
        adminArticlePreviewVO.setTags(adminTagVOS);

        return adminArticlePreviewVO;
    }
}
