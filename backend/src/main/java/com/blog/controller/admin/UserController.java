package com.blog.controller.admin;

import com.blog.annotation.OperationLog;
import com.blog.common.Result;
import com.blog.pojo.dto.admin.ChangePasswordDTO;
import com.blog.pojo.dto.admin.UpdateUserInfoDTO;
import com.blog.pojo.dto.admin.UserLoginDTO;
import com.blog.pojo.vo.admin.CaptchaVO;
import com.blog.pojo.vo.admin.LoginVO;
import com.blog.pojo.vo.admin.UserInfoVO;
import com.blog.service.admin.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 用户与登录接口
 */
@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 用户登录
     */
    @OperationLog(module = "user", action = "登录", key = "#a0.username")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody UserLoginDTO dto) {
        return Result.success(userService.login(dto));
    }

    /**
     * 退出登录
     */
    @OperationLog(module = "user", action = "退出登录")
    @PostMapping("/logout")
    public Result<Void> logout() {
        userService.logout();
        return Result.success();
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/info")
    public Result<UserInfoVO> getUserInfo() {
        return Result.success(userService.getUserInfo());
    }

    /**
     * 获取图形验证码
     */
    @GetMapping("/captcha")
    public Result<CaptchaVO> getCaptcha() {
        return Result.success(userService.generateCaptcha());
    }

    /**
     * 修改当前登录用户的基本信息
     */
    @OperationLog(module = "user", action = "修改个人信息")
    @PutMapping("/info")
    public Result<Void> updateUserInfo(@Valid @RequestBody UpdateUserInfoDTO dto) {
        userService.updateUserInfo(dto);
        return Result.success();
    }

    /**
     * 修改当前登录用户的密码
     */
    @OperationLog(module = "user", action = "修改密码")
    @PutMapping("/password")
    public Result<Void> changePassword(@Valid @RequestBody ChangePasswordDTO dto) {
        userService.changePassword(dto);
        return Result.success();
    }
}
