package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blog.pojo.po.Category;
import com.blog.pojo.vo.CategoryVO;
import org.apache.ibatis.annotations.Param;

/**
 * 分类 Mapper
 */
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 分页查询分类，复杂查询下沉到 XML 实现
     */
    IPage<CategoryVO> selectCategoryPage(IPage<CategoryVO> page, @Param("name") String name);
}
