package com.my.blog.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.common.result.PageResult;
import com.my.blog.common.utils.PageQueryUtils;
import com.my.blog.pojo.dto.admin.AdminDictValuePageQueryDTO;
import com.my.blog.pojo.po.DictValue;
import com.my.blog.pojo.vo.admin.AdminDictValuePageQueryVO;
import com.my.blog.server.mapper.DictValueMapper;
import com.my.blog.server.service.IDictValueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DictValueServiceImpl extends ServiceImpl<DictValueMapper, DictValue> implements IDictValueService {
    @Resource
    private PageQueryUtils pageQueryUtils;

    /**
     * 分页查询字典内容
     *
     * @param adminDictValuePageQueryDTO 查询条件
     * @return 分页结果
     */
    @Override
    public PageResult<AdminDictValuePageQueryVO> pageQuery(AdminDictValuePageQueryDTO adminDictValuePageQueryDTO) {
        // 参数校验和初始化
        pageQueryUtils.checkAndInitPageQuery(adminDictValuePageQueryDTO);

        // 构建分页条件
        Page<DictValue> page = new Page<>();
        page.setCurrent(adminDictValuePageQueryDTO.getPageNum());
        page.setSize(adminDictValuePageQueryDTO.getPageSize());

        // 查询
        page = lambdaQuery()
                .like(StrUtil.isNotEmpty(adminDictValuePageQueryDTO.getDictLabel()),
                        DictValue::getDictLabel,
                        adminDictValuePageQueryDTO.getDictLabel())
                .eq(DictValue::getDictId
                        , adminDictValuePageQueryDTO.getDictId())
                .page(page);

        // 构建VO数据
        List<AdminDictValuePageQueryVO> adminDictValuePageQueryVOS = BeanUtil.copyToList(page.getRecords(), AdminDictValuePageQueryVO.class);

        return PageResult.<AdminDictValuePageQueryVO>builder()
                .pageSize(page.getPages())
                .pageNum(page.getCurrent())
                .total(page.getTotal())
                .result(adminDictValuePageQueryVOS)
                .build();
    }
}
