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
public enum UserStateEnum {
    // 用户账户状态
    SIGN_IN("SIGN_IN","账号注册中"),
    NORMAL("NORMAL","账号正常使用"),
    FREEZE("FREEZE","账号已被冻结"),

    ;

    private final String code;
    private final String remark;
}

