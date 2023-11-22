package org.sia.vo;

import lombok.Data;
import org.sia.enums.ResultEnum;
import org.sia.enums.TaskAction;
import org.sia.exception.BusinessException;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/13 19:20
 */
@Data
public class AdminImageConsumeInfoVo {
    private Integer imagineIntegral = 0;
    private Integer upscaleIntegral = 0;
    private Integer variateIntegral = 0;
    private Integer resetIntegral = 0;
    private Integer blendIntegral = 0;
    private Integer panIntegral = 0;
    private Integer zoomIntegral = 0;
    private Integer describeIntegral = 0;


    public Integer getIntegralByTaskAction(TaskAction action) {
        if (TaskAction.IMAGINE.equals(action)) {
            return getImagineIntegral();
        } else if (TaskAction.UPSCALE.equals(action)) {
            return getUpscaleIntegral();
        } else if (TaskAction.VARIATION.equals(action)) {
            return getVariateIntegral();
        } else if (TaskAction.REROLL.equals(action)) {
            return getResetIntegral();
        } else if (TaskAction.BLEND.equals(action)) {
            return getBlendIntegral();
        } else if (TaskAction.DESCRIBE.equals(action)) {
            return getDescribeIntegral();
        }
        throw new BusinessException(ResultEnum.NOT_SUPPORT_TYPE);
    }
}
