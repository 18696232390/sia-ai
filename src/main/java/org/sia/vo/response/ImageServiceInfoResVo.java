package org.sia.vo.response;

import lombok.Data;

import java.util.Map;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/25 13:00
 */
@Data
public class ImageServiceInfoResVo {
    private boolean state;
    private Map<String,Long> data;
}
