package com.my.blog.server.filter;

import cn.hutool.core.util.StrUtil;
import com.my.blog.common.utils.JWTUtils;
import com.my.blog.common.utils.URLUtils;
import com.my.blog.server.security.JWTToken;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
@Component
public class LoginFilter extends AuthenticatingFilter {

    @Resource
    private JWTUtils jwtUtils;

    @Resource
    private URLUtils urlUtils;

    public static final String Filter_NAME="login_filter";
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        // 获取token
        HttpServletRequest httpServletRequest = WebUtils.toHttp(servletRequest);
        String token = httpServletRequest.getHeader("token");

        // 判空
        if(StrUtil.isEmpty(token)) {
            return null;
        }

        // 校验
        try{
            jwtUtils.verifyJWT(token);
        }catch (Exception e) {
            return null;
        }

        return JWTToken.builder()
                .token(token)
                .build();
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        // 所有拦截请求都走认证
        return executeLogin(servletRequest, servletResponse);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        String uri = httpServletRequest.getRequestURI();

        // 根据路径配置判断是否放行
        return urlUtils.isExcludePath(uri);
    }
}
