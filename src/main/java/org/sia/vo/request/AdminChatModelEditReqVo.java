package org.sia.vo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/8 00:03
 */
@Data
public class AdminChatModelEditReqVo{
    private Long id;
    @NotBlank(message = "请填写名称")
    private String name;
    @NotBlank(message = "请填写KEY值")
    private String key;
    @NotBlank(message = "请填写别名")
    private String alias;
    @NotNull(message = "请选择是否禁用")
    private Integer open;
    @NotNull(message = "请填写排序")
    private Integer sort;

    private Integer token;
    @NotNull(message = "请选择是否禁用")
    private Integer integral;
    @NotNull(message = "请选择是否为自定义模型")
    private Integer custom;

}
