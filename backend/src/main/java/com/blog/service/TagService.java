package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.pojo.dto.TagDTO;
import com.blog.pojo.dto.TagPageQueryDTO;
import com.blog.pojo.po.Tag;
import com.blog.pojo.vo.PageVO;
import com.blog.pojo.vo.TagVO;

import java.util.List;

/**
 * 标签服务
 */
public interface TagService extends IService<Tag> {

    /**
     * 分页查询标签
     */
    PageVO<TagVO> pageQuery(TagPageQueryDTO dto);

    /**
     * 根据 ID 查询标签
     */
    TagVO getTagById(Integer id);

    /**
     * 新增标签
     */
    void addTag(TagDTO dto);

    /**
     * 修改标签
     */
    void updateTag(TagDTO dto);

    /**
     * 删除单个标签
     */
    void deleteTag(Integer id);

    /**
     * 批量删除标签
     */
    void deleteTags(List<Integer> ids);
}
