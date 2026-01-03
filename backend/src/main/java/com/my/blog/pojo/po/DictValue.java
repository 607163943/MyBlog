package com.my.blog.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("tb_sys_dict_value")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictValue {
    // id
    private Long id;
    // 字典id
    private Long dictId;
    // 字典标签
    private String dictLabel;
    // 字典值
    private Integer dictValue;
    // 创建时间
    private LocalDateTime createTime;
    // 修改时间
    private LocalDateTime updateTime;
}
