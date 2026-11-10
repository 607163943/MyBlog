package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.pojo.dto.ArticleDTO;
import com.blog.pojo.dto.ArticlePageQueryDTO;
import com.blog.pojo.po.Article;
import com.blog.pojo.vo.ArticleVO;
import com.blog.pojo.vo.PageVO;

import java.util.List;

/**
 * 文章服务
 */
public interface ArticleService extends IService<Article> {

    /**
     * 分页查询文章
     */
    PageVO<ArticleVO> pageQuery(ArticlePageQueryDTO dto);

    /**
     * 根据 ID 查询文章
     */
    ArticleVO getArticleById(Integer id);

    /**
     * 新增文章
     */
    void addArticle(ArticleDTO dto);

    /**
     * 修改文章
     */
    void updateArticle(ArticleDTO dto);

    /**
     * 删除单个文章
     */
    void deleteArticle(Integer id);

    /**
     * 批量删除文章
     */
    void deleteArticles(List<Integer> ids);
}
