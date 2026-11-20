package com.blog.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 文件资源实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_file_asset")
public class FileAsset {

    /**
     * 文件 ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 存储类型：0-本地存储，1-OSS
     */
    private Integer storageType;

    /**
     * OSS Bucket 名称；本地存储为空串
     */
    private String bucket;

    /**
     * 存储对象键：本地为相对存储根目录的路径，不含 /image 前缀
     */
    private String objectKey;

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
     * 上传用户 ID
     */
    private Integer uploaderId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
