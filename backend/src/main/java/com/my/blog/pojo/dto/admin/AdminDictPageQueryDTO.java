package com.my.blog.pojo.dto.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@ApiModel("字典分页查询参数")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDictPageQueryDTO extends PageQueryDTO {
    @ApiModelProperty("字典类型")
    private String dictType;
    @ApiModelProperty("字典状态 0启用 1禁用")
    private Integer status;
}
