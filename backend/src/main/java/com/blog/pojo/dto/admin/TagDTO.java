package com.blog.pojo.dto.admin;

import com.blog.validation.ValidationGroups;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    @NotNull(message = "标签ID不能为空", groups = ValidationGroups.Update.class)
    private Integer id;

    /**
     * 标签名称
     */
    @NotBlank(message = "标签名称不能为空")
    private String name;
}
