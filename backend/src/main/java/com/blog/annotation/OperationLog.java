package com.blog.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志注解：标注在 Controller 方法上，由 OperationLogAspect 统一记录。
 * <p>
 * key 为 SpEL 表达式，可通过 {@code #a0}、{@code #a1} 引用方法参数（从 0 开始），
 * 例如 {@code "#a0.id"}、{@code "#a0.username"}；不配置时不记录业务参数，
 * 避免密码、邮箱等敏感信息写入日志。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /**
     * 模块名，如 user / article / comment / upload
     */
    String module();

    /**
     * 操作名，如 登录 / 删除文章
     */
    String action();

    /**
     * 关键业务参数的 SpEL 表达式，如 "#a0"、"#a0.id"、"#a0.username"
     */
    String key() default "";
}
