package org.sia.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/10/23 21:15
 */
@Slf4j
@Service
public class BaiduService {
    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    @Value("${baidu.apiKey}")
    private String apiKey;

    @Value("${baidu.secretKey}")
    private String secretKey;
    public  String getAccessToken(){
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(StrUtil.format("https://aip.baidubce.com/oauth/2.0/token?client_id={}&client_secret={}&grant_type=client_credentials",apiKey,secretKey))
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        try {
            Response response = HTTP_CLIENT.newCall(request).execute();
            return JSONUtil.parseObj(response.body().string()).getStr("access_token");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String repair(Map<String,Object> bodyMap){
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, JSONUtil.toJsonStr(bodyMap));
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rest/2.0/image-process/v1/inpainting?access_token=" + getAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        try {
            Response response = HTTP_CLIENT.newCall(request).execute();
            log.info(">>> result:{}",response.body().string());
            return JSONUtil.parseObj(response.body().string()).getStr("image");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
