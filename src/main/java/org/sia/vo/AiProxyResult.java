package org.sia.vo;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/10/20 11:55
 */
@Data
public class AiProxyResult {
    private Boolean success;
    private String message;
    private Object data;
    private Integer errorCode;



}
