package org.sia.vo;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/12 23:02
 */
@Data
public class AdminSettingInviteInfoVo {
    // 启用新人注册奖励
    private Boolean hasNewUser;
    // 新人注册奖励积分
    private Integer nIntegral;
    // 新人注册奖励积分有效天数
    private Integer nExpireDay;

    // 启用邀请奖励
    private Boolean hasInviteUser;
    // 邀请好友奖励积分
    private Integer iIntegral;
    // 邀请好友奖励积分有效天数
    private Integer iExpireDay;
    // 邀请好友防刷检测
    private Boolean checkBrush;
    // 邀请说明模板
    private String template;

}
