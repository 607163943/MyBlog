package com.my.blog.server.controller.admin;

import com.my.blog.common.result.PageResult;
import com.my.blog.common.result.Result;
import com.my.blog.pojo.dto.admin.AdminDictValuePageQueryDTO;
import com.my.blog.pojo.vo.admin.AdminDictValuePageQueryVO;
import com.my.blog.server.service.IDictValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "字典值管理")
@RequestMapping("/admin/dict-value")
@RestController
public class DictValueController {
    @Resource
    private IDictValueService dictValueService;

    @ApiOperation("分页查询字典内容")
    @GetMapping
    public Result<PageResult<AdminDictValuePageQueryVO>> pageQuery(@Validated AdminDictValuePageQueryDTO adminDictValuePageQueryDTO) {
        PageResult<AdminDictValuePageQueryVO> pageResult = dictValueService.pageQuery(adminDictValuePageQueryDTO);
        return Result.success(pageResult);
    }
}
