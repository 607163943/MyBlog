package com.blog.service.admin.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.common.Constants;
import com.blog.config.UploadProperties;
import com.blog.enums.FileExceptionEnum;
import com.blog.exception.BusinessException;
import com.blog.mapper.FileAssetMapper;
import com.blog.mapper.FileRefMapper;
import com.blog.pojo.dto.PageQueryDTO;
import com.blog.pojo.dto.admin.FileAssetPageQueryDTO;
import com.blog.pojo.po.FileAsset;
import com.blog.pojo.po.FileRef;
import com.blog.pojo.vo.PageVO;
import com.blog.pojo.vo.admin.FileAssetDetailVO;
import com.blog.pojo.vo.admin.FileAssetUploadVO;
import com.blog.pojo.vo.admin.FileAssetVO;
import com.blog.service.admin.FileAssetService;
import com.blog.util.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 文件服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileAssetServiceImpl extends ServiceImpl<FileAssetMapper, FileAsset> implements FileAssetService {

    /**
     * 对象键最大长度，与 tb_file_asset.object_key 列长度一致
     */
    private static final int MAX_OBJECT_KEY_LENGTH = 255;

    /**
     * 原始文件名最大长度，与 tb_file_asset.original_name 列长度一致
     */
    private static final int MAX_ORIGINAL_NAME_LENGTH = 255;

    /**
     * MIME 类型最大长度，与 tb_file_asset.content_type 列长度一致
     */
    private static final int MAX_CONTENT_TYPE_LENGTH = 100;

    /**
     * Markdown 图片：![alt](url "title")，URL 允许被尖括号包裹
     */
    private static final Pattern MARKDOWN_IMAGE_PATTERN = Pattern.compile(
            "!\\[[^\\]]*]\\(\\s*<?([^)\\s>]+)>?(?:\\s+[\"'][^\"']*[\"'])?\\s*\\)");

    /**
     * HTML 图片：<img src="url">，支持单引号、双引号与无引号
     */
    private static final Pattern HTML_IMAGE_PATTERN = Pattern.compile(
            "<img\\b[^>]*?\\bsrc\\s*=\\s*(?:\"([^\"]*)\"|'([^']*)'|([^\\s>]+))",
            Pattern.CASE_INSENSITIVE);

    private final FileRefMapper fileRefMapper;

    private final UploadProperties uploadProperties;

    @Override
    public PageVO<FileAssetVO> pageQuery(FileAssetPageQueryDTO dto) {
        PageQueryDTO query = PageUtil.checkPage(dto);
        if (dto.getStartDate() != null && dto.getEndDate() != null
                && dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new BusinessException(FileExceptionEnum.FILE_DATE_RANGE_INVALID);
        }

        LocalDateTime startTime = dto.getStartDate() == null
                ? null
                : dto.getStartDate().atStartOfDay();
        // 结束日期含当天
        LocalDateTime endTime = dto.getEndDate() == null
                ? null
                : dto.getEndDate().plusDays(1).atStartOfDay();

        Page<FileAssetVO> page = new Page<>(query.getPage(), query.getPageSize());
        IPage<FileAssetVO> pageResult = baseMapper.selectFileAssetPage(page, startTime, endTime);

        PageVO<FileAssetVO> pageVO = new PageVO<>();
        pageVO.setRecords(pageResult.getRecords());
        pageVO.setTotal(pageResult.getTotal());
        pageVO.setSize(pageResult.getSize());
        pageVO.setPages(pageResult.getPages());
        return pageVO;
    }

    @Override
    public FileAssetDetailVO getFileAssetById(Integer id) {
        FileAsset fileAsset = id == null ? null : getById(id);
        if (fileAsset == null) {
            throw new BusinessException(FileExceptionEnum.FILE_NOT_FOUND);
        }
        return new FileAssetDetailVO(fileAsset.getId(), fileAsset.getOriginalName(),
                fileAsset.getContentType(), fileAsset.getFileSize(), buildUrl(fileAsset));
    }

    @Override
    public FileAssetUploadVO registerLocalFileAsset(String objectKey, String originalName,
                                          String contentType, long fileSize, Integer uploaderId) {
        if (StrUtil.isBlank(objectKey) || StrUtil.isBlank(originalName) || uploaderId == null) {
            throw new BusinessException(FileExceptionEnum.FILE_METADATA_INVALID);
        }

        FileAsset fileAsset = new FileAsset();
        fileAsset.setStorageType(Constants.FILE_STORAGE_TYPE_LOCAL);
        fileAsset.setBucket("");
        fileAsset.setObjectKey(truncate(objectKey, MAX_OBJECT_KEY_LENGTH));
        fileAsset.setOriginalName(truncate(originalName, MAX_ORIGINAL_NAME_LENGTH));
        fileAsset.setContentType(
                truncate(StrUtil.blankToDefault(contentType, ""), MAX_CONTENT_TYPE_LENGTH));
        fileAsset.setFileSize(Math.max(fileSize, 0L));
        fileAsset.setUploaderId(uploaderId);
        save(fileAsset);
        return new FileAssetUploadVO(fileAsset.getId(), buildUrl(fileAsset));
    }

    @Override
    public Integer resolveFileAssetIdByUrl(String url) {
        String objectKey = extractLocalObjectKey(url);
        if (objectKey == null) {
            return null;
        }
        FileAsset fileAsset = getOne(new LambdaQueryWrapper<FileAsset>()
                .eq(FileAsset::getStorageType, Constants.FILE_STORAGE_TYPE_LOCAL)
                .eq(FileAsset::getBucket, "")
                .eq(FileAsset::getObjectKey, objectKey), false);
        return fileAsset == null ? null : fileAsset.getId();
    }

    @Override
    public List<Integer> resolveFileAssetIdsByContent(String content) {
        if (StrUtil.isBlank(content)) {
            return Collections.emptyList();
        }

        Set<String> objectKeys = new LinkedHashSet<>();
        Matcher markdownMatcher = MARKDOWN_IMAGE_PATTERN.matcher(content);
        while (markdownMatcher.find()) {
            addObjectKey(objectKeys, markdownMatcher.group(1));
        }
        Matcher htmlMatcher = HTML_IMAGE_PATTERN.matcher(content);
        while (htmlMatcher.find()) {
            String url = htmlMatcher.group(1) != null
                    ? htmlMatcher.group(1)
                    : htmlMatcher.group(2) != null ? htmlMatcher.group(2) : htmlMatcher.group(3);
            addObjectKey(objectKeys, url);
        }
        if (objectKeys.isEmpty()) {
            return Collections.emptyList();
        }

        List<FileAsset> fileAssets = list(new LambdaQueryWrapper<FileAsset>()
                .eq(FileAsset::getStorageType, Constants.FILE_STORAGE_TYPE_LOCAL)
                .eq(FileAsset::getBucket, "")
                .in(FileAsset::getObjectKey, objectKeys));
        Map<String, Integer> objectKeyToId = fileAssets.stream()
                .collect(Collectors.toMap(FileAsset::getObjectKey, FileAsset::getId, (a, b) -> a));
        return objectKeys.stream()
                .map(objectKeyToId::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public String buildUrl(FileAsset fileAsset) {
        if (fileAsset == null
                || !Objects.equals(fileAsset.getStorageType(), Constants.FILE_STORAGE_TYPE_LOCAL)
                || StrUtil.isBlank(fileAsset.getObjectKey())) {
            return null;
        }
        return UploadProperties.ROUTE_PREFIX + "/" + fileAsset.getObjectKey();
    }

    @Override
    public void deleteFileAsset(Integer id) {
        FileAsset fileAsset = id == null ? null : getById(id);
        if (fileAsset == null) {
            throw new BusinessException(FileExceptionEnum.FILE_NOT_FOUND);
        }

        Long refCount = fileRefMapper.selectCount(new LambdaQueryWrapper<FileRef>()
                .eq(FileRef::getFileId, id));
        if (refCount != null && refCount > 0) {
            throw new BusinessException(FileExceptionEnum.FILE_REFERENCED);
        }

        if (Objects.equals(fileAsset.getStorageType(), Constants.FILE_STORAGE_TYPE_LOCAL)) {
            deleteLocalObject(fileAsset.getObjectKey());
        }
        if (!removeById(id)) {
            throw new BusinessException(FileExceptionEnum.FILE_NOT_FOUND);
        }
    }

    /**
     * 提取本地存储对象键：仅接受固定的 /image 前缀，去掉查询串与锚点
     */
    private String extractLocalObjectKey(String url) {
        String trimmed = url == null ? "" : url.trim();
        String prefix = UploadProperties.ROUTE_PREFIX + "/";
        if (!trimmed.startsWith(prefix)) {
            return null;
        }
        String objectKey = trimmed.substring(prefix.length());
        // 将链接中可能附带的?或者#后缀进行清理
        int parameterIndex = indexOfQueryOrFragment(objectKey);
        if (parameterIndex >= 0) {
            objectKey = objectKey.substring(0, parameterIndex);
        }
        return StrUtil.isBlank(objectKey) ? null : objectKey;
    }

    /**
     * 返回查询串 ? 或锚点 # 的最早下标，都不存在时返回 -1
     */
    private int indexOfQueryOrFragment(String value) {
        int queryIndex = value.indexOf('?');
        int fragmentIndex = value.indexOf('#');
        if (queryIndex < 0) {
            return fragmentIndex;
        }
        if (fragmentIndex < 0) {
            return queryIndex;
        }
        return Math.min(queryIndex, fragmentIndex);
    }

    private void addObjectKey(Set<String> objectKeys, String url) {
        String objectKey = extractLocalObjectKey(url);
        if (objectKey != null) {
            objectKeys.add(objectKey);
        }
    }

    /**
     * 删除本地物理文件；失败时保留数据库记录以便重试
     */
    private void deleteLocalObject(String objectKey) {
        if (StrUtil.isBlank(objectKey) || uploadProperties.getRootDir() == null) {
            throw new BusinessException(FileExceptionEnum.FILE_DELETE_IO_ERROR);
        }
        Path path = uploadProperties.getRootDir().toPath().resolve(objectKey);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.error("删除物理文件失败：{}", path, e);
            throw new BusinessException(FileExceptionEnum.FILE_DELETE_IO_ERROR);
        }
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }
}
