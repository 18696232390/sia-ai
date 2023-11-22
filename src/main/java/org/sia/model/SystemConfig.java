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
@TableName("t_config")
@Data
public class SystemConfig {
    @TableId(value = "`key`",type = IdType.INPUT)
    private String key;
    @TableField("`name`")
    private String name;
    private String config;
    @TableField("`ignore`")
    private String ignore;
}
