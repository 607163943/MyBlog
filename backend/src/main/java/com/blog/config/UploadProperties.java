package com.blog.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * 图片上传配置
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "upload")
public class UploadProperties {

    public static final String DEFAULT_ROUTE_PREFIX = "/image";

    /**
     * 图片访问路由前缀，同时作为静态资源映射路径
     */
    private String routePrefix = DEFAULT_ROUTE_PREFIX;

    /**
     * 图片存储根目录
     */
    private String dir;

    /**
     * 归一化路由前缀：保证以 / 开头、不以 / 结尾；空值或 / 回退为 /image
     */
    public String getNormalizedRoutePrefix() {
        String prefix = StrUtil.trim(routePrefix);
        if (StrUtil.isEmpty(prefix)) {
            return DEFAULT_ROUTE_PREFIX;
        }
        if (!prefix.startsWith("/")) {
            prefix = "/" + prefix;
        }
        // 去除末尾斜杠
        while (prefix.length() > 1 && prefix.endsWith("/")) {
            prefix = prefix.substring(0, prefix.length() - 1);
        }
        // 避免只有斜杠
        return "/".equals(prefix) ? DEFAULT_ROUTE_PREFIX : prefix;
    }

    /**
     * 存储根目录的绝对路径；未配置时返回 null
     */
    public File getRootDir() {
        String path = StrUtil.trim(dir);
        if (StrUtil.isEmpty(path)) {
            return null;
        }
        return new File(path).getAbsoluteFile();
    }

    /**
     * 启动时尝试创建根目录。失败只记录日志，不阻断应用启动；
     * 真正上传时若目录不可用会返回业务异常。
     */
    @PostConstruct
    public void init() {
        File rootDir = getRootDir();
        if (rootDir == null) {
            log.warn("未配置 upload.dir，图片上传功能不可用");
            return;
        }
        try {
            File created = FileUtil.mkdir(rootDir);
            if (created == null || !created.isDirectory()) {
                log.warn("图片存储目录创建失败：{}", rootDir.getAbsolutePath());
            }
        } catch (Exception e) {
            log.warn("图片存储目录创建失败：{}", rootDir.getAbsolutePath(), e);
        }
    }
}
