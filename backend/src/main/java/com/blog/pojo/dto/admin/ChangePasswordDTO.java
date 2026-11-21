package com.blog.pojo.dto.admin;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 修改当前用户密码入参
 */
@Data
public class ChangePasswordDTO {

    /**
     * 旧密码
     */
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码长度需为 6-20 个字符")
    @Size(min = 6, max = 20, message = "新密码长度需为 6-20 个字符")
    private String newPassword;

    /**
     * 图形验证码标识
     */
    @NotBlank(message = "验证码不能为空")
    private String captchaKey;

    /**
     * 图形验证码内容
     */
    @NotBlank(message = "验证码不能为空")
    private String captchaCode;
}
