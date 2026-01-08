package com.my.blog.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.common.result.PageResult;
import com.my.blog.common.utils.PageQueryUtils;
import com.my.blog.pojo.dto.admin.AdminArticlePageQueryDTO;
import com.my.blog.pojo.po.Article;
import com.my.blog.pojo.po.ArticleTag;
import com.my.blog.pojo.vo.admin.AdminArticlePageQueryVO;
import com.my.blog.server.mapper.ArticleMapper;
import com.my.blog.server.service.IArticleService;
import com.my.blog.server.service.IArticleTagService;
import com.my.blog.server.service.IUploadFileRefService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {
    @Resource
    private PageQueryUtils pageQueryUtils;

    @Resource
    private IUploadFileRefService uploadFileRefService;

    @Resource
    private IArticleTagService articleTagService;

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
        List<AdminArticlePageQueryVO> adminArticlePageQueryVOS = BeanUtil.copyToList(page.getRecords(), AdminArticlePageQueryVO.class);

        return PageResult.<AdminArticlePageQueryVO>builder()
                .pageSize(page.getPages())
                .pageNum(page.getCurrent())
                .total(page.getTotal())
                .result(adminArticlePageQueryVOS)
                .build();
    }

    /**
     * 添加分类
     *
     * @param adminArticleDTO 分类数据
     */
    //@Transactional
    //@Override
    //public void addArticle(AdminArticleDTO adminArticleDTO) {
    //    // 检测是否存在同名分类
    //    Long count = lambdaQuery().eq(Category::getName, adminArticleDTO.getName()).count();
    //    if (count > 0) {
    //        throw new AdminCategoryException(ExceptionEnums.ADMIN_CATEGORY_EXIST);
    //    }
    //
    //    Article article = BeanUtil.copyProperties(adminArticleDTO, Article.class);
    //    save(article);
    //
    //    // 标记引用文件状态为已使用，同时更新业务标记
    //    if (adminArticleDTO.getUploadFileRefId() != null) {
    //        UploadFileRef uploadFileRef = uploadFileRefService.lambdaQuery()
    //                .eq(UploadFileRef::getId, adminArticleDTO.getUploadFileRefId())
    //                .one();
    //
    //        if (uploadFileRef == null) {
    //            throw new AdminCategoryException(ExceptionEnums.ADMIN_CATEGORY_COVER_NOT_EXIST);
    //        }
    //
    //        // 更新为使用
    //        uploadFileRef.setStatus(UploadFileRefStatus.USE);
    //        // 补充业务数据
    //        uploadFileRef.setBizType(BizTypeConstant.CATEGORY_COVER);
    //        uploadFileRef.setBizId(category.getId());
    //
    //        uploadFileRefService.updateById(uploadFileRef);
    //    }
    //}

    /**
     * 修改分类
     *
     * @param adminArticleDTO 分类数据
     */
    //@Transactional
    //@Override
    //public void updateArticle(AdminArticleDTO adminArticleDTO) {
    //    // 检测修改后是否存在同名标签
    //    Long count = lambdaQuery().eq(Category::getName, adminArticleDTO.getName())
    //            .notIn(Category::getId, adminArticleDTO.getId())
    //            .count();
    //    if (count > 0) {
    //        throw new AdminCategoryException(ExceptionEnums.ADMIN_CATEGORY_EXIST);
    //    }
    //
    //    Category category = BeanUtil.copyProperties(adminArticleDTO, Category.class);
    //    updateById(category);
    //
    //    // 标记引用文件状态为已使用，同时更新业务标记，旧文件标记为未使用
    //    if (adminArticleDTO.getUploadFileRefId() != null) {
    //        UploadFileRef uploadFileRef = uploadFileRefService.lambdaQuery()
    //                .eq(UploadFileRef::getBizId, adminArticleDTO.getId())
    //                .one();
    //        // 旧文件标记为未使用
    //        if (uploadFileRef != null) {
    //            uploadFileRef.setStatus(UploadFileRefStatus.NOT_USE);
    //            uploadFileRefService.updateById(uploadFileRef);
    //        }
    //
    //        // 标记引用文件状态为已使用，同时更新业务标记
    //        uploadFileRef = uploadFileRefService.lambdaQuery()
    //                .eq(UploadFileRef::getId, adminArticleDTO.getUploadFileRefId())
    //                .one();
    //
    //        if (uploadFileRef == null) {
    //            throw new AdminCategoryException(ExceptionEnums.ADMIN_CATEGORY_COVER_NOT_EXIST);
    //        }
    //
    //        // 更新为使用
    //        uploadFileRef.setStatus(UploadFileRefStatus.USE);
    //        // 补充业务数据
    //        uploadFileRef.setBizType(BizTypeConstant.CATEGORY_COVER);
    //        uploadFileRef.setBizId(category.getId());
    //
    //        uploadFileRefService.updateById(uploadFileRef);
    //    }
    //}

    /**
     * 修改分类状态
     *
     * @param id 分类id
     */
    //@Override
    //public void updateStatus(Long id) {
    //    Category category = getById(id);
    //    category.setStatus(category.getStatus() == 0 ? 1 : 0);
    //    updateById(category);
    //}
}
