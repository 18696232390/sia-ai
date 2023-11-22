package org.sia.vo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/20 14:51
 */
@Data
public class ChatMessageLoadReqVo {
    @NotNull(message = "请填写对话ID")
    private Long sessionId;
    @NotBlank(message = "请填写操作类型:UP(上拉刷新)/DOWN(下拉加载)")
    private String action;

    private Long offsetId;

    private int limit = 10;

    private String userId;
}
