package org.sia.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/7/27 23:50
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wxpay")
public class WxPayConfig {
    private String appId;
    private String mchId;
    private String apiV3Key;
    private String privateKeyPath;
    private String merchantSerialNumber;
    private String description;
    private String notifyUrl;


}
