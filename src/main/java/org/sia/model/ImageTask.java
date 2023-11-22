package org.sia.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.sia.enums.TaskAction;
import org.sia.enums.TaskStatus;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/17 21:25
 */
@Data
@TableName("t_image_task")
public class ImageTask {
    @TableId(type = IdType.INPUT)
    private String id;
    private String userId;
    private TaskAction action;
    private String prompt;
    private String promptEn;
    private String remixPrompt;
    private String description;
    private String state;
    private Long submitTime;
    private Long startTime;
    private Long finishTime;
    private String imageUrl;
    private TaskStatus status = TaskStatus.NOT_START;
    private String progress;
    private String failReason;
    private Integer cost;
    private Integer back;
    private String isPublic;
    private String miniUrl;
}
