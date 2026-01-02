package com.my.blog.pojo.vo;

import com.my.blog.pojo.entity.BlogCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel("文章编辑VO")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleEditVO{

    @ApiModelProperty("文章分类列表")
    private List<BlogCategory> categories;
}
