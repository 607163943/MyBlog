package com.blog.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 文章总浏览量每日快照实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_view_snapshot")
public class ViewSnapshot {

    /**
     * 快照记录 ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 快照日期（自然日），一个自然日只保留一行
     */
    private LocalDate snapshotDate;

    /**
     * 截至快照时刻的已发布文章总浏览量
     */
    private Long totalViewCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
