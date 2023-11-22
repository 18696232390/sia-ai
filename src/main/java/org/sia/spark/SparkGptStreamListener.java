package org.sia.spark;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.plexpt.chatgpt.util.SseHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.sse.EventSource;
import org.sia.enums.ResultEnum;
import org.sia.exception.BusinessException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class SparkGptStreamListener extends WebSocketListener {

    private final SseEmitter sseEmitter;
    private String requestJson;

    private String answer = "";

    public void setRequestJson(String requestJson) {
        this.requestJson = requestJson;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        webSocket.send(requestJson);
        log.info(">>>> WSS发送请求");
        try {
            onOpen.accept(webSocket);
        } catch (Exception ex) {
            this.onFailure(webSocket, ex, response);
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        SparkChatResponse result = JSONUtil.toBean(text, SparkChatResponse.class);
        if (result.getHeader().getCode() != 0) {
            this.onFailure(webSocket, new BusinessException(ResultEnum.FAIL, result.getHeader().getMessage()), null);
            return;
        }
        String message = result.getPayload().getChoices().getText().get(0).getContent();
        answer+=message;
        SseHelper.send(this.sseEmitter, StrUtil.replace(message, " ", "&#32;").replaceAll("\n", "&#92n"));
        if (result.getHeader().getStatus() == 2) {
            webSocket.close(1000, "done");
            onComplate.accept(answer);
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        webSocket.close(1000, t.getMessage());
        onError.accept(t);
        SseHelper.send(this.sseEmitter, t.getMessage() + "[DONE]");
        SseHelper.complete(this.sseEmitter);
    }

    @Setter
    @Getter
    protected Consumer<Throwable> onError = s -> {

    };


    @Setter
    @Getter
    protected Consumer<WebSocket> onOpen = s -> {

    };

    @Setter
    @Getter
    protected Consumer<String> onComplate = s -> {

    };
}