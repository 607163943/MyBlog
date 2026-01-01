package com.site.blog.my.core.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("用户登录数据")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDTO {
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("验证码")
    private String captchaCode;
}
