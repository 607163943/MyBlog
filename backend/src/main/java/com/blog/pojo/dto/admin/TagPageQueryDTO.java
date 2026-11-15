package com.blog.pojo.dto.admin;

import com.blog.pojo.dto.PageQueryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 标签分页查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TagPageQueryDTO extends PageQueryDTO {

    /**
     * 标签名称，模糊搜索
     */
    private String name;
}
