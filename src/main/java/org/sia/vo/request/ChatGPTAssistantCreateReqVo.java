package org.sia.vo.request;

import lombok.Data;
import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/11/8 18:26
 */
@Data
public class ChatGPTAssistantCreateReqVo {
    private String name;
    private String model;
    private String description;
    private String instructions;
    private List<Tool> tools;

    @Data
    public static class Tool{
        private String type;
    }
}
