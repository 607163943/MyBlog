package com.blog.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.enums.TagExceptionEnum;
import com.blog.exception.BusinessException;
import com.blog.mapper.TagMapper;
import com.blog.pojo.dto.admin.TagDTO;
import com.blog.pojo.dto.PageQueryDTO;
import com.blog.pojo.dto.admin.TagPageQueryDTO;
import com.blog.pojo.po.Tag;
import com.blog.pojo.vo.PageVO;
import com.blog.pojo.vo.admin.TagVO;
import com.blog.service.admin.TagService;
import com.blog.util.PageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 标签服务实现
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public PageVO<TagVO> pageQuery(TagPageQueryDTO dto) {
        PageQueryDTO query = PageUtil.checkPage(dto);

        Page<TagVO> page = new Page<>(query.getPage(), query.getPageSize());
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
            throw new BusinessException(TagExceptionEnum.TAG_NOT_FOUND);
        }
        Tag tag = getById(id);
        if (tag == null) {
            throw new BusinessException(TagExceptionEnum.TAG_NOT_FOUND);
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
        Tag existing = getById(dto.getId());
        if (existing == null) {
            throw new BusinessException(TagExceptionEnum.TAG_NOT_FOUND);
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
            throw new BusinessException(TagExceptionEnum.TAG_NOT_FOUND);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTags(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(TagExceptionEnum.TAG_DELETE_IDS_REQUIRED);
        }
        removeByIds(ids);
    }

    /**
     * 去除名称首尾空格
     */
    private String normalizeName(String name) {
        return name.trim();
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
            throw new BusinessException(TagExceptionEnum.TAG_NAME_EXISTS);
        }
    }
}
