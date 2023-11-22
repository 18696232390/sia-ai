package org.sia.controller.admin;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.sia.enums.ConfigKeyEnum;
import org.sia.service.*;
import org.sia.vo.*;
import org.sia.vo.request.AdminChatModelEditReqVo;
import org.sia.vo.request.ConfigInitReqVo;
import org.sia.vo.response.AdminChatModelInfoResVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/7 18:16
 */
@RequestMapping("/v1/admin/global")
@RestController
@RequiredArgsConstructor
public class AdminGlobalController {

    private final ChatModelService modelService;
    private final ConfigService configService;
    private final ChatMessageService messageService;
    private final SmsService smsService;
    private final MailService mailService;
    private final SeoService seoService;

    /**
     * [对话配置]获取模型配置
     *
     * @param custom
     * @return
     */
    @GetMapping("/chat/model/list")
    public R<List<AdminChatModelInfoResVO>> chatModelList(@RequestParam Integer custom) {
        return R.success(modelService.list(custom));
    }

    /**
     * [对话配置]删除模型配置
     *
     * @param id
     * @return
     */
    @GetMapping("/chat/model/remove")
    public R<Void> removeChatModel(@RequestParam Long id) {
        modelService.delete(id);
        return R.success();
    }


    /**
     * [对话配置]编辑自定义模型
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/chat/model/edit")
    public R<Void> editChatModel(@RequestBody @Validated List<AdminChatModelEditReqVo> reqVo) {
        modelService.edit(reqVo);
        return R.success();
    }

    /**
     * [对话配置]编辑模型
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/chat/model/default/edit")
    public R<Void> editDefaultChatModel(@RequestBody @Validated AdminChatModelEditReqVo reqVo) {
        modelService.editDefault(reqVo);
        return R.success();
    }



    /**
     * [对话配置]基础设置编辑
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/chat/base/edit")
    public R<Void> editChatBase(@RequestBody @Validated AdminChatBaseInfoVo reqVo) {
        ConfigInitReqVo updateReqVo = new ConfigInitReqVo();
        updateReqVo.setKey(ConfigKeyEnum.CHAT_BASE.getKey());
        updateReqVo.setName(ConfigKeyEnum.CHAT_BASE.getName());
        updateReqVo.setConfig(JSONUtil.toJsonStr(reqVo));
        configService.updateConfig(updateReqVo);
        messageService.initChatGptConfig();
        return R.success();
    }

    /**
     * [对话配置]基础设置信息
     *
     * @return
     */
    @GetMapping("/chat/base/info")
    public R<AdminChatBaseInfoVo> chatBaseInfo() {
        return R.success(configService.getConfigObject(ConfigKeyEnum.CHAT_BASE).toBean(AdminChatBaseInfoVo.class));
    }


    /**
     * [对话配置]默认预设
     *
     * @return
     */
    @GetMapping("/chat/preset/info")
    public R<AdminChatPreSetInfoVo> chatPreSetInfo() {
        return R.success(configService.getConfigObject(ConfigKeyEnum.CHAT_PRESET).toBean(AdminChatPreSetInfoVo.class));
    }


    /**
     * [对话配置]默认预设编辑
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/chat/preset/edit")
    public R<Void> editChatPreSet(@RequestBody @Validated AdminChatPreSetInfoVo reqVo) {
        ConfigInitReqVo updateReqVo = new ConfigInitReqVo();
        updateReqVo.setKey(ConfigKeyEnum.CHAT_PRESET.getKey());
        updateReqVo.setName(ConfigKeyEnum.CHAT_PRESET.getName());
        updateReqVo.setConfig(JSONUtil.toJsonStr(reqVo));
        configService.updateConfig(updateReqVo);
        return R.success();
    }


    /**
     * [全局配置]基础设置
     *
     * @return
     */
    @GetMapping("/setting/base/info")
    public R<AdminSettingBaseInfoVo> settingBaseInfo() {
        return R.success(configService.getConfigObject(ConfigKeyEnum.SETTING_BASE).toBean(AdminSettingBaseInfoVo.class));
    }


