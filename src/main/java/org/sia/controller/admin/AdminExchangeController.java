package org.sia.controller.admin;

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
@RequestMapping("v1/admin/exchange")
@RequiredArgsConstructor
public class AdminExchangeController {

    private final ExchangeCodeService exchangeCodeService;

    /**
     * 批量生成兑换码
     * @return
     */
    @PostMapping("generate/batch")
    public R<String> batchGenerate(@RequestBody @Validated ExchangeCodeBatchGenerateReqVo reqVo) {
        exchangeCodeService.batchGenerate(reqVo);
        return R.success();
    }


    /**
     * 兑换码列表
     * @param reqVo
     * @return
     */
    @PostMapping("/list")
    public R<Page<ExchangeResVo>> codeList(@RequestBody @Validated PageReqVo<ExchangeReqVo> reqVo){
        return R.success(exchangeCodeService.list(reqVo));
    }

    /**
     * 删除兑换码
     * @return
     */
    @GetMapping("remove")
    public R<String> remove(@RequestParam String ids) {
        exchangeCodeService.remove(ids);
        return R.success();
    }
}
