package com.my.blog.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.common.result.PageResult;
import com.my.blog.pojo.dto.admin.AdminCategoryDTO;
import com.my.blog.pojo.dto.admin.AdminCategoryPageQueryDTO;
import com.my.blog.pojo.po.Category;
import com.my.blog.pojo.vo.admin.AdminCategoryPageQueryVO;

public interface ICategoryService extends IService<Category> {

    /**
     * 分页查询分类
      * @param adminCategoryPageQueryDTO 查询条件
     * @return 分页结果
     */
    PageResult<AdminCategoryPageQueryVO> pageQuery(AdminCategoryPageQueryDTO adminCategoryPageQueryDTO);

    /**
     * 新增分类
      * @param adminCategoryDTO 分类数据
     */
    void addCategory(AdminCategoryDTO adminCategoryDTO);

    /**
     * 修改分类
     * @param adminCategoryDTO 分类数据
     */
    void updateCategory(AdminCategoryDTO adminCategoryDTO);

    /**
     * 修改分类状态
     * @param id 分类id
     */
    void updateStatus(Long id);
}
