package org.sia.service;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sia.enums.ConfigKeyEnum;
import org.sia.vo.AdminSettingBaseInfoVo;
import org.sia.vo.AdminSettingEmailInfoVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/8 17:19
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MailService implements InitializingBean {
    private MailAccount mailAccount;

    private final ConfigService configService;
    // 站点名称
    private String sitName;
    // 邮件模板
    private String registerTemplate;
    private String loginTemplate;
    private String retrieveTemplate;
    private String bindTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initMailConfig();
        this.initSitName();
    }

    /**
     * 初始化邮箱配置
     */
    public void initMailConfig() {
        AdminSettingEmailInfoVo config = configService.getConfigObject(ConfigKeyEnum.SETTING_EMAIL).toBean(AdminSettingEmailInfoVo.class);
        this.mailAccount = new MailAccount();
        this.mailAccount.setHost(config.getServer());
        this.mailAccount.setPort(config.getPort());
        this.mailAccount.setSslEnable(config.getHasSSL());
        this.mailAccount.setFrom(StrUtil.format("{}<{}>", config.getNickName(), config.getUserName()));
        this.mailAccount.setPass(config.getPassword());
        if (config.getOpenWhiteList() && StrUtil.isNotBlank(config.getWhiteList())) {
            this.mailAccount.setSslProtocols(String.join(" ", StrUtil.split(config.getWhiteList(), ',')));
        }
        this.registerTemplate = config.getRegisterTemplate();
        this.loginTemplate = config.getLoginTemplate();
        this.retrieveTemplate = config.getRetrieveTemplate();
        this.bindTemplate = config.getBindTemplate();
    }

    public void initSitName() {
        // 站点名称
        AdminSettingBaseInfoVo baseConfig = configService.getConfigObject(ConfigKeyEnum.SETTING_BASE).toBean(AdminSettingBaseInfoVo.class);
        this.sitName = baseConfig.getName();
    }


    /**
     * 发生邮箱注册验证码
     *
     * @param email
     * @param code
     */
    public void sendVerifyCode(String email, String code) {
        String html = StrUtil.format(registerTemplate,
                MapUtil.builder("{sit_name}", sitName)
                        .put("{code}", code)
                        .build());
        MailUtil.send(
                mailAccount,
                email,
                StrUtil.format("{}账号注册", sitName),
                StrUtil.format(html, code), true);
    }


    /**
     * 发生邮箱登录验证码
     *
     * @param email
     * @param code
     */
    public void sendLoginCode(String email, String code) {
        String html = StrUtil.format(loginTemplate,
                MapUtil.builder("{sit_name}", sitName)
                        .put("{code}", code)
                        .build());
        MailUtil.send(
                mailAccount,
                email,
                StrUtil.format("{}登录验证", sitName),
                StrUtil.format(html, code), true);
    }


    /**
     * 发生邮箱找回验证码
     *
     * @param email
     * @param code
     */
    public void sendGetBackCode(String email, String code) {
        String html = StrUtil.format(retrieveTemplate,
                MapUtil.builder("{sit_name}", sitName)
                        .put("{code}", code)
                        .build());
        MailUtil.send(
                mailAccount,
                email,
                StrUtil.format("{}找回密码", sitName),
                StrUtil.format(html, code), true);
    }

    /**
     * 发生邮箱找回验证码
     *
     * @param email
     * @param code
     */
    public void sendBindCode(String email, String code) {
        String html = StrUtil.format(bindTemplate,
                MapUtil.builder("{sit_name}", sitName)
                        .put("{code}", code)
                        .build());
        MailUtil.send(
                mailAccount,
                email,
                StrUtil.format("{}绑定邮箱", sitName),
                StrUtil.format(html, code), true);
    }
}
