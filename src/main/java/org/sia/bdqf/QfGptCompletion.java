package org.sia.bdqf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/19 09:31
 */
@Data
@Builder
@Slf4j
@AllArgsConstructor
@NoArgsConstructor(force = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QfGptCompletion implements Serializable {

    @NonNull
    private String model;

    @NonNull
    private List<Message> messages;
    /**
     * 使用什么取样温度，0到2之间。越高越奔放。越低越保守。
     * <p>
     * 不要同时改这个和topP
     */
    @Builder.Default
    private double temperature = 0.9;

    /**
     * 0-1
     * 建议0.9
     * 不要同时改这个和temperature
     */
    @JsonProperty("top_p")
    @Builder.Default
    private double topP = 0.9;

    /**
     * 是否流式输出.
     * default:false
     */
    @Builder.Default
    private boolean stream = false;

    /**
     * 	通过对已生成的token增加惩罚，减少重复生成的现象。说明：
     * （1）值越大表示惩罚越大
     * （2）默认1.0，取值范围：[1.0, 2.0]
     */
    @JsonProperty("penalty_score")
    private double penaltyScore = 1.0;

    /**
     * 用户唯一值，确保接口不被重复调用
     */
    @JsonProperty("user_id")
    private String userId;


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
}
