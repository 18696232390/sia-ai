package org.sia.vo.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/14 21:47
 */
@Data
public class AdminImageAccountEditReqVo extends AdminImageAccountCreateReqVo{
    @NotNull(message = "请填写ID")
    private Long id;
}
