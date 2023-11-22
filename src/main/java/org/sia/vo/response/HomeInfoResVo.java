package org.sia.vo.response;

import lombok.Data;

import java.util.Map;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/23 16:59
 */
@Data
public class HomeInfoResVo {
    private Map<String,Long> total;
    private Map<String,Long> toady;
}
