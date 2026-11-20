package com.blog.common;

/**
 * 公共常量
 */
public final class Constants {

    /**
     * 默认页码
     */
    public static final int DEFAULT_PAGE_NUM = 1;

    /**
     * 默认每页记录数
     */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 分页查询每页记录数上限
     */
    public static final int MAX_PAGE_SIZE = 50;

    /**
     * 一次性查询记录数上限
     */
    public static final int MAX_LIMIT = 50;

    /**
     * 文件存储类型：本地存储
     */
    public static final int FILE_STORAGE_TYPE_LOCAL = 0;

    /**
     * 文件引用业务类型：文章
     */
    public static final String FILE_BIZ_TYPE_ARTICLE = "article";

    /**
     * 文件引用业务类型：用户
     */
    public static final String FILE_BIZ_TYPE_USER = "user";

    private Constants() {
    }
}
