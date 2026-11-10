package com.blog.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用分页查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageQueryDTO {

    /**
     * 当前页码
     */
    private Integer page = 1;

    /**
     * 每页记录数
     */
    private Integer pageSize = 10;
}
