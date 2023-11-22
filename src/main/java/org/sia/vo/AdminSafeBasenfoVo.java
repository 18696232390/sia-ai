package org.sia.vo;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/13 20:51
 */
@Data
public class AdminSafeBasenfoVo {
    private Boolean stopInnerTxtCheck = false;
    private Boolean stopInnerMjCheck = false;
    private Boolean stopCustomCheck = false;
    private Boolean cutSpecialSymbol = false;
    private String regex;
    private String defaultRegex = "[\\|\\s%$&*@]+";
}
