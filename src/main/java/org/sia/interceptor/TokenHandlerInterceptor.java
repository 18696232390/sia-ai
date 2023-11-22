package org.sia.interceptor;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sia.enums.ConfigKeyEnum;
import org.sia.enums.ResultEnum;
import org.sia.exception.BusinessException;
import org.sia.handler.RequestDataHandler;
import org.sia.model.User;
import org.sia.service.ConfigService;
import org.sia.service.UserService;
import org.sia.vo.AdminDomainAuthInfoVo;
import org.sia.vo.AdminSettingAuthLoginInfoVo;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2022/7/11 16:51
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenHandlerInterceptor implements HandlerInterceptor {
    private final static String TOKEN_TAG = "x-token";
    private final static String AUTH_TAG = "x-dm";
    private final UserService userService;
    private final ConfigService configService;
    public static final TimedCache<ConfigKeyEnum, AdminDomainAuthInfoVo> dmCache = CacheUtil.newTimedCache(300000);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 服务开关
        String dm = request.getHeader(AUTH_TAG);
        AdminDomainAuthInfoVo authConfig = dmCache.get(ConfigKeyEnum.DOMAIN_AUTH, false);
        log.info(">>> x-dm={}",authConfig.getDomainList());
        if (!request.getRequestURI().startsWith("/v1/config") && ((ObjectUtil.isNull(authConfig) || !authConfig.getDomainList().contains(dm)))) {
            throw new BusinessException(ResultEnum.NOT_AUTH_DOMAIN);
        }
        // 配置检查
        AdminSettingAuthLoginInfoVo config = configService.getConfigObject(ConfigKeyEnum.SETTING_LOGIN).toBean(AdminSettingAuthLoginInfoVo.class);
        if (ObjectUtil.isNull(config)) {
            throw new BusinessException(ResultEnum.ERROR, "请配置[站点设置]->[全局配置]->登录授权");
        }
        // JWT校验
        String token = request.getHeader(TOKEN_TAG);
        if (StrUtil.isBlank(token)) {
            throw new BusinessException(config.getHasAuth() ? ResultEnum.NO_PERMISSION_YES : ResultEnum.NO_PERMISSION_NO);
        }
        if (!userService.verifyToken(token)) {
            throw new BusinessException(config.getHasAuth() ? ResultEnum.NO_PERMISSION_YES : ResultEnum.NO_PERMISSION_NO);
        }
        // JWT解析
        Map<String, String> payload = userService.parseToken(token);
        // 管理员权限检查
        if (request.getRequestURI().startsWith("/v1/admin") && !"1".equals(payload.get("isAdmin"))) {
            throw new BusinessException(ResultEnum.NO_ADMIN);
        }
        // 手机号绑定检查
        if (!request.getRequestURI().startsWith("/v1/user/bind")
                && (config.getHasBindPhone() && userService.count(Wrappers.<User>lambdaQuery().eq(User::getUserId, payload.get("userId")).isNotNull(User::getPhone)) == 0)) {
            throw new BusinessException(ResultEnum.NO_BIND_PHONE);
        }
        // 邮箱绑定检查
        if (!request.getRequestURI().startsWith("/v1/user/bind")
                && (config.getHasBindEmail() && userService.count(Wrappers.<User>lambdaQuery().eq(User::getUserId, payload.get("userId")).isNotNull(User::getEmail)) == 0)) {
            throw new BusinessException(ResultEnum.NO_BIND_EMAIL);
        }
        // 设置客户端IP
        payload.put("ip", request.getRemoteAddr());
        RequestDataHandler.setPayload(payload);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestDataHandler.remove();
    }
}
