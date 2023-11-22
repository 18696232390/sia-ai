package org.sia.vo.request;

import javax.validation.constraints.NotEmpty;
import lombok.Data;


/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/11 13:59
 */
@Data
public class ChatCompletionReqVo {
    private Long sessionId;
    private Long appId;
    @NotEmpty(message = "请输入消息内容")
    private String text;
}
