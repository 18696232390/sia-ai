package org.sia.vo.request;

import cn.hutool.core.lang.RegexPool;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/9 20:30
 */
@Data
public class UserModifyPwdReqVo {
    @NotBlank(message = "请填写原密码")
    private String oldPwd;
    @NotBlank(message = "请填写新密码")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,16}$",message = "包含大小写字母、数字和特殊字符，长度在8~16个字符")
    private String newPwd;
}
