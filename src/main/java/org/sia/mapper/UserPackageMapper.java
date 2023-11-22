package org.sia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.sia.model.Package;
import org.sia.model.UserPackage;
import org.sia.vo.request.AdminUpReqVo;
import org.sia.vo.response.AdminUpResVo;
import org.sia.vo.response.PackageListResVo;

import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/8 21:08
 */
public interface UserPackageMapper extends BaseMapper<UserPackage> {
    List<UserPackage> getQuotaPkg(@Param("userId") String email, @Param("quota") Long quota,@Param("date")String date);

    Page<AdminUpResVo> searchList(Page page, @Param("condition") AdminUpReqVo condition);
}
