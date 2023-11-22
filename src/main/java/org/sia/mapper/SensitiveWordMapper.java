package org.sia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.sia.model.SensitiveWord;
import org.sia.vo.request.AdminSensitiveWordSearchReqVo;
import org.sia.vo.response.AdminSensitiveWordSearchResVo;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/8 21:08
 */
public interface SensitiveWordMapper extends BaseMapper<SensitiveWord> {

    Page<AdminSensitiveWordSearchResVo> searchList(Page page, @Param("condition") AdminSensitiveWordSearchReqVo condition);

    AdminSensitiveWordSearchResVo info(@Param("id") Long id);
}
