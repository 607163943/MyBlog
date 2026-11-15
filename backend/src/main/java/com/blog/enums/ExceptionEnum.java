package com.blog.enums;

/**
 * 异常枚举统一契约
 *
 * <p>业务码规则：模块编号 + 该模块异常序号，如 101 表示 1 号模块（user）1 号异常，
 * 110 表示 1 号模块 10 号异常，210 表示 2 号模块（article）10 号异常。
 */
public interface ExceptionEnum {

    /**
     * 业务码
     */
    Integer getCode();

    /**
     * 异常消息
     */
    String getMsg();

    /**
     * HTTP 状态码
     */
    int getHttpCode();
}
