package org.sia.vo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/6 13:59
 */
@Data
public class AdminChatAccountEditReqVo extends AdminChatAccountCreateReqVo{
    @NotNull(message = "请填写账号ID")
    private Long id;
}
