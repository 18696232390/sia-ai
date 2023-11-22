package org.sia.vo.request;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/10/20 14:36
 */
@Data
public class AiProxyCreateTextReqVo {
    private Long libraryId;
    private String title;
    private String text;
    private String url;
    private String apiKey;
}
