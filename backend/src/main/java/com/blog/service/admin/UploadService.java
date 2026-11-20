package com.blog.service.admin;

import com.blog.pojo.vo.admin.FileAssetUploadVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片上传服务
 */
public interface UploadService {

    /**
     * 上传图片
     *
     * @param file 图片文件
     * @return 文件 ID 与可直接访问的相对 URL
     */
    FileAssetUploadVO uploadImage(MultipartFile file);
}
