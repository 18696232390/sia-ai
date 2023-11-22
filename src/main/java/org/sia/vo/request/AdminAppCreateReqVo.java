package org.sia.vo.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/2 16:44
 */
@Data
public class AdminAppCreateReqVo {
    @NotBlank(message = "请填写应用名称")
    private String name;
    @NotNull(message = "请选择是否公开")
    private Integer isPublic;
    private String icon;
    @NotBlank(message = "请填写应用描述")
    private String description;
    private String placeholder;
    private Integer sort;
    @NotBlank(message = "请填写提交按钮文案")
    private String submitTitle;
    @NotBlank(message = "请填写应用设定")
    private String prompt;
    @NotNull(message = "请选择分类")
    private Long categoryId;
    @NotNull(message = "请填写最大Tokens")
    private Long maxLength;
    @NotNull(message = "请填写模型")
    private String model;
    @NotNull(message = "请填写随机性")
    @Min(value = 0,message = "随机性范围:0～1")
    @Max(value = 1,message = "随机性范围:0～1")
    private Float temperature;
    @NotNull(message = "请填写话题新鲜度")
    @Min(value = -2,message = "话题新鲜度范围:-2～2")
    @Max(value = 2,message = "话题新鲜度范围:-2～2")
    private Float presencePenalty;
    @NotNull(message = "请填写重复度")
    @Min(value = -2,message = "重复度范围:-2～2")
    @Max(value = 2,message = "重复度范围:-2～2")
    private Float frequencyPenalty;
}
