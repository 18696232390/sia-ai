package org.sia.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/9 11:54
 */
@TableName("t_package_record")
@Data
public class PackageRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String userId;
    private String category;
    private String operation;
    private Integer cost;
    private LocalDateTime createTime;
}
