package com.my.blog.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.pojo.po.UploadFileRef;
import com.my.blog.server.mapper.UploadFileRefMapper;
import com.my.blog.server.service.IUploadFileRefService;
import org.springframework.stereotype.Service;

@Service
public class UploadFileRefServiceImpl extends ServiceImpl<UploadFileRefMapper, UploadFileRef> implements IUploadFileRefService {
}
