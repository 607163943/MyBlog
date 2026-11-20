package com.blog.pojo.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件详情返回结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileAssetDetailVO {

    /**
     * 文件 ID
     */
    private Integer id;

    /**
     * 原始文件名
     */
    private String originalName;

    /**
     * MIME 类型
     */
    private String contentType;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 访问 URL；OSS 等暂不支持的类型为 null
     */
    private String url;
}
