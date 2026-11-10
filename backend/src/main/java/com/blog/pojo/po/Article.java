package com.blog.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 文章实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_article")
public class Article {

    /**
     * 文章 ID
     */
    @TableId(type = IdType.AUTO)
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
     * 作者 ID
     */
    private Integer authorId;

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
