package org.sia.vo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/6 13:59
 */
@Data
public class AdminChatAccountCreateReqVo {
    @NotBlank(message = "请填写名称")
    private String name;
    @NotBlank(message = "请根据规则配置KEY")
    private String key;
    @NotBlank(message = "请选择平台")
    private String platform;
    @NotBlank(message = "请选择模型集")
    private String model;
    @NotNull(message = "请选择是否可用")
    private Integer isValid;
}
