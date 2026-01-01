package com.site.blog.my.core.security;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.site.blog.my.core.common.utils.JWTUtils;
import com.site.blog.my.core.pojo.po.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class LoginRealm extends AuthenticatingRealm {
    @Resource
    private JWTUtils jwtUtils;
    @Override
    public boolean supports(AuthenticationToken token) {
        // 只处理自定义认证令牌
        return token instanceof JWTToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JWTToken jwtToken = (JWTToken) authenticationToken;
        String token= (String) jwtToken.getCredentials();

        // 获取登录用户id
        Long userId = jwtUtils.getUserId(token);

        if(userId==null) {
            return null;
        }

        // 获取用户数据
        User user = Db.lambdaQuery(User.class).eq(User::getId, userId).one();

        return new SimpleAuthenticationInfo(user, token, getName());
    }
}
