package com.blog.pojo.dto.web;

import com.blog.pojo.dto.PageQueryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 前台文章分页查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WebArticlePageQueryDTO extends PageQueryDTO {

    /**
     * 分类 ID
     */
    private Integer categoryId;

    /**
     * 标签 ID
     */
    private Integer tagId;

    /**
     * 搜索关键词：匹配标题、摘要、分类名、标签名
     */
    private String keyword;
}
