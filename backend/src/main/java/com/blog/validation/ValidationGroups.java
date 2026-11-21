package com.blog.validation;

import javax.validation.groups.Default;

/**
 * Bean 校验分组
 */
public interface ValidationGroups {

    /**
     * 修改场景：在默认校验之外，额外要求主键非空
     */
    interface Update extends Default {
    }
}
