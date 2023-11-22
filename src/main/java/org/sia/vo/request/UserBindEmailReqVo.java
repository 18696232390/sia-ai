package org.sia.vo.request;

import cn.hutool.core.lang.RegexPool;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/9 20:30
 */
@Data
public class UserBindEmailReqVo {
    @NotBlank(message = "请填写邮箱")
    @Pattern(regexp = RegexPool.EMAIL,message = "邮箱格式错误")
    private String email;
    @NotBlank(message = "请填写验证码")
    @Length(min = 6, max = 6, message = "验证码格式错误")
    private String code;
    @NotBlank(message = "请填写图形验证码code")
    private String captchaCode;
    @NotBlank(message = "请填写图形验证码")
    @Length(min = 4, max = 4, message = "图形验证码格式错误")
    private String captcha;
}
