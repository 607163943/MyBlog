package com.my.blog.server.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import com.my.blog.common.result.PageResult;
import com.my.blog.common.result.Result;
import com.my.blog.pojo.dto.admin.AdminDictDTO;
import com.my.blog.pojo.dto.admin.AdminDictPageQueryDTO;
import com.my.blog.pojo.po.Dict;
import com.my.blog.pojo.vo.admin.AdminDictPageQueryVO;
import com.my.blog.pojo.vo.admin.AdminDictVO;
import com.my.blog.server.config.valid.UpdateValidGroup;
import com.my.blog.server.service.IDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.groups.Default;
import java.util.List;

@Api(tags = "字典管理")
@RequestMapping("/admin/dict")
@RestController
public class DictController {
    @Resource
    private IDictService dictService;

    @ApiOperation("分页查询字典")
    @GetMapping
    public Result<PageResult<AdminDictPageQueryVO>> pageQuery(AdminDictPageQueryDTO adminDictPageQueryDTO) {
        PageResult<AdminDictPageQueryVO> pageResult = dictService.pageQuery(adminDictPageQueryDTO);
        return Result.success(pageResult);
    }

    @ApiOperation("根据id查询字典")
    @GetMapping("/{id}")
    public Result<AdminDictVO> getById(@PathVariable Long id) {
        Dict dict = dictService.getById(id);
        AdminDictVO adminDictVO = BeanUtil.copyProperties(dict, AdminDictVO.class);
        return Result.success(adminDictVO);
    }

    @ApiOperation("添加字典")
    @PostMapping
    public Result<Object> addDict(@Validated @RequestBody AdminDictDTO adminDictDTO) {
        dictService.addDict(adminDictDTO);
        return Result.success();
    }

    @ApiOperation("修改字典")
    @PutMapping
    public Result<Object> updateDict(@Validated({Default.class, UpdateValidGroup.class}) @RequestBody AdminDictDTO adminDictDTO) {
        dictService.updateDict(adminDictDTO);
        return Result.success();
    }

    @ApiOperation("根据id修改字典状态")
    @PatchMapping("/{id}/status")
    public Result<Object> updateStatus(@PathVariable Long id) {
        dictService.updateStatus(id);
        return Result.success();
    }

    @ApiOperation("根据id删除字典")
    @DeleteMapping("/{id}")
    public Result<Object> deleteById(@PathVariable Long id) {
        dictService.removeById(id);
        return Result.success();
    }

    @ApiOperation("批量删除字典")
    @DeleteMapping
    public Result<Object> deleteByIds(@RequestParam List<Long> ids) {
        dictService.removeBatchByIds(ids);
        return Result.success();
    }
}
