package com.blog.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDTO {

    /**
     * 登录用户名
     */
    private String username;

    /**
     * 登录密码（当前阶段为明文）
     */
    private String password;
}
