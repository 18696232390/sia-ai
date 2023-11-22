package org.sia.controller.client;

import javax.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.sia.service.ConfigService;
import org.sia.vo.R;
import org.sia.vo.request.ConfigInitReqVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/19 12:30
 */
@RestController
@RequestMapping("v1/config")
@RequiredArgsConstructor
public class ConfigController {

    private final ConfigService configService;

    @GetMapping("/{key}")
    public R<Object> getConfig(@PathVariable String key) {
        return R.success(configService.getConfig(key));
    }

}
