package org.sia.vo.request;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/4 14:17
 */
@Data
public class AdminPkgSearchReqVo {
    private Long id;
    private String category;
    private String name;
    private String description;
}
