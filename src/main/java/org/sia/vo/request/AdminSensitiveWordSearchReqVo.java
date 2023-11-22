package org.sia.vo.request;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/15 00:32
 */
@Data
public class AdminSensitiveWordSearchReqVo {
    private Long id;
    private String keyword;
    private String createBeginTime;
    private String createEndTime;
    private String updateBeginTime;
    private String updateEndTime;
}
