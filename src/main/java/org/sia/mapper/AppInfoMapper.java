package org.sia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.sia.model.AppCategory;
import org.sia.model.AppInfo;
import org.sia.vo.request.AdminAppSearchReqVo;
import org.sia.vo.response.AdminAppSearchResVo;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/8 21:08
 */
public interface AppInfoMapper extends BaseMapper<AppInfo> {
    Page<AdminAppSearchResVo> searchList(Page page, @Param("condition") AdminAppSearchReqVo condition);
}
