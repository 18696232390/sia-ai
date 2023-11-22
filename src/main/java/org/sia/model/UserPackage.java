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
 * @CreateDate: 2023/8/10 16:35
 */
@Data
@TableName("t_user_package")
public class UserPackage {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String userId;
    private Long pkgId;
    @TableField("`name`")
    private String name;
    private String category;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime resetTime;
    private Integer eachDay;
    private Long integral;
    private Long remain;
    private Long exchangeId;
}
