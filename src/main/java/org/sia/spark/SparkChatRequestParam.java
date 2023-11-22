package org.sia.spark;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.*;

import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/17 22:33
 */
@Data
@Builder
public class SparkChatRequestParam {
    //应用appid，从开放平台控制台创建的应用中获取
    private String appId;
    //每个用户的id，用于区分不同用户
    private String uid;

    //指定访问的领域,general指向V1.5版本 generalv2指向V2版本。注意：不同的取值对应的url也不一样！
    private String domain = "general";
    //核采样阈值。用于决定结果随机性，取值越高随机性越强即相同的问题得到的不同答案的可能性越高
    private Float temperature = 0.5F;
    //模型回答的tokens的最大长度
    private Integer maxTokens = 2048;
    //从k个候选中随机选择⼀个（⾮等概率）
    private Integer top_k = 4;
    //用于关联用户会话
    private String chatId;

    private List<Message> messageList;


    @Data
    @AllArgsConstructor
    public static class Message {
        private String role;
        private String content;

        public static Message ofUser(String content){
            return new Message("user",content);
        }

        public static Message ofAssistant(String content){
            return new Message("assistant",content);
        }
    }


    public JSONObject getRequestParam() {
        JSONObject requestJson = JSONUtil.createObj();

        com.alibaba.fastjson.JSONObject header = new com.alibaba.fastjson.JSONObject();  // header参数
        header.put("app_id", appId);
        header.put("uid", uid);

        com.alibaba.fastjson.JSONObject parameter = new com.alibaba.fastjson.JSONObject(); // parameter参数
        com.alibaba.fastjson.JSONObject chat = new com.alibaba.fastjson.JSONObject();
        chat.put("domain", domain);
        chat.put("temperature", temperature);
        chat.put("max_tokens", maxTokens);
        chat.put("top_k", top_k);
        chat.put("chat_id", chatId);
        parameter.put("chat", chat);

        JSONObject payload = JSONUtil.createObj(); // payload参数
        payload.put("message", JSONUtil.createObj().set("text",messageList));

        requestJson.put("header", header);
        requestJson.put("parameter", parameter);
        requestJson.put("payload", payload);
        return requestJson;
    }


}
