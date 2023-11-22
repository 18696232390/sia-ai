package org.sia.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("t_chat_account")
@Data
public class ChatAccount {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("`name`")
    private String name;
    @TableField("`key`")
    private String key;
    private String platform;
    private String model;
    private Integer isValid;
    private Integer state;
    private Long totalToken;
    private Long usedToken;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime checkTime;
}
