package com.my.blog.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.pojo.po.Dict;
import com.my.blog.server.mapper.DictMapper;
import com.my.blog.server.service.IDictService;
import org.springframework.stereotype.Service;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper,Dict> implements IDictService {
}
