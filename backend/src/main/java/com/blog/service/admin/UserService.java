package com.blog.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.pojo.dto.admin.UserLoginDTO;
import com.blog.pojo.po.User;
import com.blog.pojo.vo.admin.LoginVO;
import com.blog.pojo.vo.admin.UserInfoVO;

/**
 * 用户服务
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     */
    LoginVO login(UserLoginDTO dto);

    /**
     * 退出登录
     */
    void logout();

    /**
     * 获取当前登录用户信息
     */
    UserInfoVO getUserInfo();
}
