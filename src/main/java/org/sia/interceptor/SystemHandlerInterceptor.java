package org.sia.interceptor;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
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
public class SystemHandlerInterceptor implements HandlerInterceptor {
    private final static String TAG_KEY = "x-server";
    private final static String TAG_VALUE = "SIAAI@2024#";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tag = request.getHeader(TAG_KEY);
        if (StrUtil.isBlank(tag)||!tag.equals(TAG_VALUE)) {
            throw new BusinessException(ResultEnum.ERROR,"NOT AUTH");
        }
        return true;
    }

}
