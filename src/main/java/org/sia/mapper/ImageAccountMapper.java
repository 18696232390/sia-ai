package org.sia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.sia.model.ChatAccount;
import org.sia.model.ImageAccount;
import org.sia.vo.request.AdminChatAccountSearchReqVo;
import org.sia.vo.request.AdminImageAccountSearchReqVo;
import org.sia.vo.response.AdminChatAccountResVo;
import org.sia.vo.response.AdminImageAccountSearchResVo;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/8 21:08
 */
public interface ImageAccountMapper extends BaseMapper<ImageAccount> {

    Page<AdminImageAccountSearchResVo> searchList(Page page, @Param("condition") AdminImageAccountSearchReqVo condition);

    AdminImageAccountSearchResVo info(@Param("id") Long id);
}
