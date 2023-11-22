package org.sia.vo.request;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/5 19:35
 */
@Data
public class AdminUpEditReqVo {
    @NotNull(message = "请填写用户套餐ID")
    private Long id;
    private String userId;
    private Long pkgId;
    private String name;
    private String category;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer eachDay;
    private Long integral;
    private Long remain;

}
