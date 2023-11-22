package org.sia.vo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/3 18:22
 */
@Data
public class AdminPwdResetReqVo {
    @NotBlank(message = "请填写用户ID")
    private String userId;
    @NotBlank(message = "请填写用户密码")
    private String password;
}
