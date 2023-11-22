package org.sia.vo.request;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/5 15:10
 */
@Data
public class AdminOrderSearchReqVo {
    private String id;
    private String platform;
    private String userId;
    private Long pkgId;
    private String state;
}
