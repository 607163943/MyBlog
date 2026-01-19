package com.my.blog.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.blog.pojo.po.Category;
import com.my.blog.pojo.vo.user.UserHotCategoryVO;

import java.util.List;

public interface CategoryMapper extends BaseMapper<Category> {
    /**
     * 获取最热门的5个分类
     * @return 最热门的5个分类
     */
    List<UserHotCategoryVO> getHotCategoryTop5();
}
