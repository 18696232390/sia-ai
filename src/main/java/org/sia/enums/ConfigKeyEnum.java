package org.sia.enums;

import cn.hutool.core.collection.ListUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/8 21:20
 */
@Getter
@AllArgsConstructor
public enum ConfigKeyEnum {
    // 系统配置 KEY
    KEY_MJ_METHOD("MJ_METHOD", "MJ绘画配置"),
    PKG_CATEGORY("PKG_CATEGORY", "套餐类型"),
    CHAT_BASE("CHAT_BASE", "对话设置-基础设置"),
    CHAT_PRESET("CHAT_PRESET", "对话设置-默认预设"),
    SETTING_BASE("SETTING_BASE", "全局配置-基础设置"),
    SETTING_LOGIN("SETTING_LOGIN", "全局配置-登录授权"),
    SETTING_EMAIL("SETTING_EMAIL", "全局配置-邮箱及短信"),
    SETTING_PAY("SETTING_PAY", "全局配置-产品支付"),
    SETTING_INVITE("SETTING_INVITE", "全局配置-新人与邀请"),
    SETTING_PROMPT("SETTING_PROMPT", "全局配置-提示信息"),
    SETTING_NOTIFY("SETTING_NOTIFY", "全局配置-公告通知"),
    SETTING_STYLE("SETTING_STYLE", "全局配置-样式脚本"),
    SETTING_TOOL("SETTING_TOOL", "全局配置-AI工具"),
    IMAGE_COMMON("IMAGE_COMMON", "绘画配置-通用配置"),
    IMAGE_SHOW("IMAGE_SHOW", "绘画配置-图片展示方案"),
    IMAGE_PROXY("IMAGE_PROXY", "绘画配置-代理设置"),
    IMAGE_CONSUME("IMAGE_CONSUME", "绘画配置-模型消耗"),
    SAFE_BASE("SAFE_BASE", "安全审核-基础设置"),
    SAFE_THIRD("SAFE_THIRD", "安全审核-第三方平台"),
    MENU_USER("MENU_USER", "菜单配置-用户菜单"),
    PROTOCOL_PRIACY("PROTOCOL_PRIACY", "协议内容-用户及隐私协议"),
    DOMAIN_AUTH("DOMAIN_AUTH", "域名授权"),
    ;
    private final String key;
    private final String name;
}

