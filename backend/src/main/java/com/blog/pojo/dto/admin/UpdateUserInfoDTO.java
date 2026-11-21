package com.blog.pojo.dto.admin;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 修改当前用户基本信息入参
 */
@Data
public class UpdateUserInfoDTO {

    /**
     * 用户昵称
     */
    @NotBlank(message = "昵称不能为空")
    @Size(max = 20, message = "昵称长度不能超过 20 个字符")
    private String nickname;

    /**
     * 邮箱地址，可为空
     */
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱格式不正确")
    private String email;

    /**
     * 头像图片地址，可为空
     */
    @Size(max = 255, message = "头像地址长度不能超过 255 个字符")
    private String avatar;

    /**
     * 头像文件 ID，为空时按上传接口返回的 URL 反查
     */
    private Integer avatarFileId;
}
