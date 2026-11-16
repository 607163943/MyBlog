package com.blog.pojo.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图形验证码返回结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaVO {

    /**
     * 验证码标识，提交校验时回传
     */
    private String captchaKey;

    /**
     * 验证码图片，data:image/png;base64 格式
     */
    private String image;
}
