package org.sia.vo.response;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/9 10:43
 */
@Data
public class UserInfoResVo {
    private String userId;
    private String email;
    private String openId;
    private String nickName;
    private String phone;
    private String avatar;
    private Integer isAdmin;
    private String themeMode;
    private String apiKey;
}
