package org.sia.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sia.enums.ConfigKeyEnum;
import org.sia.handler.RequestDataHandler;
import org.sia.mapper.PackageMapper;
import org.sia.model.Package;
import org.sia.vo.PageReqVo;
import org.sia.vo.request.*;
import org.sia.vo.response.AdminPkgEditReqVo;
import org.sia.vo.response.AdminPkgSearchResVo;
import org.sia.vo.response.PackageListResVo;
import org.sia.vo.response.PackageUserResVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/9 11:58
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PackageService extends ServiceImpl<PackageMapper, Package> {

    private final ConfigService configService;

    public List<PackageListResVo> getList() {
        return this.baseMapper.getList();
    }

    public Page<PackageUserResVo> getMyList(PageReqVo<PackageUserReqVo> reqVo) {
        // 查询当前用户的套餐包
        return this.baseMapper.getMyList(reqVo.getPage(),RequestDataHandler.getUserId());
    }

    public Page<AdminPkgSearchResVo> searchList(PageReqVo<AdminPkgSearchReqVo> reqVo) {
        return this.baseMapper.searchList(reqVo.getPage(),reqVo.getCondition());
    }

    public AdminPkgSearchResVo searchInfo(Long id) {
        return this.baseMapper.searchInfo(id);
    }

    public void create(AdminPkgCreateReqVo reqVo) {
        Package pkg = BeanUtil.toBean(reqVo, Package.class);
        pkg.setCreateTime(LocalDateTime.now());
        pkg.setUpdateTime(LocalDateTime.now());
        this.save(pkg);
    }

    public void edit(AdminPkgEditReqVo reqVo) {
        Package pkg = BeanUtil.toBean(reqVo, Package.class);
        pkg.setUpdateTime(LocalDateTime.now());
        this.updateById(pkg);
    }

    public List<JSONObject> categoryList() {
        return configService.getConfigList(ConfigKeyEnum.PKG_CATEGORY);
    }

    public void editCategory(List<AdminPkgCategoryEditReqVo> reqVo) {
        ConfigInitReqVo config = new ConfigInitReqVo();
        config.setKey(ConfigKeyEnum.PKG_CATEGORY.getKey());
        config.setName("套餐类型");
        config.setConfig(JSONUtil.toJsonStr(reqVo));
        configService.updateConfig(config);
    }

    public List<AdminPkgSearchResVo> simpleList() {
        return this.baseMapper.simpleList();
    }
}
