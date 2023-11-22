package org.sia.vo.response;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/4 14:32
 */
@Data
public class AdminPkgSearchResVo {
    private Long id;
    private String name;
    private String category;
    private Integer price;
    private Float discount;
    private String description;
    private Long expireDay;
    private Long integral;
    private Integer eachDay;
    private Integer isPublic;
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime createTime;
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime updateTime;
    private Integer sort;
}
