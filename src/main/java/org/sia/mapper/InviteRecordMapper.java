package org.sia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.sia.model.AppCategory;
import org.sia.model.InviteRecord;
import org.sia.vo.response.InviteRecordResVo;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/8 21:08
 */
public interface InviteRecordMapper extends BaseMapper<InviteRecord> {

    Page<InviteRecordResVo> pageList(Page page, @Param("userId") String userId);
}
