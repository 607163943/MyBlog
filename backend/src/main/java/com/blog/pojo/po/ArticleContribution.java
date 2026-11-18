package com.blog.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 文章每日贡献统计实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_article_contribution")
public class ArticleContribution {

    /**
     * 贡献记录 ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 贡献用户 ID
     */
    private Integer userId;

    /**
     * 贡献日期（自然日），一个用户一天只保留一行
     */
    private LocalDate contributionDate;

    /**
     * 当日文章贡献次数
     */
    private Integer contributionCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
