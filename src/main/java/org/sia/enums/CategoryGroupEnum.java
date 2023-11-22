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
public enum CategoryGroupEnum {
    // 分类分组
    APP("APP", "应用管理"),
    ;

    private final String name;
    private final String remark;
}

