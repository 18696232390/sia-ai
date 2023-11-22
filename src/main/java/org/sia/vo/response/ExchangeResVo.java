package org.sia.vo.response;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/13 18:46
 */
@Data
public class ExchangeResVo {
    private Long id;
    private String name;
    private String category;
    private String code;
    private Long expireDay;
    private String userId;
    private Long integral;
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime createTime;
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime exchangeTime;
}
