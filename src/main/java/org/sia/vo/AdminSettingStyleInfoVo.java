package org.sia.vo;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/12 23:54
 */
@Data
public class AdminSettingStyleInfoVo {
    private String fc;
    private String sc;
    private String scb;
    private String fch;
    private String fcp;
    private String fcs;
    private Integer globalBoxRadius = 6;
    private String globalCustomCss;
    private String headerPvCode;
    private String footerPvCode;
    private String antdStyles;
    private String themeMode = "light";
}
