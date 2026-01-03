package com.my.blog.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("tb_sys_dict")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dict {
    // id
    private Long id;
    // 字典类型
    private String dictType;
    // 字典JSON
    private String dictJson;
    // 状态(0启用 1禁用)
    private Integer status;
    // 版本
    private Integer version;
    // 备注
    private String remark;
    // 创建时间
    private LocalDateTime createTime;
    // 修改时间
    private LocalDateTime updateTime;
}
