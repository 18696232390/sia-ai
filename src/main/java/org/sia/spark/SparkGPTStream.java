package org.sia.spark;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/17 21:33
 */
@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SparkGPTStream {

    // 地址与鉴权信息  https://spark-api.xf-yun.com/v1.1/chat   1.5地址  domain参数为general
    // 地址与鉴权信息  https://spark-api.xf-yun.com/v2.1/chat   2.0地址  domain参数为generalv2
    private final String hostUrl = "https://spark-api.xf-yun.com/{}/chat";
    private String appid = "";
    private String apiSecret = "";
    private String apiKey = "";
    private OkHttpClient okHttpClient;



    /**
     * 连接超时
     */
    @Builder.Default
    private long timeout = 90;

    /**
     * 初始化
     */
    public SparkGPTStream init() {
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
    public void streamChatCompletion(SparkChatRequestParam chatParam,
                                     SparkGptStreamListener webSocketListener) {
        try {
            chatParam.setAppId(appid);
            // 构建鉴权url
            String authUrl = getAuthUrl(StrUtil.format(hostUrl, "general".equals(chatParam.getDomain()) ? "v1.1" : "v2.1"), apiKey, apiSecret);
            log.info(">>> 鉴权URL:{}", authUrl);
            String url = authUrl.replace("http://", "ws://").replace("https://", "wss://");
            Request request = new Request.Builder().url(url)
                    .build();
            webSocketListener.setRequestJson(chatParam.getRequestParam().toString());
            log.info(">>> 请求参数:{}", chatParam.getRequestParam().toStringPretty());
            okHttpClient.newWebSocket(request, webSocketListener);
        } catch (Exception e) {
            log.error("请求出错：{}", e);
        }
    }

    /**
     * 流式输出
     */
    public void streamChatCompletion(SparkChatRequestParam chatParam,
                                     SparkGptListener webSocketListener) {
        try {
            chatParam.setAppId(appid);
            // 构建鉴权url
            String authUrl = getAuthUrl(StrUtil.format(hostUrl, "general".equals(chatParam.getDomain()) ? "v1.1" : "v2.1"), apiKey, apiSecret);
            log.info(">>> 鉴权URL:{}", authUrl);
            String url = authUrl.replace("http://", "ws://").replace("https://", "wss://");
            Request request = new Request.Builder().url(url)
                    .build();
            webSocketListener.setRequestJson(chatParam.getRequestParam().toString());
            log.info(">>> 请求参数:{}", chatParam.getRequestParam().toStringPretty());
            okHttpClient.newWebSocket(request, webSocketListener);
        } catch (Exception e) {
            log.error("请求出错：{}", e);
        }
    }


    /**
     * 鉴权URL
     *
     * @param hostUrl
     * @param apiKey
     * @param apiSecret
     * @return
     * @throws Exception
     */
    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        // 时间
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        // 拼接
        String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                "GET " + url.getPath() + " HTTP/1.1";
        // SHA256加密
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);

        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        // Base64加密
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        // System.err.println(sha);
        // 拼接
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        // 拼接地址
        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder().//
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8))).//
                addQueryParameter("date", date).//
                addQueryParameter("host", url.getHost()).//
                build();

        return httpUrl.toString();
    }

}
