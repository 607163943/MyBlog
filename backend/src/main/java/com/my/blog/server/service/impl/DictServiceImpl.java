package com.my.blog.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.common.enums.ExceptionEnums;
import com.my.blog.common.exception.admin.AdminDictException;
import com.my.blog.common.result.PageResult;
import com.my.blog.common.utils.PageQueryUtils;
import com.my.blog.pojo.dto.admin.AdminDictDTO;
import com.my.blog.pojo.dto.admin.AdminDictPageQueryDTO;
import com.my.blog.pojo.po.Dict;
import com.my.blog.pojo.vo.admin.AdminDictPageQueryVO;
import com.my.blog.server.mapper.DictMapper;
import com.my.blog.server.service.IDictService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {
    @Resource
    private PageQueryUtils pageQueryUtils;
    /**
     * 分页查询字典
     *
     * @param adminDictPageQueryDTO 查询条件
     * @return 分页结果
     */
    @Override
    public PageResult<AdminDictPageQueryVO> pageQuery(AdminDictPageQueryDTO adminDictPageQueryDTO) {
        // 参数校验和初始化
        pageQueryUtils.checkAndInitPageQuery(adminDictPageQueryDTO);

        // 构建分页条件
        Page<Dict> page = new Page<>();
        page.setCurrent(adminDictPageQueryDTO.getPageNum());
        page.setSize(adminDictPageQueryDTO.getPageSize());

        // 查询
        page = lambdaQuery()
                .like(StrUtil.isNotEmpty(adminDictPageQueryDTO.getDictType()),
                        Dict::getDictType,
                        adminDictPageQueryDTO.getDictType())
                .eq(adminDictPageQueryDTO.getStatus() != null,
                        Dict::getStatus,
                        adminDictPageQueryDTO.getStatus())
                .page(page);

        // 构建VO数据
        List<AdminDictPageQueryVO> adminDictPageQueryVOS = BeanUtil.copyToList(page.getRecords(), AdminDictPageQueryVO.class);

        return PageResult.<AdminDictPageQueryVO>builder()
                .pageSize(page.getPages())
                .pageNum(page.getCurrent())
                .total(page.getTotal())
                .result(adminDictPageQueryVOS)
                .build();
    }

    /**
     * 添加字典
     *
     * @param adminDictDTO 字典数据
     */
    @Override
    public void addDict(AdminDictDTO adminDictDTO) {
        // 检测是否存在同名字典
        Long count = lambdaQuery().eq(Dict::getDictType, adminDictDTO.getDictType()).count();
        if(count>0) {
            throw new AdminDictException(ExceptionEnums.ADMIN_DICT_EXIST);
        }

        Dict dict = BeanUtil.copyProperties(adminDictDTO, Dict.class);
        // 新字典提供空json串即可
        dict.setDictJson("{}");
        save(dict);
    }

    /**
     * 修改字典
     *
     * @param adminDictDTO 字典数据
     */
    @Override
    public void updateDict(AdminDictDTO adminDictDTO) {
        // 检测修改后是否存在同名字典
        Long count = lambdaQuery().eq(Dict::getDictType, adminDictDTO.getDictType())
                .notIn(Dict::getId, adminDictDTO.getId())
                .count();
        if(count>0) {
            throw new AdminDictException(ExceptionEnums.ADMIN_DICT_EXIST);
        }

        Dict dict = BeanUtil.copyProperties(adminDictDTO, Dict.class);
        updateById(dict);
    }

    /**
     * 修改字典状态
     * @param id 字典id
     */
    @Override
    public void updateStatus(Long id) {
        Dict dict = getById(id);
        dict.setStatus(dict.getStatus() == 0 ? 1 : 0);
        updateById(dict);
    }
}
