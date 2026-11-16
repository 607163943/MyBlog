package com.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 图片上传模块异常枚举（模块编号 7）
 */
@Getter
@AllArgsConstructor
public enum UploadExceptionEnum implements ExceptionEnum {

    /**
     * 未选择文件或文件为空
     */
    UPLOAD_FILE_EMPTY(701, "请选择要上传的图片", 400),

    /**
     * 原始文件名为空、无后缀或无法提取文件名
     */
    UPLOAD_FILE_NAME_INVALID(702, "图片文件名不合法", 400),

    /**
     * Content-Type 不在白名单
     */
    UPLOAD_CONTENT_TYPE_UNSUPPORTED(703, "不支持的图片 Content-Type", 415),

    /**
     * 文件后缀不在白名单
     */
    UPLOAD_EXTENSION_UNSUPPORTED(704, "仅支持 jpg、jpeg、png、gif、webp、bmp 格式的图片", 400),

    /**
     * 魔数识别失败，或魔数与后缀不一致
     */
    UPLOAD_CONTENT_MISMATCH(705, "图片内容与文件类型不匹配", 400),

    /**
     * 读取文件头、创建目录或写入文件失败
     */
    UPLOAD_IO_ERROR(706, "图片读写失败，请稍后重试", 500),

    /**
     * 超过单文件大小限制
     */
    UPLOAD_FILE_TOO_LARGE(707, "图片大小不能超过 5MB", 413),

    /**
     * 其他 multipart 请求解析错误
     */
    UPLOAD_MULTIPART_INVALID(708, "上传请求格式不正确", 400),

    /**
     * 未配置 upload.dir
     */
    UPLOAD_DIR_NOT_CONFIGURED(709, "图片存储目录未配置", 500);

    private final Integer code;

    private final String msg;

    private final int httpCode;
}
