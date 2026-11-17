package com.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文章模块异常枚举（模块编号 2）
 */
@Getter
@AllArgsConstructor
public enum ArticleExceptionEnum implements ExceptionEnum {

    /**
     * 文章不存在
     */
    ARTICLE_NOT_FOUND(201, "文章不存在", 404),

    /**
     * 文章不存在或未发布
     */
    ARTICLE_NOT_PUBLISHED(202, "文章不存在或未发布", 404),

    /**
     * 文章 ID 为空
     */
    ARTICLE_ID_REQUIRED(203, "文章ID不能为空", 400),

    /**
     * 批量删除时未选择文章
     */
    ARTICLE_DELETE_IDS_REQUIRED(204, "请选择要删除的文章", 400),

    /**
     * 文章标题为空
     */
    ARTICLE_TITLE_REQUIRED(205, "文章标题不能为空", 400),

    /**
     * 文章标题超长
     */
    ARTICLE_TITLE_TOO_LONG(206, "文章标题不能超过 200 个字符", 400),

    /**
     * 文章正文为空
     */
    ARTICLE_CONTENT_REQUIRED(207, "文章正文不能为空", 400),

    /**
     * 文章状态取值不正确
     */
    ARTICLE_STATUS_INVALID(209, "文章状态不正确", 400),

    /**
     * 归档年份不正确
     */
    ARTICLE_YEAR_INVALID(210, "年份不正确", 400),

    /**
     * 归档月份不正确
     */
    ARTICLE_MONTH_INVALID(211, "月份不正确", 400),

    /**
     * 分类内排序值不合法
     */
    ARTICLE_CATEGORY_SORT_INVALID(212, "分类内排序值必须为正整数", 400),

    /**
     * 分类内排序值重复
     */
    ARTICLE_CATEGORY_SORT_DUPLICATED(213, "该分类下已存在相同的排序值", 409),

    /**
     * 批量调整分类内排序时未提交文章
     */
    ARTICLE_CATEGORY_SORT_IDS_REQUIRED(214, "请选择要排序的文章", 400),

    /**
     * 批量调整分类内排序时文章集合已变化
     */
    ARTICLE_CATEGORY_SORT_STALE(215, "分类内排序数据已变化，请刷新后重试", 409);

    private final Integer code;

    private final String msg;

    private final int httpCode;
}
