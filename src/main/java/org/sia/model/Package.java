package org.sia.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/9 11:54
 */
@TableName("t_package")
@Data
public class Package {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("`name`")
    private String name;
    private String category;
    private Integer price;
    private Float discount;
    private String description;
    private Long expireDay;
    private Long integral;
    private Integer eachDay;
    private Integer isPublic;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer sort;
}
