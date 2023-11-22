package org.sia.vo.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/13 19:24
 */
@Data
public class ExchangeCodeBatchGenerateReqVo {
    @NotNull(message = "请填写生成数量")
    private Long number;
    @NotNull(message = "请填写套餐ID")
    private Long pkgId;
}
