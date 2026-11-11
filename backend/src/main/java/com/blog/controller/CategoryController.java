package com.blog.controller;

import com.blog.common.Result;
import com.blog.pojo.dto.CategoryDTO;
import com.blog.pojo.dto.CategoryPageQueryDTO;
import com.blog.pojo.vo.CategoryVO;
import com.blog.pojo.vo.PageVO;
import com.blog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 分类管理接口
 */
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 分页查询分类
     */
    @GetMapping("/page")
    public Result<PageVO<CategoryVO>> page(CategoryPageQueryDTO dto) {
        return Result.success(categoryService.pageQuery(dto));
    }

    /**
     * 根据 ID 查询分类
     */
    @GetMapping("/{id}")
    public Result<CategoryVO> getById(@PathVariable Integer id) {
        return Result.success(categoryService.getCategoryById(id));
    }

    /**
     * 新增分类
     */
    @PostMapping
    public Result<Void> add(@RequestBody CategoryDTO dto) {
        categoryService.addCategory(dto);
        return Result.success();
    }

    /**
     * 修改分类
     */
    @PutMapping
    public Result<Void> update(@RequestBody CategoryDTO dto) {
        categoryService.updateCategory(dto);
        return Result.success();
    }

    /**
     * 删除单个分类
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }

    /**
     * 批量删除分类
     */
    @DeleteMapping("/batch")
    public Result<Void> deleteBatch(@RequestBody List<Integer> ids) {
        categoryService.deleteCategories(ids);
        return Result.success();
    }
}
