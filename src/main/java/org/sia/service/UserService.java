package org.sia.service;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.*;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sia.enums.ConfigKeyEnum;
import org.sia.enums.ResultEnum;
import org.sia.enums.UserStateEnum;
import org.sia.exception.BusinessException;
import org.sia.handler.RequestDataHandler;
import org.sia.mapper.UserMapper;
import org.sia.model.InviteRecord;
import org.sia.model.User;
import org.sia.model.UserPackage;
import org.sia.tool.WeChatTool;
import org.sia.vo.AdminSettingAuthLoginInfoVo;
import org.sia.vo.AdminSettingInviteInfoVo;
import org.sia.vo.InviteDto;
import org.sia.vo.PageReqVo;
import org.sia.vo.request.*;
import org.sia.vo.response.*;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/8 21:01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService extends ServiceImpl<UserMapper, User> {
    private final MailService mailService;
    private final UserPackageService userPackageService;
    private final ConfigService configService;
    private final SmsService smsService;

    private static final byte[] SINGER_KEY = "SiaAI@2023#".getBytes(StandardCharsets.UTF_8);

    /**
     * 图形验证码：默认5分钟过期
     */
    private static final TimedCache<String, LineCaptcha> captchaCache = CacheUtil.newTimedCache(300000);
    /**
     * 注册登录验证码：默认5分钟过期
     */
    private static final TimedCache<String, String> codeCache = CacheUtil.newTimedCache(300000);
    /**
     * 绑定账号验证码: 默认5分钟过期
     */
    private static final TimedCache<String, String> bindCache = CacheUtil.newTimedCache(300000);
    /**
     * 微信公众号验证码：默认5分钟过期
     */
    private static final TimedCache<String, String> wxCodeCache = CacheUtil.newTimedCache(300000);
    /**
     * 微信公众号登录回调结果：默认5分钟过期
     */
    private static final TimedCache<String, String> wxUserLoginCache = CacheUtil.newTimedCache(300000);

    public void sendSignInEmail(String email) {
        this.checkOpenEmail();
        // 判断是否已发送
        User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getEmail, email));
        // 已注册
        if (ObjectUtil.isNotNull(user)) {
            throw new BusinessException(ResultEnum.HAS_SIGN_IN, "邮箱");
        }
        // 发送注册邮件
        String code = RandomUtil.randomNumbers(6);
        mailService.sendVerifyCode(email, code);
        // 保存验证码
        codeCache.put(email, code);
    }

    /**
     * 注册用户
     *
     * @param keyword
     * @return
     */
    private User registerUser(String keyword) {
        User user = new User();
        if (ReUtil.isMatch(PatternPool.EMAIL, keyword)) {
            user.setEmail(keyword);
        }
        if (ReUtil.isMatch(PatternPool.MOBILE, keyword)) {
            user.setPhone(keyword);
        }
        if (keyword.length() == 28) {
            user.setOpenId(keyword);
        }
        user.setUserId(IdUtil.getSnowflakeNextIdStr());
        user.setAvatar("b1");
        user.setNickName("用户" + RandomUtil.randomString(6));
        user.setState(UserStateEnum.SIGN_IN.getCode());
        user.setCreateTime(LocalDateTime.now());
        this.save(user);
        return user;
    }

    public void sendLoginEmail(String email) {
        this.checkOpenEmail();
        // 判断用户是否已注册
        User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getEmail, email));
        // 邮箱未注册
        if (ObjectUtil.isNull(user)) {
            throw new BusinessException(ResultEnum.ERROR, "邮箱未注册");
        }
        // 冻结中的用户
        if (UserStateEnum.FREEZE.getCode().equals(user.getState())) {
            throw new BusinessException(ResultEnum.EMAIL_FREEZE);
        }
        // 验证码是否已过期
        if (StrUtil.isNotBlank(codeCache.get(user.getUserId(), false))) {
            throw new BusinessException(ResultEnum.REPEAT_SEND_MAIL);
        }
        // 发送登录验证码邮件
        String code = RandomUtil.randomNumbers(6);
        // 保存验证码
        codeCache.put(user.getUserId(), code);
        mailService.sendLoginCode(email, code);
    }

    public void sendSignInPhone(String phone) {
        this.checkOpenPhone();
        // 判断是否已发送
        User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getPhone, phone));
        // 已注册
        if (ObjectUtil.isNotNull(user)) {
            throw new BusinessException(ResultEnum.HAS_SIGN_IN, "手机号");
        }
        // 发送注册短信验证码
        String code = RandomUtil.randomNumbers(6);
        smsService.sendRegister(phone, code);
        codeCache.put(phone, code);
    }


    public void sendLoginPhone(String phone) {
        this.checkOpenPhone();
        // 判断用户是否已注册
        User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getPhone, phone));
        // 邮箱未注册
        if (ObjectUtil.isNull(user)) {
            throw new BusinessException(ResultEnum.ERROR, "手机号未注册");
        }
        // 冻结中的用户
        if (UserStateEnum.FREEZE.getCode().equals(user.getState())) {
            throw new BusinessException(ResultEnum.EMAIL_FREEZE);
        }
        // 验证码是否已过期
        if (StrUtil.isNotBlank(codeCache.get(user.getUserId(), false))) {
            throw new BusinessException(ResultEnum.REPEAT_SEND_MAIL);
        }
        // 发送登录验证码邮件
        String code = RandomUtil.randomNumbers(6);
        // 保存验证码
        codeCache.put(user.getUserId(), code);
        smsService.sendLogin(phone, code);
    }

    public void signInEmail(UserSignInReqVo reqVo) {
        this.checkOpenEmail();
        // 检查邮箱是否被注册
        long count = this.count(Wrappers.<User>lambdaQuery().eq(User::getEmail, reqVo.getEmail()));
        if (count > 0) {
            throw new BusinessException(ResultEnum.HAS_SIGN_IN, "邮箱");
        }
        // 检查验证码是否正确
        String code = codeCache.get(reqVo.getEmail(), false);
        if (StrUtil.isBlank(code)) {
            throw new BusinessException(ResultEnum.ERROR, "验证码已过期");
        }
        if (!code.equals(reqVo.getCode())) {
            throw new BusinessException(ResultEnum.ERROR, "验证码错误");
        }
        // 检查图形验证码
        if (!captchaCache.containsKey(reqVo.getCaptchaCode())
                || !captchaCache.get(reqVo.getCaptchaCode(), false).verify(reqVo.getCaptcha())) {
            throw new BusinessException(ResultEnum.ERROR, "图形验证码错误");
        }
        // 创建用户
        this.createUser(reqVo.getEmail(), reqVo.getPassword(), reqVo.getInviteCode());
    }


    public void signInPhone(UserSignInReqVo reqVo) {
        this.checkOpenPhone();
        // 检查手机号是否被注册
        long count = this.count(Wrappers.<User>lambdaQuery().eq(User::getPhone, reqVo.getPhone()));
        if (count > 0) {
            throw new BusinessException(ResultEnum.HAS_SIGN_IN, "手机号");
        }
        // 检查验证码是否正确
        String code = codeCache.get(reqVo.getPhone(), false);
        if (StrUtil.isBlank(code)) {
            throw new BusinessException(ResultEnum.ERROR, "验证码已过期");
        }
        if (!code.equals(reqVo.getCode())) {
            throw new BusinessException(ResultEnum.ERROR, "验证码错误");
        }
        // 检查图形验证码
        if (!captchaCache.containsKey(reqVo.getCaptchaCode())
                || !captchaCache.get(reqVo.getCaptchaCode(), false).verify(reqVo.getCaptcha())) {
            throw new BusinessException(ResultEnum.ERROR, "图形验证码错误");
        }
        // 保存账户信息
        this.createUser(reqVo.getPhone(), reqVo.getPassword(), reqVo.getInviteCode());
    }

    private User createUser(String account, String password, String inviteCode) {
        User user = new User();
        if (ReUtil.isMatch(PatternPool.EMAIL, account)) {
            user.setEmail(account);
        }
        if (ReUtil.isMatch(PatternPool.MOBILE, account)) {
            user.setPhone(account);
        }
        if (account.length() == 28) {
            user.setOpenId(account);
        }
        user.setUserId(IdUtil.getSnowflakeNextIdStr());
        user.setAvatar("b1");
        user.setNickName("用户" + RandomUtil.randomString(6));
        user.setState(UserStateEnum.NORMAL.getCode());
        user.setCreateTime(LocalDateTime.now());
        // 密码
        user.setPassword(password);
        // 邀请码
        user.setInviteCode(IdUtil.nanoId(4));
        // 默认普通用户
        user.setIsAdmin(0);
        this.save(user);
        // 新用户注册套餐包
        userPackageService.bindNewUser(user.getUserId());
        // 邀请码注册的双方增加积分
        this.inviteUser(inviteCode, user.getUserId());
        return user;
    }


    private void inviteUser(String inviteCode, String userId) {
        if (StrUtil.isBlank(inviteCode)) {
            return;
        }
        AdminSettingInviteInfoVo config = configService.getConfigObject(ConfigKeyEnum.SETTING_INVITE).toBean(AdminSettingInviteInfoVo.class);
        InviteDto inviteDto = new InviteDto();
        User inviter = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getInviteCode, inviteCode));
        // 生成套餐记录
        inviteDto.setFrom(inviter.getUserId());
        inviteDto.setTo(userId);
        inviteDto.setIntegral(config.getIIntegral().longValue());
        inviteDto.setExpireDay(config.getIExpireDay().longValue());
        userPackageService.bindUser(inviteDto);
        // 生成邀请记录
        InviteRecord record = new InviteRecord();
        record.setFrom(inviter.getUserId());
        record.setTo(userId);
        record.setCreateTime(LocalDateTime.now());
        SpringUtil.getBean(InviteService.class).save(record);
    }

    public CaptchaResVo getCaptcha() {
        //定义图形验证码的长、宽、验证码字符数、干扰线宽度
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(100, 34, 4, 4);
        //图形验证码写出，可以写出到文件，也可以写出到流
        CaptchaResVo result = new CaptchaResVo();
        String code = IdUtil.nanoId();
        result.setCode(code);
        result.setBase64(captcha.getImageBase64());
        captchaCache.put(code, captcha);
        return result;
    }

    public UserLoginResVo loginPwd(UserLoginPwdReqVo reqVo, String ip) {
        if (StrUtil.isAllBlank(reqVo.getEmail(), reqVo.getPhone())) {
            throw new BusinessException(ResultEnum.ERROR, "请填写手机号或邮箱");
        }
        if (StrUtil.isNotBlank(reqVo.getPhone())) {
            this.checkOpenPhone();
        }
        if (StrUtil.isNotBlank(reqVo.getEmail())) {
            this.checkOpenEmail();
        }
        // 校验图形验证码
        if (!captchaCache.containsKey(reqVo.getCaptchaCode())
                || !captchaCache.get(reqVo.getCaptchaCode(), false).verify(reqVo.getCaptcha())) {
            throw new BusinessException(ResultEnum.ERROR, "图形验证码错误");
        }
        // 查询账号是否存在
        User user = this.getOne(Wrappers.<User>lambdaQuery()
                .eq(StrUtil.isNotBlank(reqVo.getEmail()), User::getEmail, reqVo.getEmail())
                .eq(StrUtil.isNotBlank(reqVo.getPhone()), User::getPhone, reqVo.getPhone())
        );
        if (ObjectUtil.isNull(user) || user.getState().equals(UserStateEnum.SIGN_IN.getCode())) {
            throw new BusinessException(ResultEnum.ERROR, "账户未注册");
        }
        // 查询账号是否被冻结
        if (user.getState().equals(UserStateEnum.FREEZE.getCode())) {
            throw new BusinessException(ResultEnum.ERROR, "账户已被冻结");
        }
        // 校验密码
        if (!user.getPassword().equals(reqVo.getPassword())) {
            throw new BusinessException(ResultEnum.ERROR, "密码错误");
        }
        user.setIp(ip);
        // 生成token
        return this.createToken(user);
    }

    private UserLoginResVo createToken(User user) {
        UserLoginResVo result = new UserLoginResVo();
        // 获取登录授权有效天数
        AdminSettingAuthLoginInfoVo config = configService.getConfigObject(ConfigKeyEnum.SETTING_LOGIN).toBean(AdminSettingAuthLoginInfoVo.class);
        if (ObjectUtil.isNull(config)) {
            throw new BusinessException(ResultEnum.ERROR, "请配置[站点设置]->[全局配置]->登录授权");
        }
        DateTime expireTime = DateUtil.offsetDay(DateUtil.date(), config.getExpireDay());
        result.setExpireTime(expireTime.toString());
        result.setToken(JWT.create()
                .setExpiresAt(expireTime)
                .addPayloads(MapUtil.builder("userId", user.getUserId())
                        .put("isAdmin", user.getIsAdmin().toString())
                        .build())
                .setKey(SINGER_KEY)
                .sign());
        // 更新最后登录时间
        user.setLastTime(LocalDateTime.now());
        if (StrUtil.isNotBlank(user.getIp())) {
            user.setIp(StrUtil.split(user.getIp(), ':').get(0));
        }
        this.updateById(user);
        return result;
    }


    public boolean verifyToken(String token) {
        try {
            JWTValidator.of(token)
                    .validateDate();
            // 验证 token是否有效
            return JWTUtil.verify(token, SINGER_KEY);
        } catch (Exception e) {
            log.error(">>> token解析异常", e);
            return false;
        }
    }

    public Map parseToken(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        return jwt.getPayloads().toBean(Map.class);
    }

    public UserLoginResVo loginVerifyCode(UserLoginVerifyCodeReqVo reqVo, String ip) {
        if (StrUtil.isNotBlank(reqVo.getPhone())) {
            this.checkOpenPhone();
        }
        if (StrUtil.isNotBlank(reqVo.getEmail())) {
            this.checkOpenEmail();
        }
        if (StrUtil.isAllBlank(reqVo.getEmail(), reqVo.getPhone())) {
            throw new BusinessException(ResultEnum.ERROR, "请填写手机号或邮箱");
        }
        // 校验图形验证码
        if (!captchaCache.containsKey(reqVo.getCaptchaCode())
                || !captchaCache.get(reqVo.getCaptchaCode(), false).verify(reqVo.getCaptcha())) {
            throw new BusinessException(ResultEnum.ERROR, "图形验证码错误");
        }
        // 查询邮箱账号是否存在
        User user = this.getOne(Wrappers.<User>lambdaQuery()
                .eq(StrUtil.isNotBlank(reqVo.getEmail()), User::getEmail, reqVo.getEmail())
                .eq(StrUtil.isNotBlank(reqVo.getPhone()), User::getPhone, reqVo.getPhone())
        );
        if (ObjectUtil.isNull(user) || user.getState().equals(UserStateEnum.SIGN_IN.getCode())) {
            throw new BusinessException(ResultEnum.ERROR, "账户未注册");
        }
        // 查询邮箱账号是否被冻结
        if (user.getState().equals(UserStateEnum.FREEZE.getCode())) {
            throw new BusinessException(ResultEnum.ERROR, "账户已被冻结");
        }
        // 校验密码
        String code = codeCache.get(user.getUserId(), false);
        if (StrUtil.isBlank(code)) {
            throw new BusinessException(ResultEnum.ERROR, "验证码已过期");
        }
        if (!code.equals(reqVo.getCode())) {
            throw new BusinessException(ResultEnum.ERROR, "验证码错误");
        }
        user.setIp(ip);
        // 生成token
        return this.createToken(user);
    }


    public UserInfoResVo info() {
        User user = this.getById(RequestDataHandler.getUserId());
        if (ObjectUtil.isNull(user)) {
            throw new BusinessException(ResultEnum.DATA_EMPTY, "用户");
        }
        return BeanUtil.toBean(user, UserInfoResVo.class);
    }

    public void modify(UserModifyReqVo reqVo) {
        this.update(Wrappers.<User>lambdaUpdate()
                .eq(User::getUserId, RequestDataHandler.getUserId())
                .set(StrUtil.isNotBlank(reqVo.getPhone()), User::getPhone, reqVo.getPhone())
                .set(StrUtil.isNotBlank(reqVo.getAvatar()), User::getAvatar, reqVo.getAvatar())
                .set(StrUtil.isNotBlank(reqVo.getNickName()), User::getNickName, reqVo.getNickName())
        );
    }

    public void modifyPwd(UserModifyPwdReqVo reqVo) {
        String userId = RequestDataHandler.getUserId();
        // 校验原始密码是否正确
        User user = this.getById(userId);
        if (!user.getPassword().equals(reqVo.getOldPwd())) {
            throw new BusinessException(ResultEnum.ERROR, "原密码输入错误");
        }
        this.update(Wrappers.<User>lambdaUpdate()
                .eq(User::getEmail, userId)
                .set(User::getPassword, reqVo.getNewPwd())
        );
    }

    public Page<AdminUserSearchResVo> searchList(PageReqVo<AdminUserSearchReqVo> reqVo) {
        return this.baseMapper.searchList(reqVo.getPage(), reqVo.getCondition());
    }

    public AdminUserSearchResVo searchInfo(String userId) {
        return this.baseMapper.info(userId);
    }

    public void edit(AdminUserEditReqVo reqVo) {
        User user = BeanUtil.toBean(reqVo, User.class);
        this.updateById(user);
    }

    public void resetPassword(AdminPwdResetReqVo reqVo) {
        User user = this.getById(reqVo.getUserId());
        if (ObjectUtil.isNull(user)) {
            throw new BusinessException(ResultEnum.DATA_EMPTY, "用户");
        }
        user.setPassword(reqVo.getPassword());
        this.updateById(user);
    }

    public Page<AdminUpResVo> searchUpList(PageReqVo<AdminUpReqVo> reqVo) {
        return userPackageService.getBaseMapper().searchList(reqVo.getPage(), reqVo.getCondition());
    }

    public void removeUp(Long id) {
        userPackageService.removeById(id);
    }

    public void editUp(AdminUpEditReqVo reqVo) {
        UserPackage up = BeanUtil.toBean(reqVo, UserPackage.class);
        userPackageService.updateById(up);
    }

    public void changeTheme(String mode) {
        this.update(Wrappers.<User>lambdaUpdate().eq(User::getUserId, RequestDataHandler.getUserId()).set(User::getThemeMode, mode));
    }


    public void bindPhone(UserBindPhoneReqVo reqVo) {
        this.checkOpenPhone();
        // 校验图形验证码
        if (!captchaCache.containsKey(reqVo.getCaptchaCode())
                || !captchaCache.get(reqVo.getCaptchaCode(), false).verify(reqVo.getCaptcha())) {
            throw new BusinessException(ResultEnum.ERROR, "图形验证码错误");
        }
        // 查询账号是否存在
        User user = this.getById(RequestDataHandler.getUserId());
        if (ObjectUtil.isNull(user) || user.getState().equals(UserStateEnum.SIGN_IN.getCode())) {
            throw new BusinessException(ResultEnum.ERROR, "账户未注册");
        }
        // 查询账号是否被冻结
        if (user.getState().equals(UserStateEnum.FREEZE.getCode())) {
            throw new BusinessException(ResultEnum.ERROR, "账户已被冻结");
        }
        // 校验短信验证码
        String code = bindCache.get(reqVo.getPhone(), false);
        if (StrUtil.isBlank(code)) {
            throw new BusinessException(ResultEnum.ERROR, "验证码已过期");
        }
        if (!code.equals(reqVo.getCode())) {
            throw new BusinessException(ResultEnum.ERROR, "验证码错误");
        }
        // 绑定手机号
        this.update(Wrappers.<User>lambdaUpdate()
                .eq(User::getUserId, user.getUserId())
                .set(User::getPhone, reqVo.getPhone())
        );
    }

    public void bindEmail(UserBindEmailReqVo reqVo) {
        this.checkOpenEmail();
        // 校验图形验证码
        if (!captchaCache.containsKey(reqVo.getCaptchaCode())
                || !captchaCache.get(reqVo.getCaptchaCode(), false).verify(reqVo.getCaptcha())) {
            throw new BusinessException(ResultEnum.ERROR, "图形验证码错误");
        }
        // 查询账号是否存在
        User user = this.getById(RequestDataHandler.getUserId());
        if (ObjectUtil.isNull(user) || user.getState().equals(UserStateEnum.SIGN_IN.getCode())) {
            throw new BusinessException(ResultEnum.ERROR, "账户未注册");
        }
        // 查询账号是否被冻结
        if (user.getState().equals(UserStateEnum.FREEZE.getCode())) {
            throw new BusinessException(ResultEnum.ERROR, "账户已被冻结");
        }
        // 校验邮箱验证码
        String code = bindCache.get(reqVo.getEmail());
        if (StrUtil.isBlank(code)) {
            throw new BusinessException(ResultEnum.ERROR, "验证码已过期");
        }
        if (!code.equals(reqVo.getCode())) {
            throw new BusinessException(ResultEnum.ERROR, "验证码错误");
        }
        // 绑定邮箱
        this.update(Wrappers.<User>lambdaUpdate()
                .eq(User::getUserId, user.getUserId())
                .set(User::getEmail, reqVo.getEmail())
        );
    }

    private void checkOpenWxPublic() {
        // 手机验证服务是否开启
        AdminSettingAuthLoginInfoVo config = configService.getConfigObject(ConfigKeyEnum.SETTING_LOGIN).toBean(AdminSettingAuthLoginInfoVo.class);
        if (ObjectUtil.isNull(config)) {
            throw new BusinessException(ResultEnum.ERROR, "请配置[站点设置]->[全局配置]->登录授权");
        }
        if (!config.getOpenWxPublic()) {
            throw new BusinessException(ResultEnum.ERROR, "未开启微信公众号注册登录服务");
        }
    }

    private void checkOpenPhone() {
        // 手机验证服务是否开启
        AdminSettingAuthLoginInfoVo config = configService.getConfigObject(ConfigKeyEnum.SETTING_LOGIN).toBean(AdminSettingAuthLoginInfoVo.class);
        if (ObjectUtil.isNull(config)) {
            throw new BusinessException(ResultEnum.ERROR, "请配置[站点设置]->[全局配置]->登录授权");
        }
        if (!config.getOpenPhone()) {
            throw new BusinessException(ResultEnum.ERROR, "未开启手机号码注册登录服务");
        }
    }

    private void checkOpenEmail() {
        // 邮箱验证服务是否开启
        AdminSettingAuthLoginInfoVo config = configService.getConfigObject(ConfigKeyEnum.SETTING_LOGIN).toBean(AdminSettingAuthLoginInfoVo.class);
        if (ObjectUtil.isNull(config)) {
            throw new BusinessException(ResultEnum.ERROR, "请配置[站点设置]->[全局配置]->登录授权");
        }
        if (!config.getOpenEmail()) {
            throw new BusinessException(ResultEnum.ERROR, "未开启邮箱注册登录服务");
        }
    }

    public void sendBindPhone(String phone) {
        this.checkOpenPhone();
        // 判断是否已发送
        User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getPhone, phone));
        // 正在使用中的手机号
        if (ObjectUtil.isNotNull(user)) {
            throw new BusinessException(ResultEnum.HAS_SIGN_IN, "手机号");
        }
        // 验证码未过期，重复发送
        if (StrUtil.isNotBlank(bindCache.get(phone, false))) {
            throw new BusinessException(ResultEnum.REPEAT_SEND_MAIL);
        }
        // 发送绑定手机短信验证码
        String code = RandomUtil.randomNumbers(6);
        smsService.sendBind(phone, code);
        // 保存验证码
        bindCache.put(phone, code);
    }


    public String loginWechatPublic(String xml) {
        JSONObject body = XmlUtil.xmlToBean(XmlUtil.parseXml(xml).getDocumentElement(), JSONObject.class);
        String encrypt = body.getStr("Encrypt");
        AdminSettingAuthLoginInfoVo config = configService.getConfigObject(ConfigKeyEnum.SETTING_LOGIN).toBean(AdminSettingAuthLoginInfoVo.class);
        String xmlContent = WeChatTool.decrypt(encrypt, config.getWxEncodingAESKey());
        body = XmlUtil.xmlToBean(XmlUtil.parseXml(xmlContent).getDocumentElement(), JSONObject.class);
        String openId = body.getStr("FromUserName");
        String msgType = body.getStr("MsgType");
        String code = body.getStr("Content");
        // 文本消息
        if (!"text".equals(msgType)) {
            throw new BusinessException(ResultEnum.NOT_SUPPORT_TYPE);
        }
        // 查询账号是否存在
        if (this.count(Wrappers.<User>lambdaQuery().eq(User::getOpenId, openId)) > 0) {

        }
        // 验证码
        log.info(">>> 微信公众号登录用户:{}={}", code, openId);
        wxUserLoginCache.put(code, openId);
        return "success";
    }

    /**
     * 微信公众号绑定服务器 TOKEN验证
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    public String checkWechatPublicToken(String signature, String timestamp, String nonce, String echostr) {
        log.info(">>> signature:{}", signature);
        log.info(">>> timestamp:{}", timestamp);
        log.info(">>> nonce:{}", nonce);
        log.info(">>> echostr:{}", echostr);
        AdminSettingAuthLoginInfoVo config = configService.getConfigObject(ConfigKeyEnum.SETTING_LOGIN).toBean(AdminSettingAuthLoginInfoVo.class);
        String token = config.getWxPublicToken();
        log.info(">>> token:{}", token);
        String[] arr = new String[]{token, timestamp, nonce};
        // 将token、timestamp、nonce三个参数进行字典序排序
        Arrays.sort(arr);
        // 将三个参数字符串拼接成一个字符串进行sha1加密
        String tmpStr = SecureUtil.sha1(arr[0] + arr[1] + arr[2]);
        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        if (tmpStr.equals(signature.toUpperCase())) {
            throw new BusinessException(ResultEnum.FAIL, "微信签名被篡改，验证不通过");
        }
        return echostr;
    }

    public UserPreLoginResVo preLoginWechatPublic() {
        this.checkOpenWxPublic();
        UserPreLoginResVo result = new UserPreLoginResVo();
        AdminSettingAuthLoginInfoVo config = configService.getConfigObject(ConfigKeyEnum.SETTING_LOGIN).toBean(AdminSettingAuthLoginInfoVo.class);
        result.setUrl(config.getWxQrcode());
        result.setVerifyCode(RandomUtil.randomNumbers(6));
        result.setCode(IdUtil.fastSimpleUUID());
        // 换成验证码
        wxCodeCache.put(result.getCode(), result.getVerifyCode());
        return result;
    }


    public UserLoginResVo wechatLoginToken(String code) {
        UserLoginResVo result = new UserLoginResVo();
        // 检测 code 是否存在
        String verifyCode = wxCodeCache.get(code, false);
        log.info(">>> 微信验证码:{}", verifyCode);
        if (StrUtil.isBlank(verifyCode)) {
            return result;
        }
        // 获取token
        String openId = wxUserLoginCache.get(verifyCode, false);
        log.info(">>> 微信登录用户:{}", openId);
        if (StrUtil.isBlank(openId)) {
            return result;
        }
        User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getOpenId, openId));
        if (ObjectUtil.isNull(user)){
            // 注册账号
            user = this.createUser(openId, null, null);
        }
        return this.createToken(user);
    }

    public void sendBindEmail(String email) {
        this.checkOpenEmail();
        // 判断邮箱是否已绑定
        User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getEmail, email));
        if (ObjectUtil.isNotNull(user)) {
            throw new BusinessException(ResultEnum.HAS_SIGN_IN, "邮箱");
        }
        // 验证码未过期，重复发送
        if (StrUtil.isNotBlank(bindCache.get(email, false))) {
            throw new BusinessException(ResultEnum.REPEAT_SEND_MAIL);
        }
        // 绑定账号-验证码发送
        String code = RandomUtil.randomNumbers(6);
        mailService.sendBindCode(email, code);
        // 保存验证码
        bindCache.put(email, code);
    }

    public void sendBackPhone(String phone) {
        this.checkOpenPhone();
        User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getPhone, phone));
        // 手机号未注册
        if (ObjectUtil.isNull(user)) {
            throw new BusinessException(ResultEnum.NOT_SIGN, "手机号");
        }
        // 账号已被冻结
        if (UserStateEnum.FREEZE.getCode().equals(user.getState())) {
            throw new BusinessException(ResultEnum.EMAIL_FREEZE);
        }
        // 验证码未过期，重复发送
        if (StrUtil.isNotBlank(codeCache.get(user.getUserId(), false))) {
            throw new BusinessException(ResultEnum.REPEAT_SEND_MAIL);
        }
        // 发送绑定手机短信验证码
        String code = RandomUtil.randomNumbers(6);
        smsService.sendBind(phone, code);
        // 保存验证码
        codeCache.put(user.getUserId(), code);
    }

    public void sendBackEmail(String email) {
        this.checkOpenEmail();
        // 判断邮箱是否已绑定
        User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getEmail, email));
        // 邮箱未注册
        if (ObjectUtil.isNull(user)) {
            throw new BusinessException(ResultEnum.NOT_SIGN, "邮箱");
        }
        // 账号已被冻结
        if (UserStateEnum.FREEZE.getCode().equals(user.getState())) {
            throw new BusinessException(ResultEnum.EMAIL_FREEZE);
        }
        // 验证码未过期，重复发送
        if (StrUtil.isNotBlank(codeCache.get(user.getUserId(), false))) {
            throw new BusinessException(ResultEnum.REPEAT_SEND_MAIL);
        }
        // 发送找回密码邮件验证码
        String code = RandomUtil.randomNumbers(6);
        mailService.sendGetBackCode(email, code);
        // 保存验证码
        codeCache.put(user.getUserId(), code);
    }

    public void forgetPwd(UserForgetPwdReqVo reqVo) {
        if (StrUtil.isAllBlank(reqVo.getPhone(), reqVo.getEmail())) {
            throw new BusinessException(ResultEnum.ERROR, "请填写邮箱或手机号");
        }
        // 查询账号信息
        User user = this.getOne(Wrappers.<User>lambdaQuery()
                .eq(StrUtil.isNotBlank(reqVo.getPhone()), User::getPhone, reqVo.getPhone())
                .eq(StrUtil.isNotBlank(reqVo.getEmail()), User::getEmail, reqVo.getEmail())
        );
        if (ObjectUtil.isNull(user)) {
            throw new BusinessException(ResultEnum.ERROR, "账号不存在");
        }
        if (UserStateEnum.SIGN_IN.getCode().equals(user.getState())) {
            throw new BusinessException(ResultEnum.ERROR, "账号正在注册中");
        }
        if (UserStateEnum.FREEZE.getCode().equals(user.getState())) {
            throw new BusinessException(ResultEnum.EMAIL_FREEZE);
        }
        // 检查图形验证码
        if (!captchaCache.containsKey(reqVo.getCaptchaCode())
                || !captchaCache.get(reqVo.getCaptchaCode(), false).verify(reqVo.getCaptcha())) {
            throw new BusinessException(ResultEnum.ERROR, "图形验证码错误");
        }
        // 检查验证码是否正确
        String code = codeCache.get(user.getUserId(), false);
        if (StrUtil.isBlank(code)) {
            throw new BusinessException(ResultEnum.ERROR, "验证码已过期");
        }
        if (!code.equals(reqVo.getCode())) {
            throw new BusinessException(ResultEnum.ERROR, "验证码错误");
        }
        // 更新密码
        user.setPassword(reqVo.getPassword());
        this.updateById(user);
    }

    public Boolean bindWechat(String code) {
        // 检测 code 是否存在
        String verifyCode = wxCodeCache.get(code, false);
        log.info(">>> 微信验证码:{}", verifyCode);
        if (StrUtil.isBlank(verifyCode)) {
            return false;
        }
        // 获取openId
        String openId = wxUserLoginCache.get(verifyCode, false);
        if (StrUtil.isBlank(openId)) {
            return false;
        }
        log.info(">>> 微信绑定用户:{}", openId);
        if (this.count(Wrappers.<User>lambdaQuery().eq(User::getOpenId, openId)) > 0) {
            throw new BusinessException(ResultEnum.ERROR, "微信号已被绑定");
        }
        this.update(Wrappers.<User>lambdaUpdate()
                .eq(User::getUserId, RequestDataHandler.getUserId())
                .set(User::getOpenId, openId)
        );
        return true;
    }
}
