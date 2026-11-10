package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blog.pojo.po.Article;
import com.blog.pojo.vo.ArticleVO;
import org.apache.ibatis.annotations.Param;

/**
 * 文章 Mapper
 */
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 分页查询文章，复杂查询下沉到 XML 实现
     */
    IPage<ArticleVO> selectArticlePage(IPage<ArticleVO> page,
                                       @Param("title") String title,
                                       @Param("status") Integer status);

    /**
     * 根据 ID 查询文章详情（含分类名称、作者昵称）
     */
    ArticleVO selectArticleDetailById(@Param("id") Integer id);
}
