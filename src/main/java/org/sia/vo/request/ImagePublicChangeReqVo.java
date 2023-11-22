package org.sia.vo.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/20 12:10
 */
@Data
public class ImagePublicChangeReqVo {
    @NotBlank(message = "请填写任务ID")
    private String taskId;
    @NotBlank(message = "请填写是否公开")
    private String isPublic;
}
