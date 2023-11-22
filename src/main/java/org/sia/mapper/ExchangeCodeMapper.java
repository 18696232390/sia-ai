package org.sia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.sia.model.ExchangeCode;
import org.sia.vo.request.ExchangeReqVo;
import org.sia.vo.response.ExchangeResVo;

public interface ExchangeCodeMapper extends BaseMapper<ExchangeCode> {
    Page<ExchangeResVo> searchList(Page page, @Param("condition") ExchangeReqVo condition);

}