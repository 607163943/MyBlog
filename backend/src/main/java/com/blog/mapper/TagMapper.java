package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blog.pojo.po.Tag;
import com.blog.pojo.vo.admin.ArticleTagNameVO;
import com.blog.pojo.vo.admin.TagVO;
import com.blog.pojo.vo.web.WebArticleTagVO;
import com.blog.pojo.vo.web.WebTagVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标签 Mapper
 */
public interface TagMapper extends BaseMapper<Tag> {

    /* ==================== 后台管理 ==================== */

    /**
     * 分页查询标签，复杂查询下沉到 XML 实现
     */
    IPage<TagVO> selectTagPage(IPage<TagVO> page, @Param("name") String name);

    /**
     * 查询文章标签名称，供排行结果回填
     */
    List<ArticleTagNameVO> selectTagNamesByArticleIds(@Param("articleIds") List<Integer> articleIds);

    /* ==================== 前台展示 ==================== */

    /**
     * 前台查询全部标签及其已发布文章数
     */
    List<WebTagVO> selectWebTagList();

    /**
     * 前台根据 ID 查询标签及其已发布文章数
     */
    WebTagVO selectWebTagById(@Param("id") Integer id);

    /**
     * 批量查询文章关联的标签
     */
    List<WebArticleTagVO> selectTagsByArticleIds(@Param("articleIds") List<Integer> articleIds);
}
