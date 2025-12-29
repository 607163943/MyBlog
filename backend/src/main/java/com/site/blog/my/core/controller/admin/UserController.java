package com.site.blog.my.core.controller.admin;

import com.site.blog.my.core.pojo.vo.UserInfoVO;
import com.site.blog.my.core.result.Result;
import com.site.blog.my.core.service.AdminUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/admin/user")
public class UserController {
    @Resource
    private AdminUserService adminUserService;

    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public Result<UserInfoVO> getUserInfo() {
        return Result.success(adminUserService.getUserInfo());
    }
}
