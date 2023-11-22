package org.sia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.sia.model.AiTool;
import org.sia.model.AppCategory;

import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/8 21:08
 */
public interface ToolMapper extends BaseMapper<AiTool> {

    List<AiTool> appList(@Param("userId") String userId);

    void addUser(@Param("userId") String userId, @Param("toolId") Long toolId);
    Long hasTool(@Param("userId") String userId, @Param("toolId") Long toolId);

    void deleteUser(@Param("userId") String userId, @Param("toolId") Long toolId);
}
