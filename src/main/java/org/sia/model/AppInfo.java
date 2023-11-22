package org.sia.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/9 11:54
 */
@TableName("t_app_info")
@Data
public class AppInfo {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("`name`")
    private String name;
    private String userId;
    private Integer isPublic;
    private String icon;
    private String description;
    private String placeholder;
    private Integer sort;
    private String submitTitle;
    private String prompt;
    private Long categoryId;
    private Long maxLength;
    private String model;
    private Float temperature;
    private Float presencePenalty;
    private Float frequencyPenalty;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
