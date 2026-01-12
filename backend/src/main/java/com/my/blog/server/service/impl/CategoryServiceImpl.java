package com.my.blog.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.common.constants.BizTypeConstant;
import com.my.blog.common.constants.UploadFileRefStatus;
import com.my.blog.common.enums.ExceptionEnums;
import com.my.blog.common.exception.admin.AdminCategoryException;
import com.my.blog.common.result.PageResult;
import com.my.blog.common.utils.PageQueryUtils;
import com.my.blog.pojo.dto.admin.AdminCategoryDTO;
import com.my.blog.pojo.dto.admin.AdminCategoryPageQueryDTO;
import com.my.blog.pojo.po.Category;
import com.my.blog.pojo.po.UploadFileRef;
import com.my.blog.pojo.vo.admin.AdminCategoryPageQueryVO;
import com.my.blog.pojo.vo.admin.RatioChartData;
import com.my.blog.server.mapper.CategoryMapper;
import com.my.blog.server.service.ICategoryService;
import com.my.blog.server.service.IUploadFileRefService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {
    @Resource
    private PageQueryUtils pageQueryUtils;

    @Resource
    private IUploadFileRefService uploadFileRefService;
    /**
     * 分页查询分类
     *
     * @param adminCategoryPageQueryDTO 查询条件
     * @return 分页结果
     */
    @Override
    public PageResult<AdminCategoryPageQueryVO> pageQuery(AdminCategoryPageQueryDTO adminCategoryPageQueryDTO) {
        // 参数校验和初始化
        pageQueryUtils.checkAndInitPageQuery(adminCategoryPageQueryDTO);

        // 构建分页条件
        Page<Category> page = new Page<>();
        page.setCurrent(adminCategoryPageQueryDTO.getPageNum());
        page.setSize(adminCategoryPageQueryDTO.getPageSize());

        // 查询
        LambdaQueryChainWrapper<Category> chainWrapper = lambdaQuery()
                .like(StrUtil.isNotEmpty(adminCategoryPageQueryDTO.getName()),
                        Category::getName,
                        adminCategoryPageQueryDTO.getName())
                .eq(adminCategoryPageQueryDTO.getStatus() != null,
                        Category::getStatus,
                        adminCategoryPageQueryDTO.getStatus());

        // sort升序查询
        if(adminCategoryPageQueryDTO.getIsAsc().equals(0)) {
            chainWrapper.orderByAsc(Category::getSort);
        }else {
            // sort降序查询
            chainWrapper.orderByDesc(Category::getSort);
        }

        // 查询
        page = chainWrapper.page(page);

        // 构建VO数据
        List<AdminCategoryPageQueryVO> adminCategoryPageQueryVOS = BeanUtil.copyToList(page.getRecords(), AdminCategoryPageQueryVO.class);

        return PageResult.<AdminCategoryPageQueryVO>builder()
                .pageSize(page.getPages())
                .pageNum(page.getCurrent())
                .total(page.getTotal())
                .result(adminCategoryPageQueryVOS)
                .build();
    }

    /**
     * 添加分类
     *
     * @param adminCategoryDTO 分类数据
     */
    @Transactional
    @Override
    public void addCategory(AdminCategoryDTO adminCategoryDTO) {
        // 检测是否存在同名分类
        Long count = lambdaQuery().eq(Category::getName, adminCategoryDTO.getName()).count();
        if(count>0) {
            throw new AdminCategoryException(ExceptionEnums.ADMIN_CATEGORY_EXIST);
        }

        Category category = BeanUtil.copyProperties(adminCategoryDTO, Category.class);
        save(category);

        // 标记引用文件状态为已使用，同时更新业务标记
        if(adminCategoryDTO.getUploadFileRefId()!=null) {
            UploadFileRef uploadFileRef = uploadFileRefService.lambdaQuery()
                    .eq(UploadFileRef::getId, adminCategoryDTO.getUploadFileRefId())
                    .one();

            if(uploadFileRef==null) {
                throw new AdminCategoryException(ExceptionEnums.ADMIN_CATEGORY_COVER_NOT_EXIST);
            }

            // 更新为使用
            uploadFileRef.setStatus(UploadFileRefStatus.USE);
            // 补充业务数据
            uploadFileRef.setBizType(BizTypeConstant.CATEGORY_COVER);
            uploadFileRef.setBizId(category.getId());

            uploadFileRefService.updateById(uploadFileRef);
        }
    }

    /**
     * 修改分类
     *
     * @param adminCategoryDTO 分类数据
     */
    @Transactional
    @Override
    public void updateCategory(AdminCategoryDTO adminCategoryDTO) {
        // 检测修改后是否存在同名标签
        Long count = lambdaQuery().eq(Category::getName, adminCategoryDTO.getName())
                .notIn(Category::getId, adminCategoryDTO.getId())
                .count();
        if(count>0) {
            throw new AdminCategoryException(ExceptionEnums.ADMIN_CATEGORY_EXIST);
        }

        Category category = BeanUtil.copyProperties(adminCategoryDTO, Category.class);
        updateById(category);

        // 标记引用文件状态为已使用，同时更新业务标记，旧文件标记为未使用
        if(adminCategoryDTO.getUploadFileRefId()!=null) {
            UploadFileRef uploadFileRef=uploadFileRefService.lambdaQuery()
                    .eq(UploadFileRef::getBizId,adminCategoryDTO.getId())
                    .one();
            // 旧文件标记为未使用
            if(uploadFileRef!=null) {
                uploadFileRef.setStatus(UploadFileRefStatus.NOT_USE);
                uploadFileRefService.updateById(uploadFileRef);
            }

            // 标记引用文件状态为已使用，同时更新业务标记
            uploadFileRef = uploadFileRefService.lambdaQuery()
                    .eq(UploadFileRef::getId, adminCategoryDTO.getUploadFileRefId())
                    .one();

            if(uploadFileRef==null) {
                throw new AdminCategoryException(ExceptionEnums.ADMIN_CATEGORY_COVER_NOT_EXIST);
            }

            // 更新为使用
            uploadFileRef.setStatus(UploadFileRefStatus.USE);
            // 补充业务数据
            uploadFileRef.setBizType(BizTypeConstant.CATEGORY_COVER);
            uploadFileRef.setBizId(category.getId());

            uploadFileRefService.updateById(uploadFileRef);
        }
    }

    /**
     * 修改分类状态
     * @param id 分类id
     */
    @Override
    public void updateStatus(Long id) {
        Category category = getById(id);
        category.setStatus(category.getStatus() == 0 ? 1 : 0);
        updateById(category);
    }

    /**
     * 统计分类状态
     * @return 分类状态统计数据
     */
    @Override
    public List<RatioChartData> countGroupByStatus() {
        return baseMapper.countGroupByStatus();
    }
}
