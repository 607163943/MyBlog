package com.my.blog.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.blog.pojo.po.Tag;
import com.my.blog.pojo.vo.admin.RatioChartData;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 统计标签状态
     * @return 统计结果
     */
    List<RatioChartData> countGroupByStatus();
}
