package org.sia.vo.request;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/5 18:18
 */
@Data
public class AdminUpReqVo {
    private Long id;
    private String userId;
    private String name;
    private Integer eachDay;
}
