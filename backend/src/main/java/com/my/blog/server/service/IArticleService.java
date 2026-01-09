package com.my.blog.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.common.result.PageResult;
import com.my.blog.pojo.dto.admin.AdminArticleDTO;
import com.my.blog.pojo.dto.admin.AdminArticlePageQueryDTO;
import com.my.blog.pojo.po.Article;
import com.my.blog.pojo.vo.admin.AdminArticlePageQueryVO;
import com.my.blog.pojo.vo.admin.AdminArticlePreviewVO;
import com.my.blog.pojo.vo.admin.AdminArticleVO;

public interface IArticleService extends IService<Article> {

    /**
     * 分页查询文章
     * @param adminArticlePageQueryDTO 查询条件
     * @return 分页结果
     */
    PageResult<AdminArticlePageQueryVO> pageQuery(AdminArticlePageQueryDTO adminArticlePageQueryDTO);

    /**
     * 根据id查询文章
     * @param id 文章id
     * @return 文章数据
     */
    AdminArticleVO getById(Long id);

    /**
     * 新增文章
      * @param adminArticleDTO 分类数据
     */
    void addArticle(AdminArticleDTO adminArticleDTO);

    /**
     * 修改文章
     * @param adminArticleDTO 分类数据
     */
    void updateArticle(AdminArticleDTO adminArticleDTO);

    /**
     * 修改文章状态
     * @param id 文章id
     * @param status 文章状态
     */
    void updateStatus(Long id,Integer status);

    /**
     * 预览文章
     * @param id 文章id
     * @return 文章预览数据
     */
    AdminArticlePreviewVO preview(Long id);
}
