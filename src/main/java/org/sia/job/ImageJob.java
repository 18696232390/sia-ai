package org.sia.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sia.service.ImageService;
import org.sia.service.OrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/23 21:22
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ImageJob {

    private final ImageService imageService;
    /**
     * 绘画任务状态检测
     */
    @Scheduled(fixedRate = 100000)
    public void checkTaskState(){
        log.info(">>> 绘画任务状态检测 start");
        imageService.checkState();
        log.info(">>> 绘画任务状态检测 end");
    }

    /**
     * 绘画账号可用性检测
     */
    @Scheduled(fixedRate = 100000)
    public void checkAccountState(){
        log.info(">>> 绘画账号可用性检测 start");
        imageService.checkAccountState();
        log.info(">>> 绘画账号可用性检测 end");
    }
}
