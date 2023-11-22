package org.sia.vo;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/13 21:33
 */
@Data
public class AdminSafeThirdInfoVo {
    private String domain = "https://aip.baidubce.com";
    private Boolean open = false;
    private String apiKey;
    private String secretKey;
}
