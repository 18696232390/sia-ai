package org.sia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.sia.model.ChatModel;
import org.sia.vo.response.AdminChatModelInfoResVO;
import org.sia.vo.response.ChatModelShowResVo;

import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/8 21:08
 */
public interface ChatModelMapper extends BaseMapper<ChatModel> {

    List<AdminChatModelInfoResVO> infoList(@Param("custom") Integer custom,@Param("open") Integer open);

    List<ChatModelShowResVo> showList();
}
