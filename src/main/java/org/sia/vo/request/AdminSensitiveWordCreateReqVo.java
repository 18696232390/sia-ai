package org.sia.vo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/15 00:24
 */
@Data
public class AdminSensitiveWordCreateReqVo {
    @NotBlank(message = "请填写敏感词")
    private String keyword;
}
