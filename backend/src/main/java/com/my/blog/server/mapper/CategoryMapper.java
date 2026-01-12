package com.my.blog.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.blog.pojo.po.Category;
import com.my.blog.pojo.vo.admin.RatioChartData;

import java.util.List;

public interface CategoryMapper extends BaseMapper<Category> {
    /**
     * 分类状态占比
     * @return 分类状态占比
     */
    List<RatioChartData> countGroupByStatus();
}
