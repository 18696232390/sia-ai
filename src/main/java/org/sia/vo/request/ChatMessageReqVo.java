package org.sia.vo.request;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/11 09:33
 */
@Data
public class ChatMessageReqVo {
    private Long sessionId;
    private String userId;
}
