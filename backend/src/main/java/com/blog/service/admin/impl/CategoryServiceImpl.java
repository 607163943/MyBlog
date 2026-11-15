package com.blog.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.exception.BusinessException;
import com.blog.mapper.CategoryMapper;
import com.blog.pojo.dto.admin.CategoryDTO;
import com.blog.pojo.dto.admin.CategoryPageQueryDTO;
import com.blog.pojo.dto.PageQueryDTO;
import com.blog.pojo.po.Category;
import com.blog.pojo.vo.admin.CategoryVO;
import com.blog.pojo.vo.PageVO;
import com.blog.service.admin.CategoryService;
import com.blog.util.PageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 分类服务实现
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public PageVO<CategoryVO> pageQuery(CategoryPageQueryDTO dto) {
        PageQueryDTO query = PageUtil.checkPage(dto);

        Page<CategoryVO> page = new Page<>(query.getPage(), query.getPageSize());
        IPage<CategoryVO> pageResult = baseMapper.selectCategoryPage(page, dto.getName());

        PageVO<CategoryVO> pageVO = new PageVO<>();
        pageVO.setRecords(pageResult.getRecords());
        pageVO.setTotal(pageResult.getTotal());
        pageVO.setSize(pageResult.getSize());
        pageVO.setPages(pageResult.getPages());
        return pageVO;
    }

    @Override
    public CategoryVO getCategoryById(Integer id) {
        if (id == null) {
            throw new BusinessException("分类不存在");
        }
        Category category = getById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        CategoryVO categoryVO = new CategoryVO();
        BeanUtils.copyProperties(category, categoryVO);
        return categoryVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCategory(CategoryDTO dto) {
        String name = normalizeName(dto.getName());
        checkNameUnique(name, null);

        Category category = new Category();
        category.setName(name);
        category.setDescription(dto.getDescription());
        category.setSort(dto.getSort() == null ? 0 : dto.getSort());
        save(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(CategoryDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("分类ID不能为空");
        }
        Category existing = getById(dto.getId());
        if (existing == null) {
            throw new BusinessException("分类不存在");
        }
        String name = normalizeName(dto.getName());
        checkNameUnique(name, dto.getId());

        Category category = new Category();
        category.setId(dto.getId());
        category.setName(name);
        category.setDescription(dto.getDescription());
        category.setSort(dto.getSort() == null ? existing.getSort() : dto.getSort());
        updateById(category);
    }

    @Override
    public void deleteCategory(Integer id) {
        if (id == null || !removeById(id)) {
            throw new BusinessException("分类不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategories(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("请选择要删除的分类");
        }
        removeByIds(ids);
    }

    /**
     * 去除名称首尾空格并校验非空
     */
    private String normalizeName(String name) {
        String trimmed = name == null ? "" : name.trim();
        if (trimmed.isEmpty()) {
            throw new BusinessException("分类名称不能为空");
        }
        return trimmed;
    }

    /**
     * 校验分类名称唯一
     */
    private void checkNameUnique(String name, Integer excludeId) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<Category>().eq(Category::getName, name);
        if (excludeId != null) {
            wrapper.ne(Category::getId, excludeId);
        }
        if (count(wrapper) > 0) {
            throw new BusinessException("分类名称已存在");
        }
    }
}
