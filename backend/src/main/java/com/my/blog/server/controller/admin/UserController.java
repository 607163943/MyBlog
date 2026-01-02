package com.my.blog.server.controller.admin;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.my.blog.common.result.Result;
import com.my.blog.pojo.dto.UserLoginDTO;
import com.my.blog.pojo.vo.admin.CaptchaVO;
import com.my.blog.pojo.vo.admin.UserInfo;
import com.my.blog.pojo.vo.admin.UserLoginVO;
import com.my.blog.server.service.AdminUserService;
import com.my.blog.server.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/admin/user")
public class UserController {
    @Resource
    private AdminUserService adminUserService;

    @Resource
    private IUserService userService;

    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public Result<UserInfo> getUserInfo() {
        return Result.success(adminUserService.getUserInfo());
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<UserLoginVO> login(@Validated @RequestBody UserLoginDTO userLoginDTO) {
        UserLoginVO userLoginVO = userService.login(userLoginDTO);

        return Result.success(userLoginVO);
    }

    /**
     * 获取验证码
     * @return 验证码数据
     */
    @ApiOperation("获取验证码")
    @GetMapping("/captcha")
    public Result<CaptchaVO> captcha() {
        CaptchaVO captchaVO=userService.captcha();

        return Result.success(captchaVO);

    }

    //@GetMapping("/profile")
    //public String profile(HttpServletRequest request) {
    //    Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
    //    AdminUser adminUser = adminUserService.getUserDetailById(loginUserId);
    //    if (adminUser == null) {
    //        return "admin/login";
    //    }
    //    request.setAttribute("path", "profile");
    //    request.setAttribute("loginUserName", adminUser.getLoginUserName());
    //    request.setAttribute("nickName", adminUser.getNickName());
    //    return "admin/profile";
    //}
    //
    //@PostMapping("/profile/password")
    //@ResponseBody
    //public String passwordUpdate(HttpServletRequest request, @RequestParam("originalPassword") String originalPassword,
    //                             @RequestParam("newPassword") String newPassword) {
    //    if (!StringUtils.hasText(originalPassword) || !StringUtils.hasText(newPassword)) {
    //        return "参数不能为空";
    //    }
    //    Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
    //    if (adminUserService.updatePassword(loginUserId, originalPassword, newPassword)) {
    //        //修改成功后清空session中的数据，前端控制跳转至登录页
    //        request.getSession().removeAttribute("loginUserId");
    //        request.getSession().removeAttribute("loginUser");
    //        request.getSession().removeAttribute("errorMsg");
    //        return "success";
    //    } else {
    //        return "修改失败";
    //    }
    //}
    //
    //@PostMapping("/profile/name")
    //@ResponseBody
    //public String nameUpdate(HttpServletRequest request, @RequestParam("loginUserName") String loginUserName,
    //                         @RequestParam("nickName") String nickName) {
    //    if (!StringUtils.hasText(loginUserName) || !StringUtils.hasText(nickName)) {
    //        return "参数不能为空";
    //    }
    //    Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
    //    if (adminUserService.updateName(loginUserId, loginUserName, nickName)) {
    //        return "success";
    //    } else {
    //        return "修改失败";
    //    }
    //}

    //@GetMapping("/logout")
    //public String logout(HttpServletRequest request) {
    //    request.getSession().removeAttribute("loginUserId");
    //    request.getSession().removeAttribute("loginUser");
    //    request.getSession().removeAttribute("errorMsg");
    //    return "admin/login";
    //}
}
