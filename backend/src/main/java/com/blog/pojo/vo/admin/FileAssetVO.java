package com.blog.pojo.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 文件返回结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileAssetVO {

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
     * 上传时间
     */
    private LocalDateTime createTime;

    /**
     * 引用次数
     */
    private Long refCount;
}
