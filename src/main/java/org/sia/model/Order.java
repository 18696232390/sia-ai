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
@TableName("t_order")
@Data
public class Order {
    @TableId(type = IdType.INPUT)
    private String id;
    private String platform;
    private String userId;
    private Long pkgId;
    private String description;
    private Integer price;
    private String info;
    private String state;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
