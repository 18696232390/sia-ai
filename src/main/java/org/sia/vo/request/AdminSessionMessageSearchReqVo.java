package org.sia.vo.request;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/1 23:24
 */
@Data
public class AdminSessionMessageSearchReqVo {
    private Long id;
    private String userId;
    private String question;
    private String answer;
}
