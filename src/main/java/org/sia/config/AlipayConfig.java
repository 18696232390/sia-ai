package org.sia.config;

import com.alipay.easysdk.kernel.Config;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/3/2 20:53
 */
@Configuration
@ConfigurationProperties(prefix = "alipay")
public class AlipayConfig extends Config{

    public void setProtocol(String protocol){
        this.protocol = protocol;
    }
    public void setGatewayHost(String gatewayHost){
        this.gatewayHost = gatewayHost;
    }
    public void setAppId(String appId){
        this.appId = appId;
    }
    public void setSignType(String signType){
        this.signType = signType;
    }
    public void setAlipayPublicKey(String alipayPublicKey){
        this.alipayPublicKey = alipayPublicKey;
    }
    public void setMerchantPrivateKey(String merchantPrivateKey){
        this.merchantPrivateKey = merchantPrivateKey;
    }
    public void setNotifyUrl(String notifyUrl){
        this.notifyUrl = notifyUrl;
    }
    public void setEncryptKey(String encryptKey){
        this.encryptKey = encryptKey;
    }

}


