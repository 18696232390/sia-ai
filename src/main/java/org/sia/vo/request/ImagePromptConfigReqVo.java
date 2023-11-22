package org.sia.vo.request;

import cn.hutool.json.JSONObject;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/20 12:10
 */
@Data
public class ImagePromptConfigReqVo {
    private Long id;
    private String userId;
    private String drawType;
    private String sceneName;
    private String sceneType;
    private String url;
    @NotNull(message = "请填写自定义配置")
    private JSONObject config;
    private LocalDateTime updateTime;
}
