package com.blog.service.admin.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.enums.DashboardExceptionEnum;
import com.blog.exception.BusinessException;
import com.blog.mapper.ArticleContributionMapper;
import com.blog.mapper.ArticleMapper;
import com.blog.mapper.CategoryMapper;
import com.blog.mapper.TagMapper;
import com.blog.mapper.ViewSnapshotMapper;
import com.blog.pojo.dto.admin.ArticleRankQueryDTO;
import com.blog.pojo.dto.admin.CategoryRankQueryDTO;
import com.blog.pojo.dto.PageQueryDTO;
import com.blog.pojo.po.ViewSnapshot;
import com.blog.pojo.vo.admin.ArticleContributionVO;
import com.blog.pojo.vo.admin.ArticleRankVO;
import com.blog.pojo.vo.admin.ArticleTagNameVO;
import com.blog.pojo.vo.admin.CategoryRankVO;
import com.blog.pojo.vo.admin.DashboardStatisticsVO;
import com.blog.pojo.vo.admin.ViewTrendVO;
import com.blog.pojo.vo.PageVO;
import com.blog.service.admin.DashboardService;
import com.blog.util.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据总览服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    /**
     * 趋势图展示的天数
     */
    private static final int VIEW_TREND_DAYS = 30;

    /**
     * 贡献日历展示的天数（含今天）
     */
    private static final int CONTRIBUTION_DAYS = 365;

    private final ArticleMapper articleMapper;

    private final ArticleContributionMapper articleContributionMapper;

    private final CategoryMapper categoryMapper;

    private final TagMapper tagMapper;

    private final ViewSnapshotMapper viewSnapshotMapper;

    @Override
    @Transactional(readOnly = true)
    public DashboardStatisticsVO getStatistics() {
        return articleMapper.selectStatistics();
    }

    @Override
    @Transactional(readOnly = true)
    public PageVO<ArticleRankVO> pageArticleRank(ArticleRankQueryDTO dto) {
        PageQueryDTO query = PageUtil.checkPage(dto);

        Page<ArticleRankVO> page = new Page<>(query.getPage(), query.getPageSize());
        IPage<ArticleRankVO> pageResult = articleMapper.selectArticleRankPage(
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

        PageQueryDTO query = PageUtil.checkPage(dto);

        Page<CategoryRankVO> page = new Page<>(query.getPage(), query.getPageSize());
        // 派生表 + 数量范围过滤时关闭 count 优化，确保过滤条件参与 count
        page.setOptimizeCountSql(false);
        IPage<CategoryRankVO> pageResult = categoryMapper.selectCategoryRankPage(
                page,
                normalizeText(dto.getName()),
                dto.getMinCount(),
                dto.getMaxCount());
        return toPageVO(pageResult);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTrendVO> getViewTrend() {
        return viewSnapshotMapper.selectDailyViewTrend(
                LocalDate.now().minusDays(VIEW_TREND_DAYS - 1L));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleContributionVO> getArticleContribution() {
        LocalDate today = LocalDate.now();
        return articleContributionMapper.selectDailyContribution(
                StpUtil.getLoginIdAsInt(),
                today.minusDays(CONTRIBUTION_DAYS - 1L),
                today);
    }

    @Override
    @Transactional
    public void recordViewSnapshot() {
        Long totalViewCount = articleMapper.selectPublishedTotalViewCount();
        LocalDate today = LocalDate.now();
        ViewSnapshot snapshot = viewSnapshotMapper.selectOne(
                new LambdaQueryWrapper<ViewSnapshot>()
                        .eq(ViewSnapshot::getSnapshotDate, today));
        if (snapshot == null) {
            ViewSnapshot newSnapshot = new ViewSnapshot();
            newSnapshot.setSnapshotDate(today);
            newSnapshot.setTotalViewCount(totalViewCount);
            viewSnapshotMapper.insert(newSnapshot);
        } else {
            // 仅更新总量，update_time 由数据库自动维护
            viewSnapshotMapper.update(null,
                    new LambdaUpdateWrapper<ViewSnapshot>()
                            .eq(ViewSnapshot::getId, snapshot.getId())
                            .set(ViewSnapshot::getTotalViewCount, totalViewCount));
        }
        log.info("[VIEW_SNAPSHOT] date={} totalViewCount={} action={}",
                today, totalViewCount, snapshot == null ? "INSERT" : "UPDATE");
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
        List<ArticleTagNameVO> tagNames = tagMapper.selectTagNamesByArticleIds(articleIds);
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
            throw new BusinessException(DashboardExceptionEnum.DASHBOARD_MIN_COUNT_NEGATIVE);
        }
        if (maxCount != null && maxCount < 0) {
            throw new BusinessException(DashboardExceptionEnum.DASHBOARD_MAX_COUNT_NEGATIVE);
        }
        if (minCount != null && maxCount != null && minCount > maxCount) {
            throw new BusinessException(DashboardExceptionEnum.DASHBOARD_COUNT_RANGE_INVALID);
        }
    }
}
