package org.sia.vo.request;

import cn.hutool.json.JSONObject;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/14 21:11
 */
@Data
public class AdminImageAccountCreateReqVo {
    @NotBlank(message = "请填写名称")
    private String name;
    @NotBlank(message = "请填写名称")
    private String platform;
    @NotNull(message = "请选择是否可用")
    private Integer isValid;
    @NotNull(message = "请填写配置信息")
    private JSONObject config;
}
