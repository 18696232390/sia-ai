package org.sia.vo.request;

import cn.hutool.core.lang.RegexPool;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/8 20:45
 */
@Data
public class UserSmsSendReqVo {
    @NotBlank(message = "请填写短信类型")
    private String type;
    @NotBlank(message = "请填写手机号")
    @Pattern(regexp = RegexPool.MOBILE,message = "手机格式错误")
    private String phone;
}
