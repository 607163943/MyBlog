package com.blog.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 文章-标签关联实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_article_tag")
public class ArticleTag {

    /**
     * 关联记录 ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 文章 ID
     */
    private Integer articleId;

    /**
     * 标签 ID
     */
    private Integer tagId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
