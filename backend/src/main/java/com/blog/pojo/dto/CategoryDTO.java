package com.blog.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类新增 / 修改参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    /**
     * 分类 ID，修改时必填
     */
    private Integer id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类描述
     */
    private String description;

    /**
     * 排序值，值越小越靠前
     */
    private Integer sort;
}
