package org.sia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.sia.model.Order;
import org.sia.model.User;
import org.sia.vo.request.AdminOrderSearchReqVo;
import org.sia.vo.response.AdminOrderSearchResVo;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/8 21:08
 */
public interface OrderMapper extends BaseMapper<Order> {

    Page<AdminOrderSearchResVo> searchList(Page page, @Param("condition") AdminOrderSearchReqVo condition);

    AdminOrderSearchResVo searchInfo(@Param("id") Long id);
}
