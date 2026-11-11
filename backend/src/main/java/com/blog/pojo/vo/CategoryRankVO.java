package com.blog.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类文章数排行
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRankVO {

    /**
     * 分类 ID
     */
    private Integer id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 文章数
     */
    private Long articleCount;
}
