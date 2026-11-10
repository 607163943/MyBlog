package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.exception.BusinessException;
import com.blog.mapper.TagMapper;
import com.blog.pojo.dto.TagDTO;
import com.blog.pojo.dto.TagPageQueryDTO;
import com.blog.pojo.po.Tag;
import com.blog.pojo.vo.PageVO;
import com.blog.pojo.vo.TagVO;
import com.blog.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 标签服务实现
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;

    @Override
    public PageVO<TagVO> pageQuery(TagPageQueryDTO dto) {
        int pageNum = dto.getPage() == null || dto.getPage() < 1 ? DEFAULT_PAGE_NUM : dto.getPage();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? DEFAULT_PAGE_SIZE : dto.getPageSize();

        Page<TagVO> page = new Page<>(pageNum, pageSize);
        IPage<TagVO> pageResult = baseMapper.selectTagPage(page, dto.getName());

        PageVO<TagVO> pageVO = new PageVO<>();
        pageVO.setRecords(pageResult.getRecords());
        pageVO.setTotal(pageResult.getTotal());
        pageVO.setSize(pageResult.getSize());
        pageVO.setPages(pageResult.getPages());
        return pageVO;
    }

    @Override
    public TagVO getTagById(Integer id) {
        if (id == null) {
            throw new BusinessException("标签不存在");
        }
        Tag tag = getById(id);
        if (tag == null) {
            throw new BusinessException("标签不存在");
        }
        TagVO tagVO = new TagVO();
        BeanUtils.copyProperties(tag, tagVO);
        return tagVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTag(TagDTO dto) {
        String name = normalizeName(dto.getName());
        checkNameUnique(name, null);

        Tag tag = new Tag();
        tag.setName(name);
        save(tag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTag(TagDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("标签ID不能为空");
        }
        Tag existing = getById(dto.getId());
        if (existing == null) {
            throw new BusinessException("标签不存在");
        }
        String name = normalizeName(dto.getName());
        checkNameUnique(name, dto.getId());

        Tag tag = new Tag();
        tag.setId(dto.getId());
        tag.setName(name);
        updateById(tag);
    }

    @Override
    public void deleteTag(Integer id) {
        if (id == null || !removeById(id)) {
            throw new BusinessException("标签不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTags(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("请选择要删除的标签");
        }
        removeByIds(ids);
    }

    /**
     * 去除名称首尾空格并校验非空
     */
    private String normalizeName(String name) {
        String trimmed = name == null ? "" : name.trim();
        if (trimmed.isEmpty()) {
            throw new BusinessException("标签名称不能为空");
        }
        return trimmed;
    }

    /**
     * 校验标签名称唯一
     */
    private void checkNameUnique(String name, Integer excludeId) {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<Tag>().eq(Tag::getName, name);
        if (excludeId != null) {
            wrapper.ne(Tag::getId, excludeId);
        }
        if (count(wrapper) > 0) {
            throw new BusinessException("标签名称已存在");
        }
    }
}
