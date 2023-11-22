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
@TableName("t_image_prompt")
@Data
public class ImagePrompt {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String userId;
    private String drawType;
    private String sceneName;
    private String sceneType;
    private String url;
    private String config;
    private LocalDateTime updateTime;
}
