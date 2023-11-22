package org.sia.vo.request;

import cn.hutool.core.lang.RegexPool;
import cn.hutool.core.text.finder.PatternFinder;
import lombok.Data;
import org.wildfly.common.annotation.NotNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/8 20:45
 */
@Data
public class UserEmailSendReqVo {
    @NotBlank(message = "请填写邮件发送类型")
    private String type;
    @NotBlank(message = "请填写发送邮箱")
    @Pattern(regexp = RegexPool.EMAIL,message = "邮箱格式错误")
    private String email;
}
