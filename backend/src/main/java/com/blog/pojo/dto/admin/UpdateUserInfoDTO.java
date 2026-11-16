package com.blog.pojo.dto.admin;

import lombok.Data;

/**
 * 修改当前用户基本信息入参
 */
@Data
public class UpdateUserInfoDTO {

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 邮箱地址，可为空
     */
    private String email;

    /**
     * 头像图片地址，可为空
     */
    private String avatar;
}
