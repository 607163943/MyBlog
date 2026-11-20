package com.blog.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.pojo.dto.admin.FileAssetPageQueryDTO;
import com.blog.pojo.po.FileAsset;
import com.blog.pojo.vo.PageVO;
import com.blog.pojo.vo.admin.FileAssetDetailVO;
import com.blog.pojo.vo.admin.FileAssetUploadVO;
import com.blog.pojo.vo.admin.FileAssetVO;

import java.util.List;

/**
 * 文件服务
 */
public interface FileAssetService extends IService<FileAsset> {

    /**
     * 分页查询文件，按上传日期范围过滤
     */
    PageVO<FileAssetVO> pageQuery(FileAssetPageQueryDTO dto);

    /**
     * 根据 ID 查询文件，返回访问 URL
     */
    FileAssetDetailVO getFileAssetById(Integer id);

    /**
     * 登记本地存储文件，返回文件 ID 与访问 URL
     */
    FileAssetUploadVO registerLocalFileAsset(String objectKey, String originalName,
                                   String contentType, long fileSize, Integer uploaderId);

    /**
     * 本地 /image URL 反查文件 ID；外链或未登记返回 null
     */
    Integer resolveFileAssetIdByUrl(String url);

    /**
     * 从 Markdown / HTML 正文中提取本地图片并反查文件 ID，按首次出现顺序去重
     */
    List<Integer> resolveFileAssetIdsByContent(String content);

    /**
     * 拼接文件访问 URL；OSS 等暂不支持的类型返回 null
     */
    String buildUrl(FileAsset file);

    /**
     * 删除文件：存在引用时拒绝；本地存储同时删除物理文件
     */
    void deleteFileAsset(Integer id);
}
