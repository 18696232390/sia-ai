package org.sia.vo.response;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/8 23:07
 */
@Data
public class UserPreLoginResVo {
    private String url;
    private String code;
    private String verifyCode;
}
