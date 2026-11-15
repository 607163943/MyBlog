package com.blog.pojo.dto.admin;

import com.blog.pojo.dto.PageQueryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 文章分页查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ArticlePageQueryDTO extends PageQueryDTO {

    /**
     * 文章标题，模糊搜索
     */
    private String title;

    /**
     * 文章状态：0-草稿，1-已发布，为空表示全部
     */
    private Integer status;

    /**
     * 所属分类 ID，为空表示全部分类
     */
    private Integer categoryId;
}