    /**
     * [全局配置]基础设置编辑
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/setting/base/edit")
    public R<Void> editSettingBase(@RequestBody @Validated AdminSettingBaseInfoVo reqVo) {
        ConfigInitReqVo updateReqVo = new ConfigInitReqVo();
        updateReqVo.setKey(ConfigKeyEnum.SETTING_BASE.getKey());
        updateReqVo.setName(ConfigKeyEnum.SETTING_BASE.getName());
        updateReqVo.setConfig(JSONUtil.toJsonStr(reqVo));
        configService.updateConfig(updateReqVo);
        mailService.initSitName();
        seoService.initSeo();
        return R.success();
    }


    /**
     * [全局配置]登录授权
     *
     * @return
     */
    @GetMapping("/setting/authLogin/info")
    public R<AdminSettingAuthLoginInfoVo> settingAuthLogin() {
        return R.success(configService.getConfigObject(ConfigKeyEnum.SETTING_LOGIN).toBean(AdminSettingAuthLoginInfoVo.class));
    }


    /**
     * [全局配置]登录授权编辑
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/setting/authLogin/edit")
    public R<Void> editAuthLogin(@RequestBody @Validated AdminSettingAuthLoginInfoVo reqVo) {
        ConfigInitReqVo updateReqVo = new ConfigInitReqVo();
        updateReqVo.setKey(ConfigKeyEnum.SETTING_LOGIN.getKey());
        updateReqVo.setName(ConfigKeyEnum.SETTING_LOGIN.getName());
        updateReqVo.setConfig(JSONUtil.toJsonStr(reqVo));
        configService.updateConfig(updateReqVo);
        return R.success();
    }



    /**
     * [全局配置]邮箱及短信
     *
     * @return
     */
    @GetMapping("/setting/email/info")
    public R<AdminSettingEmailInfoVo> settingEmailInfo() {
        return R.success(configService.getConfigObject(ConfigKeyEnum.SETTING_EMAIL).toBean(AdminSettingEmailInfoVo.class));
    }


    /**
     * [全局配置]邮箱及短信-编辑
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/setting/email/edit")
    public R<Void> editEmailInfo(@RequestBody @Validated AdminSettingEmailInfoVo reqVo) {
        ConfigInitReqVo updateReqVo = new ConfigInitReqVo();
        updateReqVo.setKey(ConfigKeyEnum.SETTING_EMAIL.getKey());
        updateReqVo.setName(ConfigKeyEnum.SETTING_EMAIL.getName());
        updateReqVo.setConfig(JSONUtil.toJsonStr(reqVo));
        configService.updateConfig(updateReqVo);
        smsService.initSmsConfig();
        mailService.initMailConfig();
        return R.success();
    }

    /**
     * [全局配置]产品支付
     *
     * @return
     */
    @GetMapping("/setting/pay/info")
    public R<AdminSettingPayInfoVo> settingPayInfo() {
        return R.success(configService.getConfigObject(ConfigKeyEnum.SETTING_PAY).toBean(AdminSettingPayInfoVo.class));
    }

    /**
     * [全局配置]产品支付-编辑
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/setting/pay/edit")
    public R<Void> editPayInfo(@RequestBody @Validated AdminSettingPayInfoVo reqVo) {
        ConfigInitReqVo updateReqVo = new ConfigInitReqVo();
        updateReqVo.setKey(ConfigKeyEnum.SETTING_PAY.getKey());
        updateReqVo.setName(ConfigKeyEnum.SETTING_PAY.getName());
        updateReqVo.setConfig(JSONUtil.toJsonStr(reqVo));
        configService.updateConfig(updateReqVo);
        return R.success();
    }




    /**
     * [全局配置]新人与邀请
     *
     * @return
     */
    @GetMapping("/setting/invite/info")
    public R<AdminSettingInviteInfoVo> settingInviteInfo() {
        return R.success(configService.getConfigObject(ConfigKeyEnum.SETTING_INVITE).toBean(AdminSettingInviteInfoVo.class));
    }

