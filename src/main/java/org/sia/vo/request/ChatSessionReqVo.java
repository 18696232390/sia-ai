package org.sia.vo.request;

import javax.validation.constraints.*;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/10 21:44
 */
@Data
public class ChatSessionReqVo {
    @NotNull(message = "请填写对话ID")
    private Long id;
    @NotBlank(message = "请填写对话名称")
    @Size(max = 15,message = "对话名称不超过15个字")
    private String title;
    @NotBlank(message = "请选择模型")
    private String model;
    @NotNull(message = "请填写单次最大Tokens")
    private Integer maxToken;
    private String role;
    @NotNull(message = "请填写随机性")
    @Min(value = 0,message = "随机性范围:0～1")
    @Max(value = 1,message = "随机性范围:0～1")
    private Float random;
    @NotNull(message = "请填写话题新鲜度")
    @Min(value = -2,message = "话题新鲜度范围:-2～2")
    @Max(value = 2,message = "话题新鲜度范围:-2～2")
    private Float fresh;
    @NotNull(message = "请填写重复度")
    @Min(value = -2,message = "重复度范围:-2～2")
    @Max(value = 2,message = "重复度范围:-2～2")
    private Float repeat;
    @NotNull(message = "请填写上下文携带条目数")
    @Min(value = 0,message = "上下文携带条目数范围:0～15")
    @Max(value = 15,message = "上下文携带条目数范围:0～15")
    private Integer context;
}
