package com.blog.pojo.dto.admin;

import com.blog.validation.ValidationGroups;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 文章新增 / 修改参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {

    /**
     * 文章 ID，修改时必填
     */
    @NotNull(message = "文章ID不能为空", groups = ValidationGroups.Update.class)
    private Integer id;

    /**
     * 文章标题
     */
    @NotBlank(message = "文章标题不能为空")
    @Size(max = 200, message = "文章标题不能超过 200 个字符")
    private String title;

    /**
     * 文章描述 / 摘要
     */
    private String description;

    /**
     * 文章正文（Markdown 格式）
     */
    @NotBlank(message = "文章正文不能为空")
    private String content;

    /**
     * 封面图片地址
     */
    private String cover;

    /**
     * 封面文件 ID，为空时按上传接口返回的 URL 反查
     */
    private Integer coverFileId;

    /**
     * 所属分类 ID：null 或 0 表示无分类
     */
    private Integer categoryId;

    /**
     * 分类内排序值：null 表示未填写，由服务端按现有规则处理
     */
    @Min(value = 1, message = "分类内排序值必须为正整数")
    private Integer categorySort;

    /**
     * 标签 ID 列表
     */
    private List<Integer> tagIds;

    /**
     * 文章状态：0-草稿，1-已发布
     */
    @Min(value = 0, message = "文章状态不正确")
    @Max(value = 1, message = "文章状态不正确")
    private Integer status;
}