    /**
     * [全局配置]新人与邀请-编辑
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/setting/invite/edit")
    public R<Void> editInviteInfo(@RequestBody @Validated AdminSettingInviteInfoVo reqVo) {
        ConfigInitReqVo updateReqVo = new ConfigInitReqVo();
        updateReqVo.setKey(ConfigKeyEnum.SETTING_INVITE.getKey());
        updateReqVo.setName(ConfigKeyEnum.SETTING_INVITE.getName());
        updateReqVo.setConfig(JSONUtil.toJsonStr(reqVo));
        configService.updateConfig(updateReqVo);
        return R.success();
    }


    /**
     * [全局配置]提示信息
     *
     * @return
     */
    @GetMapping("/setting/prompt/info")
    public R<AdminSettingPromptInfoVo> settingPromptInfo() {
        return R.success(configService.getConfigObject(ConfigKeyEnum.SETTING_PROMPT).toBean(AdminSettingPromptInfoVo.class));
    }

    /**
     * [全局配置]提示信息-编辑
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/setting/prompt/edit")
    public R<Void> editPromptInfo(@RequestBody @Validated AdminSettingPromptInfoVo reqVo) {
        ConfigInitReqVo updateReqVo = new ConfigInitReqVo();
        updateReqVo.setKey(ConfigKeyEnum.SETTING_PROMPT.getKey());
        updateReqVo.setName(ConfigKeyEnum.SETTING_PROMPT.getName());
        updateReqVo.setConfig(JSONUtil.toJsonStr(reqVo));
        configService.updateConfig(updateReqVo);
        return R.success();
    }



    /**
     * [全局配置]公告通知
     *
     * @return
     */
    @GetMapping("/setting/notify/info")
    public R<AdminSettingNotifyInfoVo> settingNotifyInfo() {
        return R.success(configService.getConfigObject(ConfigKeyEnum.SETTING_NOTIFY).toBean(AdminSettingNotifyInfoVo.class));
    }

    /**
     * [全局配置]公告通知-编辑
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/setting/notify/edit")
    public R<Void> editPromptInfo(@RequestBody @Validated AdminSettingNotifyInfoVo reqVo) {
        ConfigInitReqVo updateReqVo = new ConfigInitReqVo();
        updateReqVo.setKey(ConfigKeyEnum.SETTING_NOTIFY.getKey());
        updateReqVo.setName(ConfigKeyEnum.SETTING_NOTIFY.getName());
        updateReqVo.setConfig(JSONUtil.toJsonStr(reqVo));
        configService.updateConfig(updateReqVo);
        return R.success();
    }

    /**
     * [全局配置]脚本样式
     *
     * @return
     */
    @GetMapping("/setting/style/info")
    public R<AdminSettingStyleInfoVo> settingStyleInfo() {
        return R.success(configService.getConfigObject(ConfigKeyEnum.SETTING_STYLE).toBean(AdminSettingStyleInfoVo.class));
    }

    /**
     * [全局配置]脚本样式-编辑
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/setting/style/edit")
    public R<Void> editStyleInfo(@RequestBody @Validated AdminSettingStyleInfoVo reqVo) {
        ConfigInitReqVo updateReqVo = new ConfigInitReqVo();
        updateReqVo.setKey(ConfigKeyEnum.SETTING_STYLE.getKey());
        updateReqVo.setName(ConfigKeyEnum.SETTING_STYLE.getName());
        updateReqVo.setConfig(JSONUtil.toJsonStr(reqVo));
        configService.updateConfig(updateReqVo);
        seoService.initSeo();
        return R.success();
    }


    /**
     * [全局配置]AI工具
     *
     * @return
     */
    @GetMapping("/setting/tool/info")
    public R<AdminSettingToolInfoVo> settingToolInfo() {
        return R.success(configService.getConfigObject(ConfigKeyEnum.SETTING_TOOL).toBean(AdminSettingToolInfoVo.class));
    }

