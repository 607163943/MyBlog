package com.site.blog.my.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@ApiModel("管理端基础VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseAdminVO {
    @ApiModelProperty("页面路径")
    private String path;
}
