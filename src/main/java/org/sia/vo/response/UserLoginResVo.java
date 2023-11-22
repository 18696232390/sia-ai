package org.sia.vo.response;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/8 23:07
 */
@Data
public class UserLoginResVo {
    private String token;
    private String expireTime;
}
