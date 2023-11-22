package org.sia.vo.response;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/3 17:43
 */
@Data
public class AdminUserSearchResVo {
    private String email;
    private String userId;
    private String avatar;
    private String nickName;
    private String inviteCode;
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime createTime;
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime lastTime;
    private String state;
    private String ip;
    private Integer isAdmin;
    private String inviter;
}
