package com.my.blog.server.mapper;

import com.my.blog.pojo.entity.BlogCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlogCategoryMapper {

    int insertSelective(BlogCategory record);

    BlogCategory selectByPrimaryKey(Integer categoryId);

    BlogCategory selectByCategoryName(String categoryName);

    int updateByPrimaryKeySelective(BlogCategory record);

    int updateByPrimaryKey(BlogCategory record);


    List<BlogCategory> selectByCategoryIds(@Param("categoryIds") List<Integer> categoryIds);

}