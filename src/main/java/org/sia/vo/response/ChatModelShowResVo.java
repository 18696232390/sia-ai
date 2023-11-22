package org.sia.vo.response;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/18 19:57
 */
@Data
public class ChatModelShowResVo {
    private String key;
    private String value;
    private Integer cost;
}
