package com.my.blog.pojo.dto.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@ApiModel("字典值分页查询参数")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDictValuePageQueryDTO extends PageQueryDTO {
    @ApiModelProperty("字典内容标签")
    private String dictLabel;
    @ApiModelProperty("字典id")
    @NotNull(message = "字典id不能为空")
    private Long dictId;
}
