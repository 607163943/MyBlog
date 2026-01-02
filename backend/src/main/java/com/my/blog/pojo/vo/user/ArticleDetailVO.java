package com.my.blog.pojo.vo.user;

import com.my.blog.pojo.vo.BlogDetailVO;
import com.my.blog.common.utils.PageResult;
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
