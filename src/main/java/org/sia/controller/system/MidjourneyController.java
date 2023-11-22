package org.sia.controller.system;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.sia.enums.ConfigKeyEnum;
import org.sia.model.ImageAccount;
import org.sia.service.ConfigService;
import org.sia.service.ImageAccountService;
import org.sia.vo.AdminImageCommonInfoVo;
import org.sia.vo.AdminImageProxyInfoVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/28 11:17
 */
@RestController
@RequestMapping("v1/system/mj")
@RequiredArgsConstructor
public class MidjourneyController {

    private final ConfigService configService;
    private final ImageAccountService accountService;
    /**
     * 获取MJ配置信息
     * @return
     */
    @GetMapping("config/info")
    public JSONObject getConfigInfo(){
        AdminImageProxyInfoVo proxyConfig = configService.getConfigObject(ConfigKeyEnum.IMAGE_PROXY).toBean(AdminImageProxyInfoVo.class);
        AdminImageCommonInfoVo commonConfig = configService.getConfigObject(ConfigKeyEnum.IMAGE_COMMON).toBean(AdminImageCommonInfoVo.class);
        return JSONUtil.createObj()
                .set("accountList",accountService.list(Wrappers.<ImageAccount>lambdaQuery().eq(ImageAccount::getPlatform,"Discord Midjourney")))
                .set("proxy",proxyConfig)
                .set("common",commonConfig);
    }
}
