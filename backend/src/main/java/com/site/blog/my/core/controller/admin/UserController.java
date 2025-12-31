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

    //@GetMapping({"/login"})
    //public String login() {
    //    return "admin/login";
    //}

    //@PostMapping(value = "/login")
    //public String login(@RequestParam("userName") String userName,
    //                    @RequestParam("password") String password,
    //                    @RequestParam("verifyCode") String verifyCode,
    //                    HttpSession session) {
    //    if (!StringUtils.hasText(verifyCode)) {
    //        session.setAttribute("errorMsg", "验证码不能为空");
    //        return "admin/login";
    //    }
    //    if (!StringUtils.hasText(userName) || !StringUtils.hasText(password)) {
    //        session.setAttribute("errorMsg", "用户名或密码不能为空");
    //        return "admin/login";
    //    }
    //    ShearCaptcha shearCaptcha = (ShearCaptcha) session.getAttribute("verifyCode");
    //    if (shearCaptcha == null || !shearCaptcha.verify(verifyCode)) {
    //        session.setAttribute("errorMsg", "验证码错误");
    //        return "admin/login";
    //    }
    //    AdminUser adminUser = adminUserService.login(userName, password);
    //    if (adminUser != null) {
    //        session.setAttribute("loginUser", adminUser.getNickName());
    //        session.setAttribute("loginUserId", adminUser.getAdminUserId());
    //        //session过期时间设置为7200秒 即两小时
    //        //session.setMaxInactiveInterval(60 * 60 * 2);
    //        return "redirect:/admin/index";
    //    } else {
    //        session.setAttribute("errorMsg", "登陆失败");
    //        return "admin/login";
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
