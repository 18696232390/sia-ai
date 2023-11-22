package org.sia.vo.response;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/2 15:47
 */
@Data
public class AdminAppSearchResVo {
    private Long id;
    private String name;
    private Long uid;
    private Integer isPublic;
    private String icon;
    private String description;
    private String placeholder;
    private Integer sort;
    private String submitTitle;
    private String prompt;
    private Long categoryId;
    private String categoryName;
    private Long maxLength;
    private String model;
    private Float temperature;
    private Float presencePenalty;
    private Float frequencyPenalty;
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime createTime;
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime updateTime;
}
