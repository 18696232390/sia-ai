package org.sia.vo.request;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/2 13:03
 */
@Data
public class AdminSessionEditReqVo {
    @NotNull(message = "会话ID不能为空")
    private Long id;
    private String title;
    private String userId;
    private String model;
    private String role;
    private Integer maxToken;
    private Float random;
    private Float fresh;
    private Float repeat;
    private Integer context;
}
