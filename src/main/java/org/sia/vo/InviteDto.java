package org.sia.vo;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/14 21:52
 */
@Data
public class InviteDto {
    private String from;
    private String to;
    private Long integral;
    private Long expireDay;
    private Long id = 9527L;
    private String name = "邀请新用户";
    private String category = "邀请码";
}
