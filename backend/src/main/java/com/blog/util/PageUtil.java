package com.blog.util;

import com.blog.common.Constants;
import com.blog.pojo.dto.PageQueryDTO;

/**
 * 分页参数工具
 */
public final class PageUtil {

    private PageUtil() {
    }

    /**
     * 校验并修正分页参数：页码、每页条数缺省或小于 1 时取默认值。
     * 直接在传入的 DTO 上修改属性值，并原样返回。
     *
     * @param dto 分页查询参数
     * @return 修正后的分页查询参数
     */
    public static PageQueryDTO checkPage(PageQueryDTO dto) {
        if (dto == null) {
            return new PageQueryDTO();
        }
        if (dto.getPage() == null || dto.getPage() < 1) {
            dto.setPage(Constants.DEFAULT_PAGE_NUM);
        }
        if (dto.getPageSize() == null || dto.getPageSize() < 1) {
            dto.setPageSize(Constants.DEFAULT_PAGE_SIZE);
        }
        return dto;
    }

    /**
     * 在 {@link #checkPage(PageQueryDTO)} 基础上追加每页条数上限校验。
     *
     * @param dto         分页查询参数
     * @param maxPageSize 每页条数上限
     * @return 修正后的分页查询参数
     */
    public static PageQueryDTO checkPage(PageQueryDTO dto, int maxPageSize) {
        PageQueryDTO query = checkPage(dto);
        if (query.getPageSize() > maxPageSize) {
            query.setPageSize(maxPageSize);
        }
        return query;
    }
}
