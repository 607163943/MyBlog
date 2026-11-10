package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.pojo.dto.CategoryDTO;
import com.blog.pojo.dto.CategoryPageQueryDTO;
import com.blog.pojo.po.Category;
import com.blog.pojo.vo.CategoryVO;
import com.blog.pojo.vo.PageVO;

import java.util.List;

/**
 * 分类服务
 */
public interface CategoryService extends IService<Category> {

    /**
     * 分页查询分类
     */
    PageVO<CategoryVO> pageQuery(CategoryPageQueryDTO dto);

    /**
     * 根据 ID 查询分类
     */
    CategoryVO getCategoryById(Integer id);

    /**
     * 新增分类
     */
    void addCategory(CategoryDTO dto);

    /**
     * 修改分类
     */
    void updateCategory(CategoryDTO dto);

    /**
     * 删除单个分类
     */
    void deleteCategory(Integer id);

    /**
     * 批量删除分类
     */
    void deleteCategories(List<Integer> ids);
}
