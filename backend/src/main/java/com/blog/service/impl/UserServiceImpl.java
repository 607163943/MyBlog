package com.blog.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.exception.BusinessException;
import com.blog.mapper.UserMapper;
import com.blog.pojo.dto.UserLoginDTO;
import com.blog.pojo.po.User;
import com.blog.pojo.vo.LoginVO;
import com.blog.pojo.vo.UserInfoVO;
import com.blog.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public LoginVO login(UserLoginDTO dto) {
        String username = dto.getUsername() == null ? "" : dto.getUsername().trim();
        String password = dto.getPassword() == null ? "" : dto.getPassword();
        if (username.isEmpty() || password.isEmpty()) {
            throw new BusinessException("用户名和密码不能为空");
        }

        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        // 当前阶段密码暂用明文比对
        if (user == null || !password.equals(user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        StpUtil.login(user.getId());
        return new LoginVO(StpUtil.getTokenName(), StpUtil.getTokenValue());
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }

    @Override
    public UserInfoVO getUserInfo() {
        User user = getById(StpUtil.getLoginIdAsInt());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);
        return userInfoVO;
    }
}
