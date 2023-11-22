package org.sia.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/12 17:23
 */
@Data
public class AdminSettingPayInfoVo {
    // 支付维护停用
    private Boolean stop = true;
    // 支付维护提示
    private String stopPrompt = "支付模块维护中";
    // 支付时弹窗提示
    private String payPrompt;
    // 产品页公告
    private String content;

    // 是否开启微信支付
    @NotNull(message = "请选择是否开启微信支付")
    private Boolean hasWxPay = false;
    // 商户号
    private String wxMchId;
    // 应用ID
    private String wxAppId;
    // 商户APIV3密钥
    private String wxApiV3Key;
    // 商户证书序列号
    private String wxMchSerialNumber;
    // 商户API私钥
    private String wxPrivateKey;
    // 微信内置浏览器JSAPI支持
    private Boolean wxSupportJsApi = false;

    // 是否开启支付宝支付
    @NotNull(message = "请选择是否开启支付宝")
    private Boolean hasAliPay = false;
    // 应用ID
    private String apAppId;
    // 开发者私钥
    private String apMchPrivateKey;
    // 支付宝公钥
    private String apPublicKey;
}
