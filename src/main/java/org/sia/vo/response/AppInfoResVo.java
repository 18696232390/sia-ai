package org.sia.vo.response;

import lombok.Data;
import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/15 16:39
 */
@Data
public class AppInfoResVo {
    private Long id;
    private String name;
    private String key;
    private String icon;
    private Integer sort;
    private List<App> appList;

    @Data
    public static class App {
        private Long id;
        private String name;
        private String icon;
        private String model;
        private String description;
        private String placeholder;
        private Integer sort;
        private String submitTitle;
        private Long categoryId;
    }

}
