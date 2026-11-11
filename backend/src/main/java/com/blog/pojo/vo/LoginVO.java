package com.blog.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录返回结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO {

    /**
     * token 名称（前端请求时作为请求头名称）
     */
    private String tokenName;

    /**
     * token 值
     */
    private String tokenValue;
}
