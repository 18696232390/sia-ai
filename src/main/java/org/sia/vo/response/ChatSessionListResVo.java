package org.sia.vo.response;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/10 20:54
 */
@Data
public class ChatSessionListResVo {
    private Long id;
    private String title;
    private String model;
    private Integer maxToken;
    private String role;
    private Float random;
    private Float fresh;
    private Float repeat;
    private Integer context;
    private Integer isPinned;
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime updateTime;
}
