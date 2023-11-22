package org.sia.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/9 11:54
 */
@TableName("t_tool")
@Data
public class AiTool {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String category;
    private String icon;
    private String title;
    private String remark;
    private Integer sort;
}
