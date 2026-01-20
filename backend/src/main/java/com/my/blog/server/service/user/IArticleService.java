package com.my.blog.server.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.common.result.PageResult;
import com.my.blog.pojo.dto.ArticlePageQueryDTO;
import com.my.blog.pojo.po.Article;
import com.my.blog.pojo.vo.user.UserArticlePageQueryVO;
import com.my.blog.pojo.vo.user.UserArticleViewVO;

import java.util.List;

public interface IArticleService extends IService<Article> {
    /**
     * 分页查询文章
     * @param articlePageQueryDTO 查询条件
     * @return 分页结果
     */
    PageResult<UserArticlePageQueryVO> pageQuery(ArticlePageQueryDTO articlePageQueryDTO);

    /**
     * 最新发布文章Top5
     * @return 最新文章Top5
     */
    List<UserArticlePageQueryVO> newArticleTop5();

    /**
     * 文章详情
     * @param articleId 文章id
     * @return 文章详情
     */
    UserArticleViewVO articleView(Long articleId);
}
