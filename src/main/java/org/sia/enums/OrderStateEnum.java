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
public enum OrderStateEnum {
    // 订单状态
    UNPAY("UNPAY", "未支付"),
    PAY("PAY", "支付成功"),
    CANCEL("CANCEL", "支付取消"),
    ;

    private final String code;
    private final String remark;
}

