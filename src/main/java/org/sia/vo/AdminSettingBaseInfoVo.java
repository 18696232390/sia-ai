package org.sia.vo;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/11 19:53
 */
@Data
public class AdminSettingBaseInfoVo {
    private String name;
    private String description;
    private String domain;
    private String keyword;
    private String logo;
    private Boolean hasSSL;
    private String secret;
    private String email;
    private String frontPath;
    private String footer;
    private String scoreIcon;
    private String placeholderImage;
    private List<String> scrollList;
}
