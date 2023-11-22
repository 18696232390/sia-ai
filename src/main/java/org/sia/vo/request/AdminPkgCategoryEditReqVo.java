package org.sia.vo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/6 00:22
 */
@Data
public class AdminPkgCategoryEditReqVo {
    @NotBlank(message = "请填写key")
    private String key;
    @NotBlank(message = "请填写name")
    private String name;
}
