package com.blog.task;

import com.blog.service.admin.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 文章总浏览量快照定时任务
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ViewSnapshotTask {

    private final DashboardService dashboardService;

    /**
     * 每小时整点记录一次已发布文章总浏览量快照
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void recordViewSnapshot() {
        try {
            dashboardService.recordViewSnapshot();
        } catch (Exception e) {
            log.error("[VIEW_SNAPSHOT] 定时任务执行失败", e);
        }
    }
}
