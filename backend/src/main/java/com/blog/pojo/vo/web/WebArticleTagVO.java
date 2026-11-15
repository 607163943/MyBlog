package com.blog.pojo.vo.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章与标签的关联查询结果，用于批量回填文章标签
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebArticleTagVO {

    /**
     * 文章 ID
     */
    private Integer articleId;

    /**
     * 标签 ID
     */
    private Integer tagId;

    /**
     * 标签名称
     */
    private String tagName;
}
