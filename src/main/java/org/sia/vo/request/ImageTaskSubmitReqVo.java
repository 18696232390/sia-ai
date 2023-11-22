package org.sia.vo.request;

import javax.validation.constraints.NotNull;
import lombok.Data;
import org.sia.enums.TaskAction;

import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/17 21:46
 */
@Data
public class ImageTaskSubmitReqVo {
    @NotNull(message = "操作类型不能为空")
    private TaskAction action;
    private String prompt;
    private String taskId;
    private Integer index;
    private String isPublic = "0";
    private List<String> imageList;
}
