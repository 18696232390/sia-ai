package org.sia.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/18 18:16
 */
@Data
@Builder
public class ImageTaskStateDto {
    private String isPublic;
    private Integer cost;
    private String userId;
    private Integer index;
    private String taskId;
    private Map<Long,Long> pkgCostMap;
}
