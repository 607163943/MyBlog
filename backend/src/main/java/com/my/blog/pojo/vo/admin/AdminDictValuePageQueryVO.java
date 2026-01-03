package com.my.blog.pojo.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("字典值分页查询结果")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDictValuePageQueryVO {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("字典id")
    private Long dictId;
    @ApiModelProperty("字典值")
    private Integer dictValue;
    @ApiModelProperty("字典标签")
    private String dictLabel;
}
