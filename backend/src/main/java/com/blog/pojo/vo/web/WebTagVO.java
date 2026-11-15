package com.blog.pojo.vo.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 前台标签信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebTagVO {

    /**
     * 标签 ID
     */
    private Integer id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 已发布文章数
     */
    private Long articleCount;
}
