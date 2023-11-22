
package org.sia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.sia.model.ChatMessage;
import org.sia.model.ChatSession;
import org.sia.vo.request.AdminSessionMessageSearchReqVo;
import org.sia.vo.request.ChatMessageLoadReqVo;
import org.sia.vo.request.ChatMessageReqVo;
import org.sia.vo.response.AdminSessionMessageResVo;
import org.sia.vo.response.ChatMessageResVo;
import org.sia.vo.response.ChatSessionListResVo;

import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/8 21:08
 */
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {
    Page<ChatMessageResVo> pageList(Page page, @Param("condition") ChatMessageReqVo condition);

    List<ChatMessage> getContextList(@Param("id") Long id, @Param("limit") Integer limit);

    Page<AdminSessionMessageResVo> searchList(Page page, @Param("condition") AdminSessionMessageSearchReqVo condition);

    List<ChatMessageResVo> load(@Param("condition") ChatMessageLoadReqVo reqVo);

    List<ChatMessageResVo> topList(@Param("sessionId")Long sessionId, @Param("limit")Long limit, @Param("userId")String userId);
}
