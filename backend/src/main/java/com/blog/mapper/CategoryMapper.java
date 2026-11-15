package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blog.pojo.po.Category;
import com.blog.pojo.vo.admin.CategoryRankVO;
import com.blog.pojo.vo.admin.CategoryVO;
import com.blog.pojo.vo.web.WebCategoryHotArticleVO;
import com.blog.pojo.vo.web.WebCategoryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分类 Mapper
 */
public interface CategoryMapper extends BaseMapper<Category> {

    /* ==================== 后台管理 ==================== */

    /**
     * 分页查询分类，复杂查询下沉到 XML 实现
     */
    IPage<CategoryVO> selectCategoryPage(IPage<CategoryVO> page, @Param("name") String name);

    /**
     * 分页查询分类文章数排行
     */
    IPage<CategoryRankVO> selectCategoryRankPage(IPage<CategoryRankVO> page,
                                                 @Param("name") String name,
                                                 @Param("minCount") Integer minCount,
                                                 @Param("maxCount") Integer maxCount);

    /* ==================== 前台展示 ==================== */

    /**
     * 前台查询全部分类及其已发布文章数
     */
    List<WebCategoryVO> selectWebCategoryList();

    /**
     * 前台根据 ID 查询分类及其已发布文章数
     */
    WebCategoryVO selectWebCategoryById(@Param("id") Integer id);

    /**
     * 查询指定分类下浏览量 Top N 的已发布文章
     */
    List<WebCategoryHotArticleVO> selectTopArticlesByCategoryIds(
            @Param("categoryIds") List<Integer> categoryIds,
            @Param("limit") Integer limit);
}
