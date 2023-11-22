package org.sia.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/23 17:49
 */
@Data
@AllArgsConstructor
public class ImageActionCostResVo {
    private String key;
    private String value;
    private Integer cost;
}
