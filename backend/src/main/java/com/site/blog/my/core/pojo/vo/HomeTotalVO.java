package com.site.blog.my.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@ApiModel("首页数据统计")
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeTotalVO extends BaseAdminVO {
    @ApiModelProperty("分类数量")
    private int categoryCount;
    @ApiModelProperty("博客数量")
    private int blogCount;
    @ApiModelProperty("友链数量")
    private int linkCount;
    @ApiModelProperty("标签数量")
    private int tagCount;
    @ApiModelProperty("评论数量")
    private int commentCount;
}
