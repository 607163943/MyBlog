package com.blog.service.admin;

import org.springframework.web.multipart.MultipartFile;

/**
 * 图片上传服务
 */
public interface UploadService {

    /**
     * 上传图片
     *
     * @param file 图片文件
     * @return 可直接访问的相对 URL，如 /image/2026/11/15/uuid.png
     */
    String uploadImage(MultipartFile file);
}
