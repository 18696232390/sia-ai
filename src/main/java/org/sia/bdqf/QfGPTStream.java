package org.sia.bdqf;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import java.util.concurrent.TimeUnit;
@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QfGPTStream {
    private String apiKey;
    private String apiSecret;
    private OkHttpClient okHttpClient;
    private String token;
    private DateTime createTime;

    /**
     * 反向代理
     */
    private final String apiHost = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/";

    private final String authUrl = "https://aip.baidubce.com/oauth/2.0/token";

    public String getAccessToken() {
        if (StrUtil.isNotBlank(token) && DateUtil.date().isBefore(createTime)) {
            return token;
        }
        String result = HttpUtil.get(StrUtil.format("https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id={}&client_secret={}", apiKey,apiSecret));
        log.info(">>>> 千帆Token信息:{}", result);
        JSONObject resultJson = JSONUtil.parseObj(result);
        token = resultJson.getStr("access_token");
        createTime = DateUtil.offsetSecond(DateUtil.date(), resultJson.getInt("expires_in") - 100);
        return token;
    }



    /**
     * 连接超时
     */
    @Builder.Default
    private long timeout = 90;


    /**
     * 初始化
     */
    public QfGPTStream init() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(timeout, TimeUnit.SECONDS);
        client.writeTimeout(timeout, TimeUnit.SECONDS);
        client.readTimeout(timeout, TimeUnit.SECONDS);
        okHttpClient = client.build();
        return this;
    }


    /**
     * 流式输出
     */
    public void streamChatCompletion(QfGptCompletion chatCompletion,
                                     EventSourceListener eventSourceListener) {
        chatCompletion.setStream(true);
        try {
            EventSource.Factory factory = EventSources.createFactory(okHttpClient);
            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(chatCompletion);
            Request request = new Request.Builder()
                    .url(apiHost + chatCompletion.getModel() + "?access_token=" + this.getAccessToken())
                    .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()),
                            requestBody))
                    .build();
            factory.newEventSource(request, eventSourceListener);
        } catch (Exception e) {
            log.error("请求出错：{}", e);
        }
    }

}
