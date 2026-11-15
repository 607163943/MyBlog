package com.blog.pojo.vo.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 前台标签简要信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebTagBriefVO {

    /**
     * 标签 ID
     */
    private Integer id;

    /**
     * 标签名称
     */
    private String name;
}
