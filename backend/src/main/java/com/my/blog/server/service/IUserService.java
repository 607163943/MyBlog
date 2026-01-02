package com.my.blog.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.pojo.dto.UserLoginDTO;
import com.my.blog.pojo.po.User;
import com.my.blog.pojo.vo.admin.CaptchaVO;
import com.my.blog.pojo.vo.admin.UserLoginVO;

public interface IUserService extends IService<User> {
    /**
     * 用户登录
     * @param userLoginDTO 用户登录信息
     * @return 用户信息+令牌
     */
    UserLoginVO login(UserLoginDTO userLoginDTO);

    /**
     * 获取验证码
     * @return 验证码数据
     */
    CaptchaVO captcha();
}
