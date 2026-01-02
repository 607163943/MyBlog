package com.my.blog.pojo.vo.user;

import com.my.blog.pojo.vo.BlogDetailVO;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Api("关于页面数据")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogAboutVO {
    private BlogDetailVO blogDetailVO;
    private String pageName;
    private Map<String,String> configurations;
}
