package com.blog.pojo.vo.admin;

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
     * 封面文件 ID
     */
    private Integer coverFileId;

    /**
     * 所属分类 ID：0 表示无分类
     */
    private Integer categoryId;

    /**
     * 所属分类名称
     */
    private String categoryName;

    /**
     * 分类内排序值：值越小越靠前，null 表示未编号
     */
    private Integer categorySort;

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
     * 首次发布时间：草稿为 null
     */
    private LocalDateTime publishTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
