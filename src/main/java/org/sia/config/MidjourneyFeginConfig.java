package org.sia.config;

import cn.hutool.core.exceptions.ExceptionUtil;
import feign.Logger;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.sia.enums.ResultEnum;
import org.sia.exception.BusinessException;
import org.springframework.context.annotation.Bean;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/27 16:17
 */
@Slf4j
public class MidjourneyFeginConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new  ErrorDecoder.Default(){
            @Override
            public Exception decode(String methodKey, Response response) {
                Exception exception = super.decode(methodKey, response);
                // 如果是RetryableException，则返回继续重试
                if (exception instanceof RetryableException) {
                    return exception;
                }
                log.error(">>> Midjourney服务异常",exception);
                return new BusinessException(ResultEnum.MJ_ERROR,"Midjourney服务异常");
            }
        };
    }

    @Bean
    public Logger.Level feignLevel() {
        return Logger.Level.FULL;
    }
}
