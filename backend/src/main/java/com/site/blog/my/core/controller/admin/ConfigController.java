package com.site.blog.my.core.controller.admin;

import com.site.blog.my.core.result.Result;
import com.site.blog.my.core.service.ConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@Api(tags = "配置管理")
@RestController
@RequestMapping("/admin/config")
public class ConfigController {
    @Resource
    private ConfigService configService;

    @ApiOperation("获取配置列表")
    @GetMapping("/list")
    public Result<Map<String,String>> list() {
        return Result.success(configService.getAllConfigs());
    }
}
