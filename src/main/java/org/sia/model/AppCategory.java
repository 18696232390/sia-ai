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
@TableName("t_app_category")
@Data
public class AppCategory {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("`group`")
    private String group;
    @TableField("`name`")
    private String name;
    @TableField("`key`")
    private String key;
    private String icon;
    private Integer sort;
}
