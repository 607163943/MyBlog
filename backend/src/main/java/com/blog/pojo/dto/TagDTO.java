package com.blog.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签新增 / 修改参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {

    /**
     * 标签 ID，修改时必填
     */
    private Integer id;

    /**
     * 标签名称
     */
    private String name;
}
