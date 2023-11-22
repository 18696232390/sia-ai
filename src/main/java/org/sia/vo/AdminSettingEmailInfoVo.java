package org.sia.vo;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/11 19:53
 */
@Data
public class AdminSettingEmailInfoVo {
    private String server;
    private Integer port;
    private Boolean hasSSL;
    private String userName;
    private String password;
    private String nickName;
    private Boolean openWhiteList;
    private String whiteList;
    private String loginTemplate;
    private String registerTemplate;
    private String retrieveTemplate;
    private String bindTemplate;

    private String smsAccessKeyId;
    private String smsAccessKeySecret;
    private String smsSignName;
    private String smsRegTempCode;
    private String smsLoginTempCode;
    private String smsRetrieveTempCode;
    private String smsBindTempCode;
}
