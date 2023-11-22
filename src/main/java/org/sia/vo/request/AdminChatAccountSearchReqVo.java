package org.sia.vo.request;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/6 13:58
 */
@Data
public class AdminChatAccountSearchReqVo {
    private Long id;
    private String name;
    private String key;
    private Integer isValid;
}
