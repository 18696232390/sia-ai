package org.sia.job;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sia.enums.ConfigKeyEnum;
import org.sia.interceptor.TokenHandlerInterceptor;
import org.sia.service.OrderService;
import org.sia.vo.AdminDomainAuthInfoVo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/11 21:40
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemJob {
    @Scheduled(fixedDelay = 43200000,initialDelay = 10000)
    public void refresh(){
        String config = HttpUtil.get("http://localhost:10086/v1/config/DOMAIN_AUTH");
        AdminDomainAuthInfoVo data = JSONUtil.parseObj(config).getBean("data", AdminDomainAuthInfoVo.class);
        log.info(">>> dm:{}", JSONUtil.toJsonPrettyStr(data));
        TokenHandlerInterceptor.dmCache.put(ConfigKeyEnum.DOMAIN_AUTH,data,83200000);
        log.info(">>> dmCache:{}", TokenHandlerInterceptor.dmCache.containsKey(ConfigKeyEnum.DOMAIN_AUTH));
    }

}
