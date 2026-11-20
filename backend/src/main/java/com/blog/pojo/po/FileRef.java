package com.blog.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 文件引用实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_file_ref")
public class FileRef {

    /**
     * 引用记录 ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 文件 ID
     */
    private Integer fileId;

    /**
     * 业务类型：article-文章，user-用户
     */
    private String bizType;

    /**
     * 业务数据 ID
     */
    private Integer bizId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
