package com.my.blog.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.common.enums.ExceptionEnums;
import com.my.blog.common.exception.admin.AdminTagException;
import com.my.blog.common.result.PageResult;
import com.my.blog.common.utils.PageQueryUtils;
import com.my.blog.pojo.dto.admin.AdminTagDTO;
import com.my.blog.pojo.dto.admin.AdminTagPageQueryDTO;
import com.my.blog.pojo.po.Tag;
import com.my.blog.pojo.vo.admin.AdminTagPageQueryVO;
import com.my.blog.pojo.vo.admin.RatioChartData;
import com.my.blog.server.mapper.TagMapper;
import com.my.blog.server.service.ITagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {
    @Resource
    private PageQueryUtils pageQueryUtils;
    /**
     * 分页查询标签
     *
     * @param adminTagPageQueryDTO 查询条件
     * @return 分页结果
     */
    @Override
    public PageResult<AdminTagPageQueryVO> pageQuery(AdminTagPageQueryDTO adminTagPageQueryDTO) {
        // 参数校验和初始化
        pageQueryUtils.checkAndInitPageQuery(adminTagPageQueryDTO);

        // 构建分页条件
        Page<Tag> page = new Page<>();
        page.setCurrent(adminTagPageQueryDTO.getPageNum());
        page.setSize(adminTagPageQueryDTO.getPageSize());

        // 查询
        page = lambdaQuery()
                .like(StrUtil.isNotEmpty(adminTagPageQueryDTO.getName()),
                        Tag::getName,
                        adminTagPageQueryDTO.getName())
                .eq(adminTagPageQueryDTO.getStatus() != null,
                        Tag::getStatus,
                        adminTagPageQueryDTO.getStatus())
                .page(page);

        // 构建VO数据
        List<AdminTagPageQueryVO> adminTagPageQueryVOS = BeanUtil.copyToList(page.getRecords(), AdminTagPageQueryVO.class);

        return PageResult.<AdminTagPageQueryVO>builder()
                .pageSize(page.getPages())
                .pageNum(page.getCurrent())
                .total(page.getTotal())
                .result(adminTagPageQueryVOS)
                .build();
    }

    /**
     * 添加标签
     *
      * @param adminTagDTO 标签数据
     */
    @Override
    public void addTag(AdminTagDTO adminTagDTO) {
        // 检测是否存在同名标签
        Long count = lambdaQuery().eq(Tag::getName, adminTagDTO.getName()).count();
        if(count>0) {
            throw new AdminTagException(ExceptionEnums.ADMIN_TAG_EXIST);
        }

        Tag tag = BeanUtil.copyProperties(adminTagDTO, Tag.class);
        save(tag);
    }

    /**
     * 修改标签
     *
     * @param adminTagDTO 标签数据
     */
    @Override
    public void updateTag(AdminTagDTO adminTagDTO) {
        // 检测修改后是否存在同名标签
        Long count = lambdaQuery().eq(Tag::getName, adminTagDTO.getName())
                .notIn(Tag::getId, adminTagDTO.getId())
                .count();
        if(count>0) {
            throw new AdminTagException(ExceptionEnums.ADMIN_TAG_EXIST);
        }

        Tag tag = BeanUtil.copyProperties(adminTagDTO, Tag.class);
        updateById(tag);
    }

    /**
     * 修改标签状态
     * @param id 标签id
     */
    @Override
    public void updateStatus(Long id) {
        Tag tag = getById(id);
        tag.setStatus(tag.getStatus() == 0 ? 1 : 0);
        updateById(tag);
    }

    /**
     * 统计标签状态
     * @return 统计结果
     */
    @Override
    public List<RatioChartData> countGroupByStatus() {
        return baseMapper.countGroupByStatus();
    }
}
