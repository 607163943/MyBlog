package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blog.pojo.po.FileAsset;
import com.blog.pojo.vo.admin.FileAssetVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * 文件资源 Mapper
 */
public interface FileAssetMapper extends BaseMapper<FileAsset> {

    /**
     * 分页查询文件，并聚合引用次数
     */
    IPage<FileAssetVO> selectFileAssetPage(IPage<FileAssetVO> page,
                                 @Param("startTime") LocalDateTime startTime,
                                 @Param("endTime") LocalDateTime endTime);
}
