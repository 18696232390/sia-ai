package org.sia.controller.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.sia.service.ExchangeCodeService;
import org.sia.vo.PageReqVo;
import org.sia.vo.R;
import org.sia.vo.request.ExchangeCodeBatchGenerateReqVo;
import org.sia.vo.request.ExchangeReqVo;
import org.sia.vo.response.ExchangeResVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/13 18:42
 */
@RestController
@RequestMapping("v1/exchangeCode")
@RequiredArgsConstructor
public class ExchangeController {

    private final ExchangeCodeService exchangeCodeService;



    /**
     * 兑换码兑换
     * @return
     */
    @GetMapping("trade")
    public R<String> trade(@RequestParam String code) {
        exchangeCodeService.trade(code);
        return R.success();
    }

}
