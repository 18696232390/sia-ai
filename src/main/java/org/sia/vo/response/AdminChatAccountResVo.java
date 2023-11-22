package org.sia.vo.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/6 13:59
 */
@Data
public class AdminChatAccountResVo {
    private Long id;
    private String name;
    private String key;
    private String platform;
    private String model;
    private Integer state;
    private Integer isValid;
    private Long totalToken;
    private Long usedToken;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime checkTime;
}
