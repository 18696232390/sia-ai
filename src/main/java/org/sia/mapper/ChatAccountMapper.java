package org.sia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.sia.model.ChatAccount;
import org.sia.vo.request.AdminChatAccountSearchReqVo;
import org.sia.vo.response.AdminChatAccountResVo;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/8 21:08
 */
public interface ChatAccountMapper extends BaseMapper<ChatAccount> {

    Page<AdminChatAccountResVo> searchList(Page page, @Param("condition") AdminChatAccountSearchReqVo condition);

    AdminChatAccountResVo info(@Param("id") Long id);
}
