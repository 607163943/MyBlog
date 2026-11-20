package com.blog.service.admin.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.blog.config.UploadProperties;
import com.blog.enums.UploadExceptionEnum;
import com.blog.exception.BusinessException;
import com.blog.pojo.vo.admin.FileAssetUploadVO;
import com.blog.service.admin.FileAssetService;
import com.blog.service.admin.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * 图片上传服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {

    /**
     * 单张图片最大 5MB，与 spring.servlet.multipart.max-file-size 保持一致
     */
    private static final long MAX_IMAGE_SIZE = 5L * 1024 * 1024;

    /**
     * 允许的 Content-Type
     */
    private static final Set<String> ALLOWED_CONTENT_TYPES = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(
                    "image/jpeg",
                    "image/png",
                    "image/gif",
                    "image/webp",
                    "image/bmp")));

    /**
     * 允许的文件后缀
     */
    private static final Set<String> ALLOWED_EXTENSIONS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList("jpg", "jpeg", "png", "gif", "webp", "bmp")));

    /**
     * HuTool 魔数识别结果 -> 落盘扩展名。
     * APNG 本质是 PNG，统一按 png 存储，避免合法的 .png APNG 被误拒。
     */
    private static final Map<String, String> MAGIC_EXTENSIONS = new HashMap<>();

    static {
        MAGIC_EXTENSIONS.put("jpg", "jpg");
        MAGIC_EXTENSIONS.put("png", "png");
        MAGIC_EXTENSIONS.put("apng", "png");
        MAGIC_EXTENSIONS.put("gif", "gif");
        MAGIC_EXTENSIONS.put("webp", "webp");
        MAGIC_EXTENSIONS.put("bmp", "bmp");
    }

    private final UploadProperties uploadProperties;

    private final FileAssetService fileAssetService;

    @Override
    public FileAssetUploadVO uploadImage(MultipartFile file) {
        // 1. 空文件 / 缺少 file 字段
        if (file == null || file.isEmpty()) {
            throw new BusinessException(UploadExceptionEnum.UPLOAD_FILE_EMPTY);
        }

        // 2. 大小兜底。正常情况下 Spring multipart 限制会先拦截
        if (file.getSize() > MAX_IMAGE_SIZE) {
            throw new BusinessException(UploadExceptionEnum.UPLOAD_FILE_TOO_LARGE);
        }

        // 3. 原始文件名与后缀
        String originalName = resolveOriginalName(file.getOriginalFilename());
        String extension = resolveExtension(originalName);

        // 4. Content-Type
        String contentType = normalizeContentType(file.getContentType());
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new BusinessException(UploadExceptionEnum.UPLOAD_CONTENT_TYPE_UNSUPPORTED);
        }

        // 5. 魔数识别
        String storedExtension = detectExtension(file);

        // 6. 魔数与后缀一致性
        if (!normalizeExtension(extension).equals(storedExtension)) {
            throw new BusinessException(UploadExceptionEnum.UPLOAD_CONTENT_MISMATCH);
        }

        // 7. 鉴权信息必须在写盘前获取，避免落库失败时留下无法归属的文件
        Integer uploaderId = StpUtil.getLoginIdAsInt();

        // 8. 落盘后登记文件资源；落库失败时补偿删除物理文件
        String objectKey = store(file, storedExtension);
        try {
            FileAssetUploadVO uploadVO = fileAssetService.registerLocalFileAsset(
                    objectKey, originalName, contentType, file.getSize(), uploaderId);
            log.info("图片上传成功：{}（{} 字节）", uploadVO.getUrl(), file.getSize());
            return uploadVO;
        } catch (RuntimeException e) {
            deleteStoredObject(objectKey);
            throw e;
        }
    }

    /**
     * 原始文件名只取最后一段，避免把浏览器提交的路径写入数据库
     */
    private String resolveOriginalName(String originalFilename) {
        if (StrUtil.isBlank(originalFilename)) {
            throw new BusinessException(UploadExceptionEnum.UPLOAD_FILE_NAME_INVALID);
        }
        String filename = FileUtil.getName(originalFilename.trim());
        if (StrUtil.isBlank(filename)) {
            throw new BusinessException(UploadExceptionEnum.UPLOAD_FILE_NAME_INVALID);
        }
        return filename;
    }

    /**
     * 只取原始文件名的最后一段，原始路径不参与落盘；无后缀直接拒绝
     */
    private String resolveExtension(String originalFilename) {
        if (StrUtil.isBlank(originalFilename)) {
            throw new BusinessException(UploadExceptionEnum.UPLOAD_FILE_NAME_INVALID);
        }
        String filename = FileUtil.getName(originalFilename.trim());
        if (StrUtil.isBlank(filename)) {
            throw new BusinessException(UploadExceptionEnum.UPLOAD_FILE_NAME_INVALID);
        }
        String extension = FileUtil.extName(filename);
        if (StrUtil.isBlank(extension)) {
            throw new BusinessException(UploadExceptionEnum.UPLOAD_FILE_NAME_INVALID);
        }
        extension = extension.toLowerCase(Locale.ROOT);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BusinessException(UploadExceptionEnum.UPLOAD_EXTENSION_UNSUPPORTED);
        }
        return extension;
    }

    /**
     * jpg/jpeg 统一为 jpg；其他保持原样
     */
    private String normalizeExtension(String extension) {
        return "jpeg".equals(extension) ? "jpg" : extension;
    }

    /**
     * 去掉 Content-Type 参数并转小写
     */
    private String normalizeContentType(String contentType) {
        if (StrUtil.isBlank(contentType)) {
            return "";
        }
        int parameterIndex = contentType.indexOf(';');
        String value = parameterIndex >= 0 ? contentType.substring(0, parameterIndex) : contentType;
        return value.trim().toLowerCase(Locale.ROOT);
    }

    /**
     * 使用 HuTool 纯魔数识别，未知返回 null。
     * 注意不能使用 getType(InputStream, fileName)，它在识别失败时会回退到文件后缀。
     */
    private String detectExtension(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            String magic = FileTypeUtil.getType(inputStream);
            String extension = magic == null
                    ? null
                    : MAGIC_EXTENSIONS.get(magic.toLowerCase(Locale.ROOT));
            if (extension == null) {
                throw new BusinessException(UploadExceptionEnum.UPLOAD_CONTENT_MISMATCH);
            }
            return extension;
        } catch (IOException | IORuntimeException e) {
            log.error("读取图片文件头失败", e);
            throw new BusinessException(UploadExceptionEnum.UPLOAD_IO_ERROR);
        }
    }

    private String store(MultipartFile file, String extension) {
        File rootDir = uploadProperties.getRootDir();
        if (rootDir == null) {
            throw new BusinessException(UploadExceptionEnum.UPLOAD_DIR_NOT_CONFIGURED);
        }

        // 目录按 JVM 默认时区生成，格式 yyyy/MM/dd
        LocalDate today = LocalDate.now();
        String relativeDir = String.format(Locale.ROOT, "%04d/%02d/%02d",
                today.getYear(), today.getMonthValue(), today.getDayOfMonth());

        File targetDir = new File(rootDir, relativeDir);
        File createdDir;
        try {
            createdDir = FileUtil.mkdir(targetDir);
        } catch (IORuntimeException e) {
            log.error("图片目录创建失败：{}", targetDir.getAbsolutePath(), e);
            throw new BusinessException(UploadExceptionEnum.UPLOAD_IO_ERROR);
        }
        if (createdDir == null || !createdDir.isDirectory()) {
            log.error("图片目录创建失败：{}", targetDir.getAbsolutePath());
            throw new BusinessException(UploadExceptionEnum.UPLOAD_IO_ERROR);
        }

        String filename = IdUtil.simpleUUID() + "." + extension;
        File targetFile = new File(targetDir, filename);
        try {
            // transferTo 必须使用绝对路径；父目录已创建；UUID 基本不存在覆盖风险
            file.transferTo(targetFile.getAbsoluteFile());
        } catch (IOException | IllegalStateException e) {
            FileUtil.del(targetFile);
            log.error("图片保存失败：{}", targetFile.getAbsolutePath(), e);
            throw new BusinessException(UploadExceptionEnum.UPLOAD_IO_ERROR);
        }

        return relativeDir + "/" + filename;
    }

    /**
     * 落库失败后清理已写入的物理文件
     */
    private void deleteStoredObject(String objectKey) {
        File rootDir = uploadProperties.getRootDir();
        if (rootDir == null) {
            return;
        }
        File targetFile = new File(rootDir, objectKey);
        try {
            if (!FileUtil.del(targetFile)) {
                log.warn("文件登记失败后清理物理文件失败：{}", targetFile.getAbsolutePath());
            }
        } catch (IORuntimeException e) {
            log.warn("文件登记失败后清理物理文件异常：{}", targetFile.getAbsolutePath(), e);
        }
    }
}
