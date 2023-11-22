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
@TableName("t_chat_session")
public class ChatSession {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String userId;
    private String model;
    private Integer maxToken;
    private String role;
    private Float random;
    private Float fresh;
    @TableField("`repeat`")
    private Float repeat;
    private Integer context;
    private Integer isPinned;
    private LocalDateTime updateTime;
    private LocalDateTime createTime;
}
