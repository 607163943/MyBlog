package com.my.blog.server.service;

import com.my.blog.pojo.entity.BlogTagCount;
import com.my.blog.common.utils.PageQueryUtil;
import com.my.blog.common.utils.PageResult;

import java.util.List;

public interface TagService {

    /**
     * 查询标签的分页数据
     *
     * @param pageUtil
     * @return
     */
    PageResult getBlogTagPage(PageQueryUtil pageUtil);

    int getTotalTags();

    Boolean saveTag(String tagName);

    Boolean deleteBatch(Integer[] ids);

    List<BlogTagCount> getBlogTagCountForIndex();
}
