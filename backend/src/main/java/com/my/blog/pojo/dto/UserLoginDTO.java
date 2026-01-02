package com.my.blog.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@ApiModel("用户登录数据")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDTO {
    @ApiModelProperty("用户名")
    @NotEmpty(message = "用户名不能为空")
    private String username;

    @ApiModelProperty("密码")
    @NotEmpty(message = "密码不能为空")
    private String password;

    @ApiModelProperty("验证码")
    @NotEmpty(message = "验证码不能为空")
    private String captchaCode;

    @ApiModelProperty("验证码key")
    @NotEmpty(message = "验证码key不能为空")
    private String captchaKey;
}
