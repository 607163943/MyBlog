package com.my.blog.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.pojo.po.UploadFile;
import com.my.blog.pojo.vo.admin.AdminUploadFileVO;
import com.my.blog.server.mapper.UploadFileMapper;
import com.my.blog.server.service.IUploadFileRefService;
import com.my.blog.server.service.IUploadFileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Service
public class UploadFileServiceImpl extends ServiceImpl<UploadFileMapper, UploadFile> implements IUploadFileService {
    @Resource
    private IUploadFileRefService uploadFileRefService;

    /**
     * 上传图片
     * @param multipartFile 图片文件
     * @return 图片信息
     */
    @Override
    public AdminUploadFileVO uploadImageToOss(MultipartFile multipartFile) {
        return null;
    }
}
