package com.blog.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 分类文章数排行查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CategoryRankQueryDTO extends PageQueryDTO {

    /**
     * 分类名称，模糊搜索
     */
    private String name;

    /**
     * 最小文章数（大于等于）
     */
    private Integer minCount;

    /**
     * 最大文章数（小于等于）
     */
    private Integer maxCount;
}
