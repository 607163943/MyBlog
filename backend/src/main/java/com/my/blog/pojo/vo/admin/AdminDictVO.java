package com.my.blog.pojo.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@ApiModel("字典VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDictVO {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("字典类型")
    private String dictType;
    @ApiModelProperty("状态 0启用 1禁用")
    private Integer status;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
}
