package org.sia.vo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/3 15:12
 */
@Data
public class AdminCategoryEditReqVo {
    @NotNull(message = "请填写分类 ID")
    private Long id;
    @NotBlank(message = "请填写分组")
    private String group;
    @NotBlank(message = "请填写分类名称")
    private String name;
    @NotBlank(message = "请填写分类KEY")
    private String key;
    @NotBlank(message = "请填写分类图标")
    private String icon;
    @NotNull(message = "请填写排序")
    private Integer sort;
}
