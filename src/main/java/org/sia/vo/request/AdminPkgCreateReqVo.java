package org.sia.vo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/4 16:35
 */
@Data
public class AdminPkgCreateReqVo {
    @NotBlank(message = "请填写套餐名称")
    private String name;
    @NotBlank(message = "请填写套餐类型")
    private String category;
    @NotNull(message = "请填写套餐价格")
    private Integer price;
    @NotNull(message = "请填写套餐折扣")
    private Float discount;
    @NotBlank(message = "请填写套餐描述")
    private String description;
    @NotNull(message = "请填写套餐有效期")
    private Long expireDay;
    @NotNull(message = "请填写套餐积分")
    private Long integral;
    @NotNull(message = "请选择是否每日重置")
    private Integer eachDay;
    @NotNull(message = "请填写排序")
    private Integer sort;
    @NotNull(message = "请选择是否公开")
    private Integer isPublic;
}
