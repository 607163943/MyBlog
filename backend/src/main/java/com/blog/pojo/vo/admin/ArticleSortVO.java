package com.blog.pojo.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类内文章排序项
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSortVO {

    /**
     * 文章 ID
     */
    private Integer id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 分类内排序值：值越小越靠前，null 表示未编号
     */
    private Integer categorySort;
}
