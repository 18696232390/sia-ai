package org.sia.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/8 21:20
 */
@Getter
@AllArgsConstructor
public enum ChatPlatformEnum {
    // 对话平台
    OPENAI("OpenAI","OpenAI","openAi"),
    OPENAI_SB("OpenAI-SB","OpenAI-SB","openAiSb"),
    API2D("Api2d","Api2d","api2d"),
    ONEAPI("OneAPI","OneAPI","oneApiProxy"),
    BAIDU("BaiDu","文心千帆",""),
    XFYUN("XfYun","讯飞星火",""),
    OTHER("OTHER","其他","otherProxy"),
    ;

    private final String key;
    private final String value;
    private final String domainKey;

    public static String getDomainKey(String key){
        for (ChatPlatformEnum chatPlatformEnum : ChatPlatformEnum.values()) {
            if (key.equals(chatPlatformEnum.getKey())){
                return chatPlatformEnum.getDomainKey();
            }
        }
        return null;
    }

    public static ChatPlatformEnum get(String key) {
        for (ChatPlatformEnum chatPlatformEnum : ChatPlatformEnum.values()) {
            if (key.equals(chatPlatformEnum.getKey())){
                return chatPlatformEnum;
            }
        }
        return null;
    }
}

