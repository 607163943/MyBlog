package com.blog.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章标签名称投影
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTagNameVO {

    /**
     * 文章 ID
     */
    private Integer articleId;

    /**
     * 标签名称
     */
    private String tagName;
}
