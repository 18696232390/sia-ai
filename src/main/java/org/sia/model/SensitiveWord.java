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
@TableName("t_sensitive_word")
@Data
public class SensitiveWord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String keyword;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
