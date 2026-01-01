package com.site.blog.my.core.pojo.vo.user;

import com.site.blog.my.core.pojo.vo.BlogDetailVO;
import com.site.blog.my.core.common.utils.PageResult;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@ApiModel("文章详情")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDetailVO {
    private BlogDetailVO blogDetailVO;
    private PageResult commentPageResult;
    private String pageName;
    private Map<String,String> configurations;
}
