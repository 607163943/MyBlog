package com.blog.controller.admin;

import com.blog.annotation.OperationLog;
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
     * <p>
     * file 非必传：避免缺文件时被 Spring 抛异常后兜底成 500，改由 Service 返回 701/400。
     */
    @OperationLog(module = "upload", action = "上传图片", key = "#a0?.originalFilename")
    @PostMapping("/image")
    public Result<String> uploadImage(
            @RequestParam(value = "file", required = false) MultipartFile file) {
        return Result.success(uploadService.uploadImage(file));
    }
}
