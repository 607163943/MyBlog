package com.blog.service.web.impl;

import com.blog.common.Constants;
import com.blog.exception.BusinessException;
import com.blog.mapper.TagMapper;
import com.blog.pojo.vo.web.WebTagVO;
import com.blog.service.web.WebTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 前台标签服务实现
 */
@Service
@RequiredArgsConstructor
public class WebTagServiceImpl implements WebTagService {

    private final TagMapper tagMapper;

    @Override
    public List<WebTagVO> listAll() {
        return tagMapper.selectWebTagList();
    }

    @Override
    public WebTagVO getDetail(Integer id) {
        if (id == null) {
            throw new BusinessException("标签不存在");
        }
        WebTagVO tag = tagMapper.selectWebTagById(id);
        if (tag == null) {
            throw new BusinessException("标签不存在");
        }
        return tag;
    }

    @Override
    public List<WebTagVO> listTop(int limit) {
        int normalizedLimit = limit < 1 ? 1 : Math.min(limit, Constants.MAX_LIMIT);
        List<WebTagVO> tags = tagMapper.selectWebTagList();
        List<WebTagVO> result = new ArrayList<>();
        for (int i = 0; i < tags.size() && i < normalizedLimit; i++) {
            result.add(tags.get(i));
        }
        return result;
    }
}
