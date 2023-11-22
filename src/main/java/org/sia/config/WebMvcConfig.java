package org.sia.config;

import lombok.RequiredArgsConstructor;
import org.sia.interceptor.SystemHandlerInterceptor;
import org.sia.interceptor.TokenHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final TokenHandlerInterceptor tokenHandlerInterceptor;
    private final SystemHandlerInterceptor systemHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenHandlerInterceptor)
                .addPathPatterns(
                        "/v1/user/**",
                        "/v1/chat/**",
                        "/v1/image/**",
                        "/v1/invite/**",
                        "/v1/notify/**",
                        "/v1/package/**",
                        "/v1/pay/**",
                        "/v1/admin/**",
                        "/v1/exchangeCode/**",
                        "/v1/tool/**"
                )
                // 不需要校验token
                .excludePathPatterns(
                        "/v1/user/email/send",
                        "/v1/user/phone/send",
                        "/v1/user/captcha",
                        "/v1/user/email/signIn",
                        "/v1/user/login/password",
                        "/v1/user/login/verifyCode",
                        "/v1/user/login/wechatPublic",
                        "/v1/user/login/wechat/token",
                        "/v1/user/preLogin/wechatPublic",
                        "/v1/pay/alipay/notify",
                        "/v1/pay/wechat/notify",
                        "/v1/image/gallery/searchList",
                        "/v1/image/proxy",
                        "/v1/chat/model/list",
                        "/v1/image/model/list",
                        "/v1/image/notify"
                );

        registry.addInterceptor(systemHandlerInterceptor)
                .addPathPatterns(
                        "/v1/system/**"
                );
    }

}
