package com.site.blog.my.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.site.blog.my.core.common.utils.MD5Util;
import com.site.blog.my.core.mapper.UserMapper;
import com.site.blog.my.core.pojo.dto.UserLoginDTO;
import com.site.blog.my.core.pojo.po.User;
import com.site.blog.my.core.pojo.vo.admin.UserInfo;
import com.site.blog.my.core.pojo.vo.admin.UserLoginVO;
import com.site.blog.my.core.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    /**
     * 用户登录
     * @param userLoginDTO 用户登录信息
     * @return 用户信息+令牌
     */
    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        // 验证码校验
        // 需要借助Redis 进行验证码校验，这里先判空
        if(StrUtil.isEmpty(userLoginDTO.getCaptchaCode())) {
            return null;
        }

        // 用户名校验
        if(StrUtil.isEmpty(userLoginDTO.getUsername())) {
            return null;
        }
        User user = lambdaQuery().eq(User::getUsername, userLoginDTO.getUsername()).one();
        if(user==null) {
            return null;
        }

        // 密码校验
        if(StrUtil.isEmpty(userLoginDTO.getPassword())) {
            return null;
        }
        String passwordMd5 = MD5Util.MD5Encode(userLoginDTO.getPassword(), "UTF-8");
        if(!passwordMd5.equals(user.getPassword())) {
            return null;
        }
        // 用户信息
        UserInfo userInfo = BeanUtil.copyProperties(user, UserInfo.class);

        // 令牌
        // TODO：需要制作令牌
        String token="";

        return UserLoginVO.builder()
                .token(token)
                .userInfo(userInfo)
                .build();
    }
}
