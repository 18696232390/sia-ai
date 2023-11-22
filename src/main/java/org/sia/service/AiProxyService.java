package org.sia.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.sia.enums.ResultEnum;
import org.sia.exception.BusinessException;
import org.sia.handler.RequestDataHandler;
import org.sia.model.User;
import org.sia.vo.AiProxyResult;
import org.sia.vo.request.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/10/20 09:49
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiProxyService {

    private final ChatService chatService;
    private final UserService userService;

    private static final String ADMIN_URL = "https://aiproxy.io{}";
    private static final String API_URL = "https://api.aiproxy.io{}";

    @Value("${aiproxy.apiKey}")
    private String apiKey;


    /**
     * 根据套餐创建想应额度的子账户
     *
     * @param reqVo
     */
    public String createApiKey(AiProxyCreateApiKeyReqVo reqVo) {
        String body = HttpUtil.createPost(StrUtil.format(ADMIN_URL, "/api/user/createApiKey"))
                .header("Api-Key", apiKey)
                .body(JSONUtil.toJsonStr(reqVo))
                .execute()
                .body();

        AiProxyResult result = JSONUtil.toBean(body, AiProxyResult.class);
        if (!result.getSuccess()) {
            throw new BusinessException(ResultEnum.FAIL, result.getMessage());
        }
        return String.valueOf(result.getData());
    }

    /**
     * 余额
     *
     * @return
     */
    public BigDecimal getBalance(String userId) {
        String body = HttpUtil.createGet(StrUtil.format(API_URL, StrUtil.format("/api/point/getPointAccount?externalId={}", userId)))
                .execute()
                .body();

        AiProxyResult result = JSONUtil.toBean(body, AiProxyResult.class);
        if (!result.getSuccess()) {
            throw new BusinessException(ResultEnum.ERROR, result.getMessage());
        }
        return JSONUtil.parseObj(result.getData()).getBigDecimal("points", BigDecimal.ZERO);
    }


    public SseEmitter chat(AiProxyChatReqVo reqVo) {
        User user = userService.getById(RequestDataHandler.getUserId());

        return chatService.chat(new Request.Builder()
                .url(StrUtil.format(API_URL, "/api/library/ask"))
                .header("Authorization", "Bearer " + user.getApiKey())
                .post(
                        RequestBody.create(MediaType.parse(ContentType.JSON.getValue()),
                                JSONUtil.toJsonStr(reqVo)))
                .build());
    }


    private void checkApiKey(String apiKey) {
        if (StrUtil.isBlank(apiKey)) {
            throw new BusinessException(ResultEnum.ERROR, "请购买知识库套餐");
        }
    }

}
