package org.sia.bdqf;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.plexpt.chatgpt.util.SseHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.function.Consumer;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/19 20:12
 */
@Slf4j
@RequiredArgsConstructor
public class QfGptListener extends EventSourceListener {

    protected String lastMessage = "";

    @Setter
    @Getter
    protected Consumer<String> onComplate = s -> {

    };

    @Setter
    @Getter
    protected Consumer<Throwable> onError = s -> {

    };


    @Setter
    @Getter
    protected Consumer<EventSource> onOpen = s -> {

    };

    @Override
    public void onOpen(EventSource eventSource, Response response) {
        onOpen.accept(eventSource);
    }

    @Override
    public void onClosed(EventSource eventSource) {

    }

    @Override
    public void onEvent(EventSource eventSource, String id, String type, String data) {
        QfChatCompletionResponse response = JSON.parseObject(data, QfChatCompletionResponse.class);
        String text = response.getResult();
        if (text != null) {
            lastMessage += text;
        }
        // 读取Json
        if (response.getIs_end()) {
            onComplate.accept(lastMessage);
        }
    }

    @SneakyThrows
    @Override
    public void onFailure(EventSource eventSource, Throwable throwable, Response response) {
        onError.accept(throwable);
        eventSource.cancel();
    }


}
