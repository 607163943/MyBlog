package com.my.blog.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("首页数据统计")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeTotalVO {
    @ApiModelProperty("分类数量")
    private int categoryCount;
    @ApiModelProperty("博客数量")
    private int blogCount;
    @ApiModelProperty("标签数量")
    private int tagCount;
}
