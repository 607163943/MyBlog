package com.blog.pojo.dto.admin;

import com.blog.validation.ValidationGroups;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    @NotNull(message = "分类ID不能为空", groups = ValidationGroups.Update.class)
    private Integer id;

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
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
