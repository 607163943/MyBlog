package com.my.blog.pojo.dto.admin;

import com.my.blog.server.config.valid.UpdateValidGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel("添加/修改字典参数")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDictDTO {
    @ApiModelProperty("id")
    // 编辑时需要校验
    @NotNull(message = "id不能为空", groups = UpdateValidGroup.class)
    private Long id;
    @ApiModelProperty("字典类型")
    @NotEmpty(message = "字典类型不能为空")
    private String dictType;
    @ApiModelProperty("状态 0启用 1禁用")
    @NotNull(message = "状态不能为空")
    private Integer status;
    @ApiModelProperty("备注")
    @NotEmpty(message = "备注不能为空")
    private String remark;
}
