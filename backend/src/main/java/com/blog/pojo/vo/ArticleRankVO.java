package com.blog.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 文章浏览量排行
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleRankVO {

    /**
     * 文章 ID
     */
    private Integer id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 分类 ID
     */
    private Integer categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 标签名称列表，由服务层二次查询回填
     */
    private List<String> tagNames;
}
