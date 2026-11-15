package com.blog.service.impl;

import com.blog.common.Constants;
import com.blog.exception.BusinessException;
import com.blog.mapper.CategoryMapper;
import com.blog.pojo.vo.web.WebArticleBriefVO;
import com.blog.pojo.vo.web.WebCategoryHotArticleVO;
import com.blog.pojo.vo.web.WebCategoryVO;
import com.blog.service.WebCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 前台分类服务实现
 */
@Service
@RequiredArgsConstructor
public class WebCategoryServiceImpl implements WebCategoryService {

    private static final int HOT_ARTICLE_LIMIT = 3;

    private final CategoryMapper categoryMapper;

    @Override
    public List<WebCategoryVO> listWithHotArticles() {
        List<WebCategoryVO> categories = categoryMapper.selectWebCategoryList();
        fillHotArticles(categories);
        return categories;
    }

    @Override
    public WebCategoryVO getDetail(Integer id) {
        if (id == null) {
            throw new BusinessException("分类不存在");
        }
        WebCategoryVO category = categoryMapper.selectWebCategoryById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        category.setHotArticles(new ArrayList<>());
        return category;
    }

    @Override
    public List<WebCategoryVO> listTop(int limit) {
        int normalizedLimit = limit < 1 ? 1 : Math.min(limit, Constants.MAX_LIMIT);
        List<WebCategoryVO> categories = categoryMapper.selectWebCategoryList();
        categories.sort((left, right) -> {
            long leftCount = left.getArticleCount() == null ? 0L : left.getArticleCount();
            long rightCount = right.getArticleCount() == null ? 0L : right.getArticleCount();
            int countCompare = Long.compare(rightCount, leftCount);
            if (countCompare != 0) {
                return countCompare;
            }
            return Integer.compare(left.getId(), right.getId());
        });
        List<WebCategoryVO> result = new ArrayList<>();
        for (int i = 0; i < categories.size() && i < normalizedLimit; i++) {
            WebCategoryVO category = categories.get(i);
            category.setHotArticles(new ArrayList<>());
            result.add(category);
        }
        return result;
    }

    /**
     * 按分类批量回填浏览量 Top3 文章
     */
    private void fillHotArticles(List<WebCategoryVO> categories) {
        if (categories == null || categories.isEmpty()) {
            return;
        }
        List<Integer> categoryIds = categories.stream()
                .map(WebCategoryVO::getId)
                .collect(Collectors.toList());
        List<WebCategoryHotArticleVO> hotRows =
                categoryMapper.selectTopArticlesByCategoryIds(categoryIds, HOT_ARTICLE_LIMIT);
        Map<Integer, List<WebArticleBriefVO>> hotMap = hotRows.stream()
                .collect(Collectors.groupingBy(
                        WebCategoryHotArticleVO::getCategoryId,
                        Collectors.mapping(
                                row -> new WebArticleBriefVO(
                                        row.getId(), row.getTitle(), row.getViewCount()),
                                Collectors.toList())));
        for (WebCategoryVO category : categories) {
            List<WebArticleBriefVO> hotArticles = hotMap.get(category.getId());
            category.setHotArticles(hotArticles == null ? new ArrayList<>() : hotArticles);
        }
    }
}
