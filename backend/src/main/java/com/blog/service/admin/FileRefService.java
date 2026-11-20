package com.blog.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.pojo.po.FileRef;

import java.util.Collection;

/**
 * 文件引用服务
 */
public interface FileRefService extends IService<FileRef> {

    /**
     * 整组重建业务数据的文件引用：先删除旧引用，再写入本次使用的全部文件
     */
    void replaceRefs(String bizType, Integer bizId, Collection<Integer> fileIds);

    /**
     * 删除单个业务数据的全部文件引用
     */
    void deleteRefs(String bizType, Integer bizId);

    /**
     * 批量删除业务数据的全部文件引用
     */
    void deleteRefs(String bizType, Collection<Integer> bizIds);
}
