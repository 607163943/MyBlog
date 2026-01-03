package com.my.blog.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.common.result.PageResult;
import com.my.blog.pojo.dto.admin.AdminDictValuePageQueryDTO;
import com.my.blog.pojo.po.DictValue;
import com.my.blog.pojo.vo.admin.AdminDictValuePageQueryVO;

public interface IDictValueService extends IService<DictValue> {
    /**
     * 分页查询字典内容
     * @param adminDictValuePageQueryDTO 查询条件
     * @return 分页结果
     */
    PageResult<AdminDictValuePageQueryVO> pageQuery(AdminDictValuePageQueryDTO adminDictValuePageQueryDTO);
}
