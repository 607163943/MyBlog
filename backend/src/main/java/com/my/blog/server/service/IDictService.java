package com.my.blog.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.common.result.PageResult;
import com.my.blog.pojo.dto.admin.AdminDictDTO;
import com.my.blog.pojo.dto.admin.AdminDictPageQueryDTO;
import com.my.blog.pojo.po.Dict;
import com.my.blog.pojo.vo.admin.AdminDictPageQueryVO;

public interface IDictService extends IService<Dict> {
    /**
     * 分页查询字典
     * @param adminDictPageQueryDTO 查询条件
     * @return 分页结果
     */
    PageResult<AdminDictPageQueryVO> pageQuery(AdminDictPageQueryDTO adminDictPageQueryDTO);

    /**
     * 新增字典
     * @param adminDictDTO 字典数据
     */
    void addDict(AdminDictDTO adminDictDTO);

    /**
     * 修改字典
     * @param adminDictDTO 字典数据
     */
    void updateDict(AdminDictDTO adminDictDTO);
}
