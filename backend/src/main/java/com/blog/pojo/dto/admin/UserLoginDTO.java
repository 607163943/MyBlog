package com.blog.pojo.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

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
    @NotBlank(message = "用户名和密码不能为空")
    private String username;

    /**
     * 登录密码（当前阶段为明文）
     */
    @NotBlank(message = "用户名和密码不能为空")
    private String password;
}
