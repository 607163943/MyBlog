package com.blog.pojo.dto.admin;

import lombok.Data;

/**
 * 修改当前用户密码入参
 */
@Data
public class ChangePasswordDTO {

    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 图形验证码标识
     */
    private String captchaKey;

    /**
     * 图形验证码内容
     */
    private String captchaCode;
}
