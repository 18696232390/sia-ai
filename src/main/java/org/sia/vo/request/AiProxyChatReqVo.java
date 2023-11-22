package org.sia.vo.request;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/10/23 15:29
 */
@Data
public class AiProxyChatReqVo {
    private String model = "gpt-3.5-turbo";
    private String query;
    private Long libraryId;
    private Boolean stream = true;
}
