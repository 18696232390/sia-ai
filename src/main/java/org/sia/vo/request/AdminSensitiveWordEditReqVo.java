package org.sia.vo.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/15 00:25
 */
@Data
public class AdminSensitiveWordEditReqVo extends AdminSensitiveWordCreateReqVo{
    @NotNull(message = "请填写ID")
    private Long id;
}
