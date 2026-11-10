package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blog.pojo.po.Tag;
import com.blog.pojo.vo.TagVO;
import org.apache.ibatis.annotations.Param;

/**
 * 标签 Mapper
 */
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 分页查询标签，复杂查询下沉到 XML 实现
     */
    IPage<TagVO> selectTagPage(IPage<TagVO> page, @Param("name") String name);
}
