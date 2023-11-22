package org.sia.vo.request;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/31 00:05
 */
@Data
public class ConfigInitReqVo {
    private String key;
    private String name;
    private String config;
}
