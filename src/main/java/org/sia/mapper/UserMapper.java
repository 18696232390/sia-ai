package org.sia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.sia.model.User;
import org.sia.vo.request.AdminUserSearchReqVo;
import org.sia.vo.response.AdminUserSearchResVo;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/8 21:08
 */
public interface UserMapper extends BaseMapper<User> {

    Page<AdminUserSearchResVo> searchList(Page page, @Param("condition") AdminUserSearchReqVo condition);

    AdminUserSearchResVo info(@Param("userId") String userId);
}
