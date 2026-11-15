package com.blog.service.web.impl;

import com.blog.mapper.ArticleMapper;
import com.blog.pojo.vo.web.WebArchiveMonthVO;
import com.blog.pojo.vo.web.WebArchiveYearVO;
import com.blog.service.web.WebArchiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 前台归档服务实现
 */
@Service
@RequiredArgsConstructor
public class WebArchiveServiceImpl implements WebArchiveService {

    private final ArticleMapper articleMapper;

    @Override
    public List<WebArchiveYearVO> listOverview() {
        List<WebArchiveMonthVO> rows = articleMapper.selectWebArchiveOverview();
        Map<Integer, WebArchiveYearVO> yearMap = new LinkedHashMap<>();
        for (WebArchiveMonthVO row : rows) {
            WebArchiveYearVO yearVO = yearMap.computeIfAbsent(row.getYear(), year -> {
                WebArchiveYearVO item = new WebArchiveYearVO();
                item.setYear(year);
                item.setMonths(new ArrayList<>());
                return item;
            });
            yearVO.getMonths().add(row);
        }
        return new ArrayList<>(yearMap.values());
    }
}
