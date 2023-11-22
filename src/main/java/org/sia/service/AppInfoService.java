package org.sia.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sia.enums.CategoryGroupEnum;
import org.sia.enums.ResultEnum;
import org.sia.exception.BusinessException;
import org.sia.handler.RequestDataHandler;
import org.sia.mapper.AppInfoMapper;
import org.sia.model.AppCategory;
import org.sia.model.AppInfo;
import org.sia.model.ChatSession;
import org.sia.vo.PageReqVo;
import org.sia.vo.request.AdminAppCreateReqVo;
import org.sia.vo.request.AdminAppEditReqVo;
import org.sia.vo.request.AdminAppSearchReqVo;
import org.sia.vo.response.AdminAppSearchResVo;
import org.sia.vo.response.AppInfoResVo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/10 16:34
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppInfoService extends ServiceImpl<AppInfoMapper, AppInfo> {
    private final AppCategoryService appCategoryService;

    public List<AppInfoResVo> getList() {
        List<AppCategory> categoryList = appCategoryService.list(Wrappers.<AppCategory>lambdaQuery().eq(AppCategory::getGroup, CategoryGroupEnum.APP.getName()));
        List<AppInfo> appList = this.list(Wrappers.<AppInfo>lambdaQuery().eq(AppInfo::getIsPublic, 1));
        return categoryList.stream().map(category -> {
                    AppInfoResVo app = BeanUtil.toBean(category, AppInfoResVo.class);
                    app.setAppList(appList.stream()
                            .filter(data -> data.getCategoryId().equals(category.getId()))
                            .map(data -> {
                                AppInfoResVo.App dto = BeanUtil.toBean(data, AppInfoResVo.App.class);
                                return dto;
                            })
                            .sorted(Comparator.comparing(AppInfoResVo.App::getSort))
                            .collect(Collectors.toList()));
                    return app;
                }).sorted(Comparator.comparing(AppInfoResVo::getSort))
                .collect(Collectors.toList());
    }

    public AppInfoResVo.App info(Long id) {
        AppInfo app = this.getById(id);
        return BeanUtil.toBean(app, AppInfoResVo.App.class);
    }

    public Page<AdminAppSearchResVo> searchList(PageReqVo<AdminAppSearchReqVo> reqVo) {
        return this.baseMapper.searchList(reqVo.getPage(),reqVo.getCondition());
    }

    public AdminAppSearchResVo searchInfo(Long id) {
        AppInfo appInfo = this.getById(id);
        if (ObjectUtil.isNull(appInfo)){
            throw new BusinessException(ResultEnum.DATA_EMPTY,"应用");
        }
        return BeanUtil.toBean(appInfo,AdminAppSearchResVo.class);
    }

    public void create(AdminAppCreateReqVo reqVo) {
        AppInfo appInfo = BeanUtil.toBean(reqVo, AppInfo.class);
        appInfo.setCreateTime(LocalDateTime.now());
        appInfo.setUpdateTime(LocalDateTime.now());
        appInfo.setUserId(RequestDataHandler.getUserId());
        this.save(appInfo);
    }

    public void edit(AdminAppEditReqVo reqVo) {
        AppInfo appInfo = BeanUtil.toBean(reqVo, AppInfo.class);
        appInfo.setUpdateTime(LocalDateTime.now());
        appInfo.setUserId(RequestDataHandler.getUserId());
        this.updateById(appInfo);
    }

}
