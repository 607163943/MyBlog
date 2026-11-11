package com.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.exception.BusinessException;
import com.blog.mapper.DashboardMapper;
import com.blog.pojo.dto.ArticleRankQueryDTO;
import com.blog.pojo.dto.CategoryRankQueryDTO;
import com.blog.pojo.vo.ArticleRankVO;
import com.blog.pojo.vo.ArticleTagNameVO;
import com.blog.pojo.vo.CategoryRankVO;
import com.blog.pojo.vo.DashboardStatisticsVO;
import com.blog.pojo.vo.PageVO;
import com.blog.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据总览服务实现
 */
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;

    private final DashboardMapper dashboardMapper;

    @Override
    @Transactional(readOnly = true)
    public DashboardStatisticsVO getStatistics() {
        return dashboardMapper.selectStatistics();
    }

    @Override
    @Transactional(readOnly = true)
    public PageVO<ArticleRankVO> pageArticleRank(ArticleRankQueryDTO dto) {
        int pageNum = resolvePage(dto.getPage());
        int pageSize = resolvePageSize(dto.getPageSize());

        Page<ArticleRankVO> page = new Page<>(pageNum, pageSize);
        IPage<ArticleRankVO> pageResult = dashboardMapper.selectArticleRankPage(
                page,
                normalizeText(dto.getTitle()),
                dto.getCategoryId(),
                dto.getTagId());
        fillTagNames(pageResult.getRecords());
        return toPageVO(pageResult);
    }

    @Override
    @Transactional(readOnly = true)
    public PageVO<CategoryRankVO> pageCategoryRank(CategoryRankQueryDTO dto) {
        validateCountRange(dto.getMinCount(), dto.getMaxCount());

        int pageNum = resolvePage(dto.getPage());
        int pageSize = resolvePageSize(dto.getPageSize());

        Page<CategoryRankVO> page = new Page<>(pageNum, pageSize);
        // 派生表 + 数量范围过滤时关闭 count 优化，确保过滤条件参与 count
        page.setOptimizeCountSql(false);
        IPage<CategoryRankVO> pageResult = dashboardMapper.selectCategoryRankPage(
                page,
                normalizeText(dto.getName()),
                dto.getMinCount(),
                dto.getMaxCount());
        return toPageVO(pageResult);
    }

    /**
     * 按文章 ID 分组回填标签名称
     */
    private void fillTagNames(List<ArticleRankVO> records) {
        if (records == null || records.isEmpty()) {
            return;
        }
        List<Integer> articleIds = records.stream()
                .map(ArticleRankVO::getId)
                .collect(Collectors.toList());
        List<ArticleTagNameVO> tagNames = dashboardMapper.selectTagNamesByArticleIds(articleIds);
        Map<Integer, List<String>> tagNameMap = tagNames.stream()
                .collect(Collectors.groupingBy(
                        ArticleTagNameVO::getArticleId,
                        LinkedHashMap::new,
                        Collectors.mapping(ArticleTagNameVO::getTagName, Collectors.toList())));
        records.forEach(record -> record.setTagNames(
                tagNameMap.getOrDefault(record.getId(), Collections.emptyList())));
    }

    /**
     * 组装通用分页返回结构
     */
    private <T> PageVO<T> toPageVO(IPage<T> pageResult) {
        PageVO<T> pageVO = new PageVO<>();
        pageVO.setRecords(pageResult.getRecords());
        pageVO.setTotal(pageResult.getTotal());
        pageVO.setSize(pageResult.getSize());
        pageVO.setPages(pageResult.getPages());
        return pageVO;
    }

    /**
     * 处理页码默认值
     */
    private int resolvePage(Integer page) {
        return page == null || page < 1 ? DEFAULT_PAGE_NUM : page;
    }

    /**
     * 处理每页条数默认值
     */
    private int resolvePageSize(Integer pageSize) {
        return pageSize == null || pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;
    }

    /**
     * 去除首尾空格，空串转为 null
     */
    private String normalizeText(String text) {
        if (text == null) {
            return null;
        }
        String trimmed = text.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    /**
     * 校验文章数范围
     */
    private void validateCountRange(Integer minCount, Integer maxCount) {
        if (minCount != null && minCount < 0) {
            throw new BusinessException("最小文章数不能为负数");
        }
        if (maxCount != null && maxCount < 0) {
            throw new BusinessException("最大文章数不能为负数");
        }
        if (minCount != null && maxCount != null && minCount > maxCount) {
            throw new BusinessException("最小文章数不能大于最大文章数");
        }
    }
}
