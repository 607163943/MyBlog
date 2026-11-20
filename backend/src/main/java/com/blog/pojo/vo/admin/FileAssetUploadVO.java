package com.blog.pojo.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件上传返回结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileAssetUploadVO {

    /**
     * 文件 ID
     */
    private Integer fileId;

    /**
     * 可直接访问的相对 URL
     */
    private String url;
}
