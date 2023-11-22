package org.sia.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/11 14:04
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "chatgpt")
public class ChatGptConfig {
    private String domain;
    private Long timeout;
    private List<String> apiKeys;
}
