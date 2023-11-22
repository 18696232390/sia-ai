package org.sia.vo.request;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/10/20 14:36
 */
@Data
public class AiProxyListDocumentReqVo {
    private String order;
    private String orderBy;
    private Integer page;
    private Integer pageSize;
    private Long libraryId;
    private String apiKey;
}
