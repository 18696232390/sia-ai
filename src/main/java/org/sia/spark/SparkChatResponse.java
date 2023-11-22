package org.sia.spark;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/17 22:33
 */
@Data
public class SparkChatResponse {
    private Header header;
    private Payload payload;

    @Data
    public static class Header{
        private Integer code;
        private String message;
        private String sid;
        private Integer status;
    }

    @Data
    public static class Payload{
        private Choices choices;
        private Usage usage;
    }

    @Data
    public static class Choices{
        private Integer status;
        private Integer seq;
        private List<Text> text;
    }

    @Data
    public static class Usage{
        private UsageText text;
    }
    @Data
    public static class UsageText{
        private Integer question_tokens;
        private Integer prompt_tokens;
        private Integer completion_tokens;
        private Integer total_tokens;
    }

    @Data
    public static class Text{
        private String content;
        private String role;
        private Integer index;
    }

}
