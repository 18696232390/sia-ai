package org.sia.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("t_image_account")
@Data
public class ImageAccount {
    @TableId(type = IdType.INPUT)
    private Long id;
    @TableField("`name`")
    private String name;
    private String platform;
    private Integer isValid;
    private String config;
    private Integer state;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
