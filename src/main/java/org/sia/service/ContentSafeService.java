package org.sia.service;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.sia.enums.ConfigKeyEnum;
import org.sia.enums.ResultEnum;
import org.sia.exception.BusinessException;
import org.sia.vo.AdminSafeThirdInfoVo;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/10/13 17:52
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ContentSafeService {

    private final ConfigService configService;
    /**
     * token 缓存
     */
    private static final TimedCache<String, String> tokenCache = CacheUtil.newTimedCache(300000);
    private static final String BD_TOKEN_KEY = "baidu";
    private final static String BAIDU_TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token?client_id={}&client_secret={}&grant_type=client_credentials";
    private final static String BAIDU_TEXT_URL = "https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined?access_token={}";

    public boolean hasSafeText(String text){
        AdminSafeThirdInfoVo config = configService.getConfigObject(ConfigKeyEnum.SAFE_THIRD).toBean(AdminSafeThirdInfoVo.class);
        if (!config.getOpen()){
            log.info(">>> open");
            return true;
        }
        if (StrUtil.isBlank(text)){
            log.info(">>> isBlank");
            return true;
        }
        String body = HttpUtil.createPost(StrUtil.format(BAIDU_TEXT_URL,this.getBaiduToken()))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(StrUtil.format("text={}",text))
                .execute()
                .body();
        JSONObject result = JSONUtil.parseObj(body);
        log.info(">>> 内容审核:{}",body);
        return result.getInt("conclusionType",1) != 2;
    }


    public String getBaiduToken() {
        if (tokenCache.containsKey(BD_TOKEN_KEY)) {
            return tokenCache.get(BD_TOKEN_KEY, false);
        }
        AdminSafeThirdInfoVo config = configService.getConfigObject(ConfigKeyEnum.SAFE_THIRD).toBean(AdminSafeThirdInfoVo.class);
        String response = HttpUtil.createPost(StrUtil.format(BAIDU_TOKEN_URL, config.getApiKey(), config.getSecretKey()))
                .execute().body();
        JSONObject result = JSONUtil.parseObj(response);
        if (result.containsKey("error")) {
            log.error(">>> 百度内容安全审核:{}", response);
            throw new BusinessException(ResultEnum.ERROR, "百度内容安全审核-获取TOKEN失败");
        }
        String accessToken = result.getStr("access_token");
        Integer expiresIn = result.getInt("expires_in");
        tokenCache.put(BD_TOKEN_KEY, accessToken, (expiresIn - 100) * 1000L);
        return accessToken;

    }
}
