package com.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.common.Constants;
import com.blog.exception.BusinessException;
import com.blog.mapper.ArticleMapper;
import com.blog.mapper.TagMapper;
import com.blog.pojo.dto.PageQueryDTO;
import com.blog.pojo.dto.web.WebArticlePageQueryDTO;
import com.blog.pojo.vo.PageVO;
import com.blog.pojo.vo.web.WebArticleBriefVO;
import com.blog.pojo.vo.web.WebArticleDetailVO;
import com.blog.pojo.vo.web.WebArticleTagVO;
import com.blog.pojo.vo.web.WebArticleVO;
import com.blog.pojo.vo.web.WebTagBriefVO;
import com.blog.service.WebArticleService;
import com.blog.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 前台文章服务实现
 */
@Service
@RequiredArgsConstructor
public class WebArticleServiceImpl implements WebArticleService {

    private static final int MIN_YEAR = 1900;
    private static final int MAX_YEAR = 9999;

    private final ArticleMapper articleMapper;

    private final TagMapper tagMapper;

    @Override
    public PageVO<WebArticleVO> pagePublished(WebArticlePageQueryDTO dto) {
        WebArticlePageQueryDTO query = dto == null ? new WebArticlePageQueryDTO() : dto;
        PageQueryDTO pageQuery = PageUtil.checkPage(query, Constants.MAX_PAGE_SIZE);
        String keyword = normalizeKeyword(query.getKeyword());

        Page<WebArticleVO> page = new Page<>(pageQuery.getPage(), pageQuery.getPageSize());
        IPage<WebArticleVO> pageResult = articleMapper.selectWebArticlePage(
                page, query.getCategoryId(), query.getTagId(), keyword);
        fillTags(pageResult.getRecords());

        PageVO<WebArticleVO> pageVO = new PageVO<>();
        pageVO.setRecords(pageResult.getRecords());
        pageVO.setTotal(pageResult.getTotal());
        pageVO.setSize(pageResult.getSize());
        pageVO.setPages(pageResult.getPages());
        return pageVO;
    }

    @Override
    public List<WebArticleBriefVO> listLatest(int limit) {
        return articleMapper.selectLatestPublished(normalizeLimit(limit));
    }

    @Override
    public List<WebArticleBriefVO> listByCategory(Integer categoryId) {
        if (categoryId == null) {
            throw new BusinessException("分类不存在");
        }
        return articleMapper.selectPublishedByCategory(categoryId);
    }

    @Override
    public List<WebArticleVO> listByMonth(int year, int month) {
        if (year < MIN_YEAR || year > MAX_YEAR) {
            throw new BusinessException("年份不正确");
        }
        if (month < 1 || month > 12) {
            throw new BusinessException("月份不正确");
        }
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startTime = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endTime = yearMonth.plusMonths(1).atDay(1).atStartOfDay();
        List<WebArticleVO> records = articleMapper.selectPublishedByTimeRange(startTime, endTime);
        fillTags(records);
        return records;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WebArticleDetailVO getDetail(Integer id) {
        if (id == null) {
            throw new BusinessException("文章不存在");
        }
        int updated = articleMapper.incrementViewCount(id);
        if (updated == 0) {
            throw new BusinessException("文章不存在或未发布");
        }
        WebArticleDetailVO detail = articleMapper.selectWebArticleDetailById(id);
        if (detail == null) {
            throw new BusinessException("文章不存在或未发布");
        }
        fillTags(Collections.<WebArticleVO>singletonList(detail));
        detail.setPrevArticle(articleMapper.selectNewerPublished(detail.getCreateTime(), id));
        detail.setNextArticle(articleMapper.selectOlderPublished(detail.getCreateTime(), id));
        return detail;
    }

    /**
     * 批量回填文章标签
     */
    private void fillTags(List<WebArticleVO> records) {
        if (records == null || records.isEmpty()) {
            return;
        }
        List<Integer> articleIds = records.stream()
                .map(WebArticleVO::getId)
                .collect(Collectors.toList());
        List<WebArticleTagVO> tagRows = tagMapper.selectTagsByArticleIds(articleIds);
        Map<Integer, List<WebTagBriefVO>> tagMap = tagRows.stream()
                .collect(Collectors.groupingBy(
                        WebArticleTagVO::getArticleId,
                        Collectors.mapping(
                                row -> new WebTagBriefVO(row.getTagId(), row.getTagName()),
                                Collectors.toList())));
        for (WebArticleVO record : records) {
            List<WebTagBriefVO> tags = tagMap.get(record.getId());
            record.setTags(tags == null ? new ArrayList<>() : tags);
        }
    }

    /**
     * 去除关键词首尾空格，空值转 null
     */
    private String normalizeKeyword(String keyword) {
        if (keyword == null) {
            return null;
        }
        String trimmed = keyword.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    /**
     * 限制一次性查询条数
     */
    private int normalizeLimit(int limit) {
        if (limit < 1) {
            return 1;
        }
        return Math.min(limit, Constants.MAX_LIMIT);
    }
}
