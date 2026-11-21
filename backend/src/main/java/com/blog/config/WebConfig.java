package com.blog.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Web 配置：Sa-Token 登录校验与图片静态资源映射
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final UploadProperties uploadProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 仅校验后台管理接口的登录状态，登录接口放行；前台 /web/** 接口公开
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/user/login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        File rootDir = uploadProperties.getRootDir();
        if (rootDir == null) {
            log.warn("未配置 upload.dir，跳过图片资源映射");
            return;
        }

        // 目录 location 必须以 / 结尾，否则 Spring 会把最后一段当成文件名
        String location = rootDir.toURI().toString();
        if (!location.endsWith("/")) {
            location = location + "/";
        }

        registry.addResourceHandler(UploadProperties.ROUTE_PREFIX + "/**")
                .addResourceLocations(location);
    }
}
