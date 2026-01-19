package com.my.blog.server.service.user.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.pojo.po.Category;
import com.my.blog.pojo.vo.user.UserHotCategoryVO;
import com.my.blog.server.mapper.CategoryMapper;
import com.my.blog.server.service.user.ICategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("user-category-service")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {
    /**
     * 获取最热门的5个分类
     * @return 最热门的5个分类
     */
    @Override
    public List<UserHotCategoryVO> getHotCategoryTop5() {
        return baseMapper.getHotCategoryTop5();
    }
}
