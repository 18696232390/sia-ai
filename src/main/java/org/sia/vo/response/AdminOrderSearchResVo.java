package org.sia.vo.response;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/5 15:13
 */
@Data
public class AdminOrderSearchResVo {
    private String id;
    private String platform;
    private String userId;
    private Long pkgId;
    private Integer price;
    private String info;
    private String state;
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime createTime;
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime updateTime;

    public JSONObject getInfo() {
        JSONObject infoJson = JSONUtil.createObj();
        if (StrUtil.isNotBlank(this.info)){
            infoJson = JSONUtil.parseObj(this.info);
        }
        return infoJson;
    }
}
