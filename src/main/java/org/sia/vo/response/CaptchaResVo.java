package org.sia.vo.response;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/14 20:17
 */
@Data
public class CaptchaResVo {
    private String base64;
    private String code;
}
