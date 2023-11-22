package org.sia.spark;

import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.sia.enums.ResultEnum;
import org.sia.exception.BusinessException;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class SparkGptListener extends WebSocketListener {
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
        if (result.getHeader().getStatus() == 2) {
            webSocket.close(1000, "done");
            onComplate.accept(answer);
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        webSocket.close(1000, t.getMessage());
        onError.accept(t);
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