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

    /**
     * 图片访问路由前缀，同时作为静态资源映射路径
     */
    public static final String ROUTE_PREFIX = "/image";

    /**
     * 图片存储根目录
     */
    private String dir;

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
            // 返回的就是rootDir本身
            File created = FileUtil.mkdir(rootDir);
            if (!created.isDirectory()) {
                throw new RuntimeException("图片存储目录创建失败");
            }
        } catch (Exception e) {
            log.warn("图片存储目录创建失败：{}", rootDir.getAbsolutePath(), e);
        }
    }
}
