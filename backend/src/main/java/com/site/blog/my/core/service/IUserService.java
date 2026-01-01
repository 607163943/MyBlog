package com.site.blog.my.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.site.blog.my.core.pojo.dto.UserLoginDTO;
import com.site.blog.my.core.pojo.po.User;
import com.site.blog.my.core.pojo.vo.admin.UserLoginVO;

public interface IUserService extends IService<User> {
    /**
     * 用户登录
     * @param userLoginDTO 用户登录信息
     * @return 用户信息+令牌
     */
    UserLoginVO login(UserLoginDTO userLoginDTO);
}
