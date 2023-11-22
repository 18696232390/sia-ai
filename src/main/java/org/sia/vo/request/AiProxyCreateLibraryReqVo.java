package org.sia.vo.request;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/10/20 14:30
 */
@Data
public class AiProxyCreateLibraryReqVo {
    private String libraryName;
    private String description;
    private String apiKey;
}
