package org.sia.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2022/5/30 14:39
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {
    SUCCESS(0, "请求成功"),
    FAIL(500, "服务异常:%s"),
    ERROR(501, "%s"),

    REQUEST_PARAM_VALID(400, "%s"),
    FILE_SIZE_LIMIT(401, "文件大小不能超过%s"),
    NOT_SUPPORT_TYPE(402, "不支持的类型"),
    NO_PERMISSION_YES(403, "无效的TOKEN"),
    NO_ADMIN(404, "你不是管理员"),
    DATA_EMPTY(406, "%s不存在"),
    NOT_SIGN(407, "%s未注册"),
    HAS_SIGN_IN(408, "%s已被注册"),
    REPEAT_SEND_MAIL(409, "请勿重复发送"),
    NOT_VEFIRY_CODE(410, "请先发送验证码"),
    EMAIL_FREEZE(411, "账号已被冻结"),
    NO_PERMISSION_NO(412, "无效的TOKEN"),
    NO_BIND_PHONE(413, "绑定手机号后才能使用服务"),
    NO_BIND_EMAIL(414, "绑定邮箱后才能使用服务"),


    ALIPAY_ERROR(600,"支付宝接口错误:%s"),
    WXPAY_ERROR(601,"微信支付接口错误:%s"),


    OSS_ERROR(700,"阿里云OSS服务异常:%s"),
    SMS_ERROR(701,"短信发送异常"),


    GPT_NOT_PKG(800,"%s"),

    MJ_ERROR(900,"%s"),
    NOT_AUTH_DOMAIN(901,"未授权域名,可联系客服微信[17600016796]进行授权"),
    ;

    private Integer code;
    private String message;
}
