
package org.sia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.sia.model.ChatSession;
import org.sia.vo.request.AdminSessionSearchReqVo;
import org.sia.vo.response.AdminSessionResVo;
import org.sia.vo.response.ChatSessionListResVo;

import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/8 21:08
 */
public interface ChatSessionMapper extends BaseMapper<ChatSession> {

    List<ChatSessionListResVo> getSessionList(@Param("keyword") String keyword,@Param("userId")String userId);

    Page<AdminSessionResVo> searchList(Page page, @Param("condition") AdminSessionSearchReqVo condition);
}
