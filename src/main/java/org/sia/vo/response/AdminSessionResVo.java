package org.sia.vo.response;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/1 23:25
 */
@Data
public class AdminSessionResVo {
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
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime createTime;
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime updateTime;
}
