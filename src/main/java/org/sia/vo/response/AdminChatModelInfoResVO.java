package org.sia.vo.response;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/7 18:18
 */
@Data
public class AdminChatModelInfoResVO {
    private Long id;
    private String name;
    private String key;
    private String alias;
    private Integer token;
    private Integer open;
    private Integer sort;
    private Integer integral;
    private Integer custom;
}
