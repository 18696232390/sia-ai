package org.sia.vo.response;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/11/8 18:37
 */
@Data
public class ChatGPTAssistantCreateResVo {
    private String id;
    private String object;
    private String name;
    private String description;
}
