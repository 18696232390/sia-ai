package org.sia.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/8 21:06
 */
@TableName("t_user")
@Data
public class User {
    @TableId(type = IdType.INPUT)
    private String userId;
    private String email;
    private String phone;
    private String openId;
    private String avatar;
    private String nickName;
    private String password;
    private String inviteCode;
    private LocalDateTime createTime;
    private LocalDateTime lastTime;
    private String ip;
    private String state;
    private Integer isAdmin;
    private String themeMode;
    private String apiKey;
}
