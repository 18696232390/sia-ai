package org.sia.listener;

import cn.hutool.core.util.StrUtil;
import com.plexpt.chatgpt.listener.AbstractStreamListener;
import com.plexpt.chatgpt.util.SseHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.function.Consumer;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/19 14:28
 */
@Slf4j
@RequiredArgsConstructor
public class ChatGptStreamListener extends AbstractStreamListener {
    private final SseEmitter sseEmitter;

    @Setter
    @Getter
    protected Consumer<Throwable> onError = s -> {

    };


    @Setter
    @Getter
    protected Consumer<EventSource> onOpen = s -> {

    };

    @SneakyThrows
    @Override
    public void onMsg(String message) {
        SseHelper.send(this.sseEmitter, StrUtil.replace(message," ","&#32;").replaceAll("\n","&#92n"));
    }

    @Override
    public void onError(Throwable throwable, String response) {
        onError.accept(throwable);
        SseHelper.send(this.sseEmitter, throwable.getMessage() + "[DONE]");
        SseHelper.complete(this.sseEmitter);
    }

    @Override
    public void onOpen(EventSource eventSource, Response response) {
        super.onOpen(eventSource, response);
        try {
            onOpen.accept(eventSource);
        } catch (Exception ex) {
            this.onError(ex, ex.getMessage());
        }
    }
}
