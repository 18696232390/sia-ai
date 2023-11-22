package org.sia.vo;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/13 09:40
 */
@Data
public class AdminImageCommonInfoVo {
    private Boolean stop;
    private Boolean filterParam;
    private Boolean autoTranslate;
    private String model;
    private String imagineVersion;
    private String blendVersion;
    private String describeVersion;
}
