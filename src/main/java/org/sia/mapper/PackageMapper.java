package org.sia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.sia.model.Package;
import org.sia.model.User;
import org.sia.vo.request.AdminPkgSearchReqVo;
import org.sia.vo.response.AdminPkgSearchResVo;
import org.sia.vo.response.PackageListResVo;
import org.sia.vo.response.PackageUserResVo;

import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/8 21:08
 */
public interface PackageMapper extends BaseMapper<Package> {
    List<PackageListResVo> getList();

    Page<PackageUserResVo> getMyList(Page page, @Param("userId") String userId);

    Page<AdminPkgSearchResVo> searchList(Page page, @Param("condition") AdminPkgSearchReqVo condition);

    AdminPkgSearchResVo searchInfo(@Param("id") Long id);

    List<AdminPkgSearchResVo> simpleList();
}
