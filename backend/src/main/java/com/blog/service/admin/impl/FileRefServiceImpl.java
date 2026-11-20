package com.blog.service.admin.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.mapper.FileRefMapper;
import com.blog.pojo.po.FileRef;
import com.blog.service.admin.FileRefService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 文件引用服务实现
 */
@Service
public class FileRefServiceImpl extends ServiceImpl<FileRefMapper, FileRef>
        implements FileRefService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replaceRefs(String bizType, Integer bizId, Collection<Integer> fileIds) {
        if (StrUtil.isBlank(bizType) || bizId == null) {
            return;
        }
        remove(new LambdaQueryWrapper<FileRef>()
                .eq(FileRef::getBizType, bizType)
                .eq(FileRef::getBizId, bizId));
        if (fileIds == null || fileIds.isEmpty()) {
            return;
        }

        Set<Integer> distinctFileIds = fileIds.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        for (Integer fileId : distinctFileIds) {
            FileRef ref = new FileRef();
            ref.setFileId(fileId);
            ref.setBizType(bizType);
            ref.setBizId(bizId);
            save(ref);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRefs(String bizType, Integer bizId) {
        if (StrUtil.isBlank(bizType) || bizId == null) {
            return;
        }
        remove(new LambdaQueryWrapper<FileRef>()
                .eq(FileRef::getBizType, bizType)
                .eq(FileRef::getBizId, bizId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRefs(String bizType, Collection<Integer> bizIds) {
        if (StrUtil.isBlank(bizType) || bizIds == null || bizIds.isEmpty()) {
            return;
        }
        List<Integer> distinctBizIds = bizIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (distinctBizIds.isEmpty()) {
            return;
        }
        remove(new LambdaQueryWrapper<FileRef>()
                .eq(FileRef::getBizType, bizType)
                .in(FileRef::getBizId, distinctBizIds));
    }
}