    /**
     * [全局配置]AI工具-编辑
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/setting/tool/edit")
    public R<Void> editToolInfo(@RequestBody @Validated AdminSettingToolInfoVo reqVo) {
        ConfigInitReqVo updateReqVo = new ConfigInitReqVo();
        updateReqVo.setKey(ConfigKeyEnum.SETTING_TOOL.getKey());
        updateReqVo.setName(ConfigKeyEnum.SETTING_TOOL.getName());
        updateReqVo.setConfig(JSONUtil.toJsonStr(reqVo));
        configService.updateConfig(updateReqVo);
        seoService.initSeo();
        return R.success();
    }


    /**
     * [绘画配置]通用配置
     *
     * @return
     */
    @GetMapping("/image/common/info")
    public R<AdminImageCommonInfoVo> imageCommonInfo() {
        return R.success(configService.getConfigObject(ConfigKeyEnum.IMAGE_COMMON).toBean(AdminImageCommonInfoVo.class));
    }

    /**
     * [绘画配置]通用配置-编辑
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/image/common/edit")
    public R<Void> editImageCommonInfo(@RequestBody @Validated AdminImageCommonInfoVo reqVo) {
        ConfigInitReqVo updateReqVo = new ConfigInitReqVo();
        updateReqVo.setKey(ConfigKeyEnum.IMAGE_COMMON.getKey());
        updateReqVo.setName(ConfigKeyEnum.IMAGE_COMMON.getName());
        updateReqVo.setConfig(JSONUtil.toJsonStr(reqVo));
        configService.updateConfig(updateReqVo);
        return R.success();
    }


    /**
     * [绘画配置]图片展示方案
     *
     * @return
     */
    @GetMapping("/image/show/info")
    public R<AdminImageShowInfoVo> imageShowInfo() {
        return R.success(configService.getConfigObject(ConfigKeyEnum.IMAGE_SHOW).toBean(AdminImageShowInfoVo.class));
    }

    /**
     * [绘画配置]图片展示方案-编辑
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/image/show/edit")
    public R<Void> editImageShowInfo(@RequestBody @Validated AdminImageShowInfoVo reqVo) {
        ConfigInitReqVo updateReqVo = new ConfigInitReqVo();
        updateReqVo.setKey(ConfigKeyEnum.IMAGE_SHOW.getKey());
        updateReqVo.setName(ConfigKeyEnum.IMAGE_SHOW.getName());
        updateReqVo.setConfig(JSONUtil.toJsonStr(reqVo));
        configService.updateConfig(updateReqVo);
        return R.success();
    }

    /**
     * [绘画配置]代理设置
     *
     * @return
     */
    @GetMapping("/image/proxy/info")
    public R<AdminImageProxyInfoVo> imageProxyInfo() {
        return R.success(configService.getConfigObject(ConfigKeyEnum.IMAGE_PROXY).toBean(AdminImageProxyInfoVo.class));
    }

    /**
     * [绘画配置]代理设置-编辑
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/image/proxy/edit")
    public R<Void> editImageProxyInfo(@RequestBody @Validated AdminImageProxyInfoVo reqVo) {
        ConfigInitReqVo updateReqVo = new ConfigInitReqVo();
        updateReqVo.setKey(ConfigKeyEnum.IMAGE_PROXY.getKey());
        updateReqVo.setName(ConfigKeyEnum.IMAGE_PROXY.getName());
        updateReqVo.setConfig(JSONUtil.toJsonStr(reqVo));
        configService.updateConfig(updateReqVo);
        return R.success();
    }


    /**
     * [绘画配置]模型消耗
     *
     * @return
     */
    @GetMapping("/image/consume/info")
    public R<AdminImageConsumeInfoVo> imageConsumeInfo() {
        return R.success(configService.getConfigObject(ConfigKeyEnum.IMAGE_CONSUME).toBean(AdminImageConsumeInfoVo.class));
    }

