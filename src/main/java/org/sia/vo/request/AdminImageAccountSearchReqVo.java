package org.sia.vo.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/14 20:03
 */
@Data
public class AdminImageAccountSearchReqVo {
    private Long id;
    private String name;
    private Integer isValid;
    private String updateBeginTime;
    private String updateEndTime;
}
