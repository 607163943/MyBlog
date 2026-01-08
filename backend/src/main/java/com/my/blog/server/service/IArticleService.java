package com.my.blog.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.common.result.PageResult;
import com.my.blog.pojo.dto.admin.AdminArticlePageQueryDTO;
import com.my.blog.pojo.po.Article;
import com.my.blog.pojo.vo.admin.AdminArticlePageQueryVO;

public interface IArticleService extends IService<Article> {

    /**
     * 分页查询文章
     * @param adminArticlePageQueryDTO 查询条件
     * @return 分页结果
     */
    PageResult<AdminArticlePageQueryVO> pageQuery(AdminArticlePageQueryDTO adminArticlePageQueryDTO);

    /**
     * 新增文章
      * @param adminArticleDTO 分类数据
     */
    //void addArticle(AdminArticleDTO adminArticleDTO);

    /**
     * 修改文章
     * @param adminArticleDTO 分类数据
     */
    //void updateArticle(AdminArticleDTO adminArticleDTO);

    /**
     * 修改文章状态
     * @param id 分类id，修改分类状态
     */
    //void updateStatus(Long id);
}
