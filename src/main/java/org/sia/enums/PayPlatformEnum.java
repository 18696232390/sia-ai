package org.sia.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/8 21:20
 */
@Getter
@AllArgsConstructor
public enum PayPlatformEnum {
    // 支付平台
    ALIPAY("ALIPAY","支付宝"),
    WXPAY("WXPAY","微信支付"),
    ;

    private final String key;
    private final String value;

}

