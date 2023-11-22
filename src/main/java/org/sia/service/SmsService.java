package org.sia.service;

import cn.hutool.json.JSONUtil;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponseBody;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sia.enums.ConfigKeyEnum;
import org.sia.enums.ResultEnum;
import org.sia.exception.BusinessException;
import org.sia.vo.AdminSettingEmailInfoVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/23 22:30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService implements InitializingBean {
    private final ConfigService configService;
    // 短信签名（模板）
    private String signName;

    // 验证码模板
    private String loginTemplateCode;
    private String registerTemplateCode;
    private String retrieveTempCode;
    private String bindTempCode;
    private AsyncClient smsClient;


    @Override
    public void afterPropertiesSet() throws Exception {
       this.initSmsConfig();
    }

    /**
     * 初始化短信配置
     */
    public void initSmsConfig(){
        // 手机配置信息
        AdminSettingEmailInfoVo config = configService.getConfigObject(ConfigKeyEnum.SETTING_EMAIL).toBean(AdminSettingEmailInfoVo.class);
        // 初始化阿里云短信配置
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(config.getSmsAccessKeyId())
                .accessKeySecret(config.getSmsAccessKeySecret())
                .build());

        this.smsClient = AsyncClient.builder()
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                )
                .build();

        this.signName = config.getSmsSignName();
        this.loginTemplateCode = config.getSmsLoginTempCode();
        this.registerTemplateCode = config.getSmsRegTempCode();
        this.retrieveTempCode = config.getSmsRetrieveTempCode();
        this.bindTempCode = config.getSmsBindTempCode();
    }

    private void send(String phone,String code,String templateCode){
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .phoneNumbers(phone)
                .signName(signName)
                .templateCode(templateCode)
                .templateParam(JSONUtil.createObj().set("code",code).toString())
                .build();

        CompletableFuture<SendSmsResponse> response = smsClient.sendSms(sendSmsRequest);

        try {
            SendSmsResponse smsResponse = response.get();
            SendSmsResponseBody body = smsResponse.getBody();
            if (!"OK".equals(body.getCode())){
                log.error("阿里云短信发送异常:{}",body.getMessage());
                throw new BusinessException(ResultEnum.SMS_ERROR);
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("阿里云短信发送异常",e);
            throw new BusinessException(ResultEnum.SMS_ERROR);
        }
    }

    public void sendLogin(String phone,String code){
        this.send(phone,code,loginTemplateCode);
    }

    public void sendRegister(String phone,String code){
        this.send(phone,code,registerTemplateCode);
    }

    public void sendGetBack(String phone,String code){
        this.send(phone,code,retrieveTempCode);
    }


    public void sendBind(String phone,String code){
        this.send(phone,code,bindTempCode);
    }

}
