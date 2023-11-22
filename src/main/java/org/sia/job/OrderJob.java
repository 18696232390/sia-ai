package org.sia.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sia.service.OrderService;
import org.sia.service.UserPackageService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/11 21:40
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderJob {

    private final OrderService orderService;
    /**
     * 订单状态检测
     */
    @Scheduled(fixedRate = 120000)
    public void refresh(){
        log.info(">>> 订单状态检测 start");
        orderService.checkState();
        log.info(">>> 订单状态检测 end");
    }

}
