package org.sia.controller.client;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.sia.exception.BusinessException;
import org.sia.service.UserService;
import org.sia.tool.IpTool;
import org.sia.vo.R;
import org.sia.enums.ResultEnum;
import org.sia.vo.request.*;
import org.sia.vo.response.CaptchaResVo;
import org.sia.vo.response.UserInfoResVo;
import org.sia.vo.response.UserLoginResVo;
import org.sia.vo.response.UserPreLoginResVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/4 10:17
 */
@RequestMapping("/v1/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 发送邮件
     *
     * @return
     */
    @PostMapping("/email/send")
    public R<String> sendEmail(@RequestBody @Validated UserEmailSendReqVo reqVo) {
        switch (reqVo.getType()) {
            case "signIn":
                userService.sendSignInEmail(reqVo.getEmail());
                break;
            case "login":
                userService.sendLoginEmail(reqVo.getEmail());
                break;
            case "bind":
                userService.sendBindEmail(reqVo.getEmail());
                break;
            case "back":
                userService.sendBackEmail(reqVo.getEmail());
                break;
            default:
                throw new BusinessException(ResultEnum.NOT_SUPPORT_TYPE);
        }
        return R.success("邮件已发送");
    }

    /**
     * 发送手机短信
     *
     * @return
     */
    @PostMapping("/phone/send")
    public R<String> sendPhone(@RequestBody @Validated UserSmsSendReqVo reqVo) {
        switch (reqVo.getType()) {
            case "signIn": // 注册验证码
                userService.sendSignInPhone(reqVo.getPhone());
                break;
            case "login":// 登录验证码
                userService.sendLoginPhone(reqVo.getPhone());
                break;
            case "bind":// 绑定手机号验证码
                userService.sendBindPhone(reqVo.getPhone());
                break;
            case "back":// 找回密码验证码
                userService.sendBackPhone(reqVo.getPhone());
                break;
            default:
                throw new BusinessException(ResultEnum.NOT_SUPPORT_TYPE);
        }
        return R.success("短信验证码已发送");
    }

    /**
     * 图形验证码
     *
     * @return
     */
    @GetMapping("/captcha")
    public R<CaptchaResVo> captcha() {
        return R.success(userService.getCaptcha());
    }

    /**
     * 邮箱/手机号注册
     *
     * @return
     */
    @PostMapping("/email/signIn")
    public R<String> signInEmail(@RequestBody @Validated UserSignInReqVo reqVo) {
        if (StrUtil.isAllBlank(reqVo.getPhone(),reqVo.getEmail())){
            throw new BusinessException(ResultEnum.ERROR,"请填写邮箱或手机号");
        }
        if (StrUtil.isNotBlank(reqVo.getEmail())){
            userService.signInEmail(reqVo);
        }else {
            userService.signInPhone(reqVo);
        }
        return R.success("注册成功");
    }


    /**
     * 密码登录
     *
     * @return
     */
    @PostMapping("/login/password")
    public R<UserLoginResVo> loginPwd(@RequestBody @Validated UserLoginPwdReqVo reqVo, HttpServletRequest request) {
        return R.success(userService.loginPwd(reqVo, IpTool.getIpAddress(request)));
    }


    /**
     * 验证码登录
     *
     * @return
     */
    @PostMapping("/login/verifyCode")
    public R<UserLoginResVo> loginVerifyCode(@RequestBody @Validated UserLoginVerifyCodeReqVo reqVo, HttpServletRequest request) {
        return R.success(userService.loginVerifyCode(reqVo, IpTool.getIpAddress(request)));
    }

    /**
     * 找回密码
     * @return
     */
    @PostMapping("/password/forget")
    public R<String> forgetPwd(@RequestBody @Validated  UserForgetPwdReqVo reqVo) {
        userService.forgetPwd(reqVo);
        return R.success("找回密码成功");
    }

    /**
     * 登录用户信息
     *
     * @return
     */
    @GetMapping("/info")
    public R<UserInfoResVo> info() {
        return R.success(userService.info());
    }


    /**
     * 修改用户信息
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/info/modify")
    public R<String> modifyUser(@RequestBody @Validated UserModifyReqVo reqVo) {
        userService.modify(reqVo);
        return R.success("修改用户信息成功");
    }


    /**
     * 修改密码
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/info/modifyPwd")
    public R<String> modifyPwd(@RequestBody @Validated UserModifyPwdReqVo reqVo) {
        userService.modifyPwd(reqVo);
        return R.success();
    }

    /**
     * 切换主题
     *
     * @param mode
     * @return
     */
    @GetMapping("/theme/change")
    public R<Void> changeTheme(@RequestParam String mode) {
        userService.changeTheme(mode);
        return R.success();
    }


    /**
     * 手机号绑定
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/bind/phone")
    public R<Void> bindPhone(@RequestBody @Validated UserBindPhoneReqVo reqVo) {
        userService.bindPhone(reqVo);
        return R.success();
    }

    /**
     * 邮箱绑定
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/bind/email")
    public R<Void> bindEmail(@RequestBody @Validated UserBindEmailReqVo reqVo) {
        userService.bindEmail(reqVo);
        return R.success();
    }

    /**
     * 微信公众号登录回调
     * @param body
     * @return
     */
    @PostMapping("/login/wechatPublic")
    public String wechatPublicLogin(@RequestBody String body) {
        return userService.loginWechatPublic(body);
    }

    /**
     * 微信公众号预登录/绑定信息
     * @return
     */
    @GetMapping("/preLogin/wechatPublic")
    public R<UserPreLoginResVo> wechatPublicReLogin() {
        return R.success(userService.preLoginWechatPublic());
    }

    /**
     * 微信公众号获取登录信息
     * @return
     */
    @GetMapping("/login/wechat/token")
    public R<UserLoginResVo> wechatLoginToken(@RequestParam String code) {
        return R.success(userService.wechatLoginToken(code));
    }

    /**
     * 微信公众号获取绑定信息
     * @return
     */
    @GetMapping("/bind/wechat")
    public R<Boolean> wechatBind(@RequestParam String code) {
        return R.success(userService.bindWechat(code));
    }



    /**
     * 微信公众号验证TOKEN
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    @GetMapping("/login/wechatPublic")
    public String checkWechatPublicLogin(
            @RequestParam String signature,
            @RequestParam String timestamp,
            @RequestParam String nonce,
            @RequestParam String echostr
    ) {
        return userService.checkWechatPublicToken(signature,timestamp,nonce,echostr);
    }


}
