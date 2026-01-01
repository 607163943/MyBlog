package com.site.blog.my.core.controller.admin;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.site.blog.my.core.common.result.Result;
import com.site.blog.my.core.pojo.vo.admin.CaptchaVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "验证码")
@RestController
@RequestMapping("/admin/captcha")
public class CaptchaController {
    /**
     * 获取验证码
     * @return 验证码数据
     */
    @ApiOperation("获取验证码")
    @GetMapping
    public Result<CaptchaVO> captcha() {
        ShearCaptcha shearCaptcha= CaptchaUtil.createShearCaptcha(150, 30, 4, 2);

        String imageBase64 = shearCaptcha.getImageBase64();

        CaptchaVO captchaVO = CaptchaVO.builder()
                .imageBase64(imageBase64)
                .build();

        return Result.success(captchaVO);

    }
}
