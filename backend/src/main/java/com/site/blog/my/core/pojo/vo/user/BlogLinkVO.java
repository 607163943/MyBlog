package com.site.blog.my.core.pojo.vo.user;

import com.site.blog.my.core.entity.BlogLink;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@ApiModel("友情链接数据")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogLinkVO {
    @ApiModelProperty("页面名称")
    private String pageName;
    @ApiModelProperty("友情链接")
    private List<BlogLink> favoriteLinks;
    @ApiModelProperty("推荐链接")
    private List<BlogLink> recommendLinks;
    @ApiModelProperty("个人网站")
    private List<BlogLink> personalLinks;
    @ApiModelProperty("网站配置")
    private Map<String,String> configurations;
}
