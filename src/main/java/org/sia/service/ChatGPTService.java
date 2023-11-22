package org.sia.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.client.OpenAiApi;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;


/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/11/8 18:36
 */
@Slf4j
@Service
public class ChatGPTService implements InitializingBean {

    private final static String Key = "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";
    private final static String hostProxy = "127.0.0.1";
    private final static Integer portProxy = 7890;
    private OpenAiService service;

    public OpenAiService getService() {
        return service;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ObjectMapper mapper = OpenAiService.defaultObjectMapper();
        OkHttpClient client = OpenAiService.defaultClient(Key, Duration.ofSeconds(10L))
                .newBuilder()
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(hostProxy, portProxy)))
                .build();
        Retrofit retrofit = OpenAiService.defaultRetrofit(client, mapper);
        this.service = new OpenAiService(retrofit.create(OpenAiApi.class));
    }

    public static void main(String[] args) {
        OpenAiService service = new OpenAiService(Key, Duration.ofSeconds(30));
        // 计费
//        BillingUsage billingUsage = service.(LocalDate.parse("2023-11-08"), LocalDate.now());
//        BigDecimal totalUsage = billingUsage.getTotalUsage();
    }

//    public static void main(String[] args) {
//        String token = Key;
//        OpenAiService service = new OpenAiService(token, Duration.ofSeconds(30));
//
//        System.out.println("\nCreating completion...");
//        CompletionRequest completionRequest = CompletionRequest.builder()
//                .model("ada")
//                .prompt("Somebody once told me the world is gonna roll me")
//                .echo(true)
//                .user("testing")
//                .n(3)
//                .build();
//        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);
//
//        System.out.println("\nCreating Image...");
//        CreateImageRequest request = CreateImageRequest.builder()
//                .prompt("A cow breakdancing with a turtle")
//                .build();
//
//        System.out.println("\nImage is located at:");
//        System.out.println(service.createImage(request).getData().get(0).getUrl());
//
//        System.out.println("Streaming chat completion...");
//        final List<ChatMessage> messages = new ArrayList<>();
//        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a dog and will speak as such.");
//        messages.add(systemMessage);
//        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
//                .builder()
//                .model("gpt-3.5-turbo")
//                .messages(messages)
//                .n(1)
//                .maxTokens(50)
//                .logitBias(new HashMap<>())
//                .build();
//
//        service.streamChatCompletion(chatCompletionRequest)
//                .doOnError(Throwable::printStackTrace)
//                .blockingForEach(System.out::println);
//
//        service.shutdownExecutor();
//    }
}
