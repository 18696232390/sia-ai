package org.sia.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/2 15:43
 */
@Data
public class AdminAppSearchReqVo {
    // ID
    @ApiModelProperty(value = "id",example = "1")
    private Long id;
    // 名称
    @ApiModelProperty(value = "名称",example = "APP")
    private String name;
    private String icon;
    private String description;
}
