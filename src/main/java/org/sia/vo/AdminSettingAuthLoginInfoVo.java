package org.sia.vo;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/11 19:53
 */
@Data
public class AdminSettingAuthLoginInfoVo {
    private Integer expireDay;
    private Boolean hasAuth;
    private Boolean hasBindPhone;
    private Boolean hasBindEmail;

    private Boolean openPhone;
    private Boolean openEmail;

    private Boolean openWxPublic;
    private String wxPublicToken;
    private String wxEncodingAESKey;
    private String wxQrcode;
}
