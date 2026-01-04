package com.my.blog.server.controller.admin;

import com.my.blog.common.result.Result;
import com.my.blog.common.utils.PageQueryUtil;
import com.my.blog.common.utils.PageResult;
import com.my.blog.server.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Api(tags = "分类管理")
@RestController
@RequestMapping("/admin/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;
    /**
     * 分类列表
     */
    @ApiOperation("分页查询分类")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Result<PageResult> list(@RequestParam Map<String, Object> params) {
        if (ObjectUtils.isEmpty(params.get("page")) || ObjectUtils.isEmpty(params.get("limit"))) {
            return Result.error(400,"参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return Result.success(categoryService.getBlogCategoryPage(pageUtil));
    }

    ///**
    // * 分类添加
    // */
    //@RequestMapping(value = "/categories/save", method = RequestMethod.POST)
    //@ResponseBody
    //public Result save(@RequestParam("categoryName") String categoryName,
    //                   @RequestParam("categoryIcon") String categoryIcon) {
    //    if (!StringUtils.hasText(categoryName)) {
    //        return ResultGenerator.genFailResult("请输入分类名称！");
    //    }
    //    if (!StringUtils.hasText(categoryIcon)) {
    //        return ResultGenerator.genFailResult("请选择分类图标！");
    //    }
    //    if (categoryService.saveCategory(categoryName, categoryIcon)) {
    //        return ResultGenerator.genSuccessResult();
    //    } else {
    //        return ResultGenerator.genFailResult("分类名称重复");
    //    }
    //}


    ///**
    // * 分类修改
    // */
    //@RequestMapping(value = "/categories/update", method = RequestMethod.POST)
    //@ResponseBody
    //public Result update(@RequestParam("categoryId") Integer categoryId,
    //                     @RequestParam("categoryName") String categoryName,
    //                     @RequestParam("categoryIcon") String categoryIcon) {
    //    if (!StringUtils.hasText(categoryName)) {
    //        return ResultGenerator.genFailResult("请输入分类名称！");
    //    }
    //    if (!StringUtils.hasText(categoryIcon)) {
    //        return ResultGenerator.genFailResult("请选择分类图标！");
    //    }
    //    if (categoryService.updateCategory(categoryId, categoryName, categoryIcon)) {
    //        return ResultGenerator.genSuccessResult();
    //    } else {
    //        return ResultGenerator.genFailResult("分类名称重复");
    //    }
    //}


    ///**
    // * 分类删除
    // */
    //@RequestMapping(value = "/categories/delete", method = RequestMethod.POST)
    //@ResponseBody
    //public Result delete(@RequestBody Integer[] ids) {
    //    if (ids.length < 1) {
    //        return ResultGenerator.genFailResult("参数异常！");
    //    }
    //    if (categoryService.deleteBatch(ids)) {
    //        return ResultGenerator.genSuccessResult();
    //    } else {
    //        return ResultGenerator.genFailResult("删除失败");
    //    }
    //}

}
