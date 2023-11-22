package org.sia.vo.response;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/14 20:03
 */
@Data
public class AdminImageAccountSearchResVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String name;
    private String platform;
    private Integer isValid;
    private String config;
    private Integer state;
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime createTime;
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime updateTime;

    private Integer waitQueueSize = 0;
    private Integer processQueueSize = 0;

    public JSONObject getConfig() {
        if (StrUtil.isNotBlank(config)) {
            return JSONUtil.parseObj(config);
        }
        return JSONUtil.createObj();
    }
}
