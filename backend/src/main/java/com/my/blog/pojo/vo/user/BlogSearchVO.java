package com.my.blog.pojo.vo.user;

import com.my.blog.pojo.entity.BlogTagCount;
import com.my.blog.pojo.vo.SimpleBlogListVO;
import com.my.blog.common.utils.PageResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@ApiModel("博客标签")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogSearchVO {
    @ApiModelProperty("博客标签列表")
    private PageResult blogPageResult;
    @ApiModelProperty("页面名称")
    private String pageName;
    @ApiModelProperty("页面url")
    private String pageUrl;
    @ApiModelProperty("关键字")
    private String keyword;
    @ApiModelProperty("最新博客列表")
    private List<SimpleBlogListVO> newBlogs;
    @ApiModelProperty("最热博客列表")
    private List<SimpleBlogListVO> hotBlogs;
    @ApiModelProperty("最热标签列表")
    private List<BlogTagCount>hotTags;
    @ApiModelProperty("网站配置")
    private Map<String,String> configurations;
}
