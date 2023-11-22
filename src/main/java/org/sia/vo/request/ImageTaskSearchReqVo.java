package org.sia.vo.request;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/18 17:26
 */
@Data
public class ImageTaskSearchReqVo {
    private String keyword;
    private String userId;
    private Long taskId;
}
