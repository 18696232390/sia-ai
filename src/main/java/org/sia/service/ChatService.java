package org.sia.service;

import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.sia.enums.ResultEnum;
import org.sia.exception.BusinessException;
import org.sia.vo.request.AiProxyChatReqVo;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/10/23 15:35
 */
@Service
@Slf4j
public class ChatService {
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.DAYS)
            .readTimeout(1, TimeUnit.DAYS)//这边需要将超时显示设置长一点，不然刚连上就断开，之前以为调用方式错误被坑了半天
            .build();

    private static final EventSource.Factory factory = EventSources.createFactory(okHttpClient);

    public SseEmitter chat(Request request) {
        SseEmitter sseEmitter = new SseEmitter(-1L);
        // 实例化EventSource，注册EventSource监听器
        factory.newEventSource(request, new EventSourceListener() {
            @Override
            public void onOpen(EventSource eventSource, Response response) {
                log.info(">>> 开启对话");
            }

            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data) {
                log.info(">>> AI:{}", data);
                try {
                    sseEmitter.send(data);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onClosed(EventSource eventSource) {
                log.info(">>> 关闭对话");
                sseEmitter.complete();
            }

            @Override
            public void onFailure(EventSource eventSource, Throwable t, Response response) {
                log.error(">>> 对话异常", t);
                sseEmitter.completeWithError(new BusinessException(ResultEnum.ERROR, "套餐余额不足"));
            }
        });
        return sseEmitter;
    }


    public static void main(String[] args) {
        AiProxyChatReqVo reqVo = new AiProxyChatReqVo();
        reqVo.setLibraryId(11242L);
        reqVo.setModel("gpt-3.5-turbo");
        reqVo.setStream(true);
        reqVo.setQuery("大A现在形势怎么样?");
        // 定义see接口
        Request request = new Request.Builder()
                .url("https://api.aiproxy.io/api/library/ask")
                .header("Authorization", "Bearer sk-AQMjiVvI3mhQvRd9w0rVX7bDTFHgFhXPEIwfCAlHpht2dFpk")
                .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()),
                        JSONUtil.toJsonStr(reqVo)))
                .build();

        // 实例化EventSource，注册EventSource监听器
        factory.newEventSource(request, new EventSourceListener() {
            @Override
            public void onOpen(EventSource eventSource, Response response) {
                log.info("onOpen");
            }

            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data) {
                log.info("onEvent");
                System.out.println(data);//请求到的数据
            }

            @Override
            public void onClosed(EventSource eventSource) {
                log.info("onClosed");
            }

            @Override
            public void onFailure(EventSource eventSource, Throwable t, Response response) {
                log.error("onFailure", t);
            }
        });

    }

}
