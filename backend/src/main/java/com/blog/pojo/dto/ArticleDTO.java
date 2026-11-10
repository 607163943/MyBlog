package com.blog.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 文章新增 / 修改参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {

    /**
     * 文章 ID，修改时必填
     */
    private Integer id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章描述 / 摘要
     */
    private String description;

    /**
     * 文章正文（Markdown 格式）
     */
    private String content;

    /**
     * 封面图片地址
     */
    private String cover;

    /**
     * 所属分类 ID
     */
    private Integer categoryId;

    /**
     * 标签 ID 列表
     */
    private List<Integer> tagIds;

    /**
     * 文章状态：0-草稿，1-已发布
     */
    private Integer status;
}
