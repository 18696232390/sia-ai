package org.sia.vo;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/8 14:09
 */
@Data
public class AdminChatBaseInfoVo {
    // 默认AI对话消息
    private String defaultMsg = "您好,我是SiaAI对话小助手，请输入您当前遇到的难题，我会尽力帮你解决的～";
    // KEY有效自动检查 0 否 1 是
    private Integer autoCheck = 0;
    // KEY检查时间间隔 (分钟)
    private Integer interval = 10;
    // 会话标题自动生成
    private Integer autoTitle = 0;
    // 会话标题自动生成模型
    private String model = "gpt-3.5-turbo";
    // OpenAI反向代理
    private String openAiProxy = "https://api.openai.com";
    // OpenAI默认域名
    private String openAi = "https://api.openai.com";
    // OpenAI-SB反向代理
    private String openAiSbProxy="https://api.openai-sb.com";
    // openAiSb默认域名
    private String openAiSb="https://api.openai-sb.com";
    // Api2D反向代理
    private String api2dProxy="https://openai.api2d.com";
    // Api2D默认域名
    private String api2d="https://openai.api2d.com";
    // 自建 OneAPI域名地址
    private String oneApiProxy="https://api.openai.com";
    // 其他平台反向代理
    private String otherProxy="https://xxx.openai.com";
}
