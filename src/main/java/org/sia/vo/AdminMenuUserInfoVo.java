package org.sia.vo;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/13 21:43
 */
@Data
public class AdminMenuUserInfoVo {
    private Boolean chatOpen = true;
    private Boolean imageOpen = true;
    private Boolean appOpen = true;
    private Boolean galleryOpen = true;
    private Boolean meOpen = true;
    private Boolean senceOpen = true;

    private List<Menu> customList;

    @Data
    public static class Menu{
        private String name;
        private String icon;
        private String action;
        private String value;
        private Boolean hasLogin = false;
        private Boolean open = false;
        private Integer sort = 0;
        private Boolean supportMobile = false;
    }
}
