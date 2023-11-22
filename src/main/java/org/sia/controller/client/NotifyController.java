package org.sia.controller.client;

import lombok.RequiredArgsConstructor;
import org.sia.enums.ConfigKeyEnum;
import org.sia.service.ConfigService;
import org.sia.vo.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/10 17:10
 */
@RestController
@RequestMapping("v1/notify")
@RequiredArgsConstructor
public class NotifyController {
    private final ConfigService configService;

    /**
     * 默认公告信息
     *
     * @return
     */
    @GetMapping("/default")
    public R<String> defaultNotify() {
        return R.success(configService.getConfigObject(ConfigKeyEnum.SETTING_NOTIFY).getStr("popTip"));
    }
}
