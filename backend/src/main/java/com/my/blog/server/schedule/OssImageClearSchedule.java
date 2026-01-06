package com.my.blog.server.schedule;

import com.my.blog.common.constants.UploadFileRefStatus;
import com.my.blog.pojo.po.UploadFileRef;
import com.my.blog.server.service.IUploadFileRefService;
import com.my.blog.server.service.IUploadFileService;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Component
public class OssImageClearSchedule {
    @Resource
    private IUploadFileRefService uploadFileRefService;
    @Resource
    private IUploadFileService uploadFileService;

    @Resource
    private FileStorageService fileStorageService;
    /**
     * 每小时清理一次超过24小时的分类图片封面
     */
    @Scheduled(cron = "* * 0/1 * * ? ")
    public void clearCategoryCover() {
        // 获取所有上传文件id集合
        uploadFileRefService.lambdaUpdate()
                .eq(UploadFileRef::getStatus, UploadFileRefStatus.NOT_USE)
                // 今天往前退24小时，在这之前的都是创建超过24小时的
                .lt(UploadFileRef::getCreateTime, LocalDateTime.now().minusHours(24))
                .remove();
        // TODO:需要查询出无用图片上传记录，然后执行Oss删除操作
    }
}