    /**
     * [绘画配置]模型消耗-编辑
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/image/consume/edit")
    public R<Void> editImageConsumeInfo(@RequestBody @Validated AdminImageConsumeInfoVo reqVo) {
        ConfigInitReqVo updateReqVo = new ConfigInitReqVo();
        updateReqVo.setKey(ConfigKeyEnum.IMAGE_CONSUME.getKey());
        updateReqVo.setName(ConfigKeyEnum.IMAGE_CONSUME.getName());
        updateReqVo.setConfig(JSONUtil.toJsonStr(reqVo));
        configService.updateConfig(updateReqVo);
        return R.success();
    }


    /**
     * [安全审核]基础设置
     *
     * @return
     */
    @GetMapping("/safe/base/info")
    public R<AdminSafeBasenfoVo> safeBaseInfo() {
        return R.success(configService.getConfigObject(ConfigKeyEnum.SAFE_BASE).toBean(AdminSafeBasenfoVo.class));
    }

    /**
     * [安全审核]基础设置-编辑
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/safe/base/edit")
    public R<Void> editSafeBaseInfo(@RequestBody @Validated AdminSafeBasenfoVo reqVo) {
        ConfigInitReqVo updateReqVo = new ConfigInitReqVo();
        updateReqVo.setKey(ConfigKeyEnum.SAFE_BASE.getKey());
        updateReqVo.setName(ConfigKeyEnum.SAFE_BASE.getName());
        updateReqVo.setConfig(JSONUtil.toJsonStr(reqVo));
        configService.updateConfig(updateReqVo);
        return R.success();
    }



    /**
     * [安全审核]第三方平台
     *
     * @return
     */
    @GetMapping("/safe/third/info")
    public R<AdminSafeThirdInfoVo> safeThirdInfo() {
        return R.success(configService.getConfigObject(ConfigKeyEnum.SAFE_THIRD).toBean(AdminSafeThirdInfoVo.class));
    }

    /**
     * [安全审核]第三方平台-编辑
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/safe/third/edit")
    public R<Void> editSafeThirdInfo(@RequestBody @Validated AdminSafeThirdInfoVo reqVo) {
        ConfigInitReqVo updateReqVo = new ConfigInitReqVo();
        updateReqVo.setKey(ConfigKeyEnum.SAFE_THIRD.getKey());
        updateReqVo.setName(ConfigKeyEnum.SAFE_THIRD.getName());
        updateReqVo.setConfig(JSONUtil.toJsonStr(reqVo));
        configService.updateConfig(updateReqVo);
        return R.success();
    }

    /**
     * [菜单配置]用户菜单
     *
     * @return
     */
    @GetMapping("/menu/user/info")
    public R<AdminMenuUserInfoVo> menuUserInfo() {
        return R.success(configService.getConfigObject(ConfigKeyEnum.MENU_USER).toBean(AdminMenuUserInfoVo.class));
    }

    /**
     * [菜单配置]用户菜单-编辑
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/menu/user/edit")
    public R<Void> editMenuUserInfo(@RequestBody @Validated AdminMenuUserInfoVo reqVo) {
        ConfigInitReqVo updateReqVo = new ConfigInitReqVo();
        updateReqVo.setKey(ConfigKeyEnum.MENU_USER.getKey());
        updateReqVo.setName(ConfigKeyEnum.MENU_USER.getName());
        updateReqVo.setConfig(JSONUtil.toJsonStr(reqVo));
        configService.updateConfig(updateReqVo);
        return R.success();
    }

    /**
     * [协议内容]用户及隐私协议
     *
     * @return
     */
    @GetMapping("/protocol/privacy/info")
    public R<AdminProtocolPrivacyInfoVo> protocolPrivacyInfo() {
        return R.success(configService.getConfigObject(ConfigKeyEnum.PROTOCOL_PRIACY).toBean(AdminProtocolPrivacyInfoVo.class));
    }

    /**
     * [协议内容]用户及隐私协议-编辑
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/protocol/privacy/edit")
    public R<Void> editProtocolPrivacyInfo(@RequestBody @Validated AdminProtocolPrivacyInfoVo reqVo) {
        ConfigInitReqVo updateReqVo = new ConfigInitReqVo();
        updateReqVo.setKey(ConfigKeyEnum.PROTOCOL_PRIACY.getKey());
        updateReqVo.setName(ConfigKeyEnum.PROTOCOL_PRIACY.getName());
        updateReqVo.setConfig(JSONUtil.toJsonStr(reqVo));
        configService.updateConfig(updateReqVo);
        return R.success();
    }
}
