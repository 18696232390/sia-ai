package org.sia.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("t_chat_model")
@Data
public class ChatModel {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("`name`")
    private String name;
    @TableField("`key`")
    private String key;
    private String alias;
    @TableField("`open`")
    private Integer open;
    private Integer sort;
    private Integer integral;
    private Integer custom;
}
