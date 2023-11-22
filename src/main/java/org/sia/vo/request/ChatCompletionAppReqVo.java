package org.sia.vo.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/11 13:59
 */
@Data
public class ChatCompletionAppReqVo {
    @NotNull(message = "请输入AppId")
    private Long appId;
    @NotEmpty(message = "请输入消息内容")
    private String text;
}
