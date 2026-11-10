package com.blog.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章返回结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleVO {

    /**
     * 文章 ID
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
     * 文章正文（Markdown 格式），仅详情返回
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
     * 所属分类名称
     */
    private String categoryName;

    /**
     * 标签 ID 列表
     */
    private List<Integer> tagIds;

    /**
     * 作者 ID
     */
    private Integer authorId;

    /**
     * 作者昵称
     */
    private String authorName;

    /**
     * 文章状态：0-草稿，1-已发布
     */
    private Integer status;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
