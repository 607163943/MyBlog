package com.site.blog.my.core.pojo.vo.user;

import com.site.blog.my.core.entity.BlogTagCount;
import com.site.blog.my.core.pojo.vo.SimpleBlogListVO;
import com.site.blog.my.core.common.utils.PageResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@ApiModel("首页博客数据")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogHomeVO {
    @ApiModelProperty("博客列表")
    private PageResult blogPageResult;
    @ApiModelProperty("最新博客")
    private List<SimpleBlogListVO> newBlogs;
    @ApiModelProperty("最热博客")
    private List<SimpleBlogListVO> hotBlogs;
    @ApiModelProperty("最热标签")
    private List<BlogTagCount> hotTags;
    @ApiModelProperty("页面名称")
    private String pageName;
    @ApiModelProperty("网站配置")
    private Map<String,String> configurations;
}
