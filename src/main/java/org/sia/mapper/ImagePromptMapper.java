package org.sia.mapper;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.sia.model.ImagePrompt;
import org.sia.vo.request.ImagePromptSearchReqVo;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/8 21:08
 */
public interface ImagePromptMapper extends BaseMapper<ImagePrompt> {

    Page<JSONObject> searchList(Page page, @Param("condition") ImagePromptSearchReqVo condition);
}
