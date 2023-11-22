package org.sia.vo.request;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/3 18:16
 */
@Data
public class AdminUserEditReqVo {
    @NotBlank(message = "请填写编辑用户ID")
    private String userId;
    private String email;
    private String avatar;
    private String nickName;
    private String inviteCode;
    private String state;
    private Integer isAdmin;
}
