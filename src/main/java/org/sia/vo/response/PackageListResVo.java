package org.sia.vo.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/9 11:30
 */
@Data
public class PackageListResVo {
    private Long id;
    // 套餐名称
    private String name;
    // 价格
    private Double price;
    // 折扣
    private Float discount;
    // 描述
    private String description;
    // 有效天数
    private Long expireDay;
    // 积分
    private Long integral;
    // 是否每日发放
    private Boolean eachDay;
}
