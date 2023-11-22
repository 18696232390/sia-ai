package org.sia.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.sia.bdqf.QfGptCompletion;

import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/20 17:02
 */
@Data
public class ChatCompletionParam {
    // 模型
    private String model;
    // 消息列表(上下文支持)
    private List<Message> messageList;
    //

    @Data
    public static class Message {
        private String role;
        private String content;

        public static QfGptCompletion.Message ofUser(String content){
            return new QfGptCompletion.Message("user",content);
        }

        public static QfGptCompletion.Message ofAssistant(String content){
            return new QfGptCompletion.Message("assistant",content);
        }
    }
}
