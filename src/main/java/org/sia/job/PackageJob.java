package org.sia.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sia.service.UserPackageService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/11 21:40
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PackageJob {

    private final UserPackageService userPackageService;
    /**
     * 每日重置套餐包
     */
    @Scheduled(fixedRate = 120000)
    public void refresh(){
        log.info(">>> 重置套餐包 start");
        userPackageService.reset();
        log.info(">>> 重置套餐包 end");
    }

}
