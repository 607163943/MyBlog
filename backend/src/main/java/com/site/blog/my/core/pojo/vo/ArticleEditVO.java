package com.site.blog.my.core.pojo.vo;

import com.site.blog.my.core.entity.BlogCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ApiModel("文章编辑VO")
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleEditVO extends BaseAdminVO {

    @ApiModelProperty("文章分类列表")
    private List<BlogCategory> categories;
}
