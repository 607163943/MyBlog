package com.blog.controller.admin;

import com.blog.common.Result;
import com.blog.service.admin.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口
 */
@RestController
@RequestMapping("/admin/upload")
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    /**
     * 上传图片，返回可直接访问的相对 URL
     */
    @PostMapping("/image")
    public Result<String> uploadImage(
            @RequestParam(value = "file", required = false) MultipartFile file) {
        return Result.success(uploadService.uploadImage(file));
    }
}
