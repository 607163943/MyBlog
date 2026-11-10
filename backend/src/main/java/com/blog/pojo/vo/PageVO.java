package com.blog.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 通用分页返回结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVO<T> {

    /**
     * 当前页数据
     */
    private List<T> records;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 每页记录数
     */
    private Long size;

    /**
     * 总页数
     */
    private Long pages;
}
