package org.sia.vo.response;

import lombok.Data;
import org.sia.model.AiTool;

import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/10/21 20:30
 */
@Data
public class ToolResVo {
    private String category;
    private List<AiTool> appList;
}
