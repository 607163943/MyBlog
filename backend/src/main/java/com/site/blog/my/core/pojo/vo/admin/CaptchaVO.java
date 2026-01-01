package com.site.blog.my.core.pojo.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("验证码")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaVO {
    @ApiModelProperty("验证码图片的base64编码")
    private String imageBase64;
}
