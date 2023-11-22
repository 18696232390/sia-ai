package org.sia.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.sia.enums.ConfigKeyEnum;
import org.sia.enums.ResultEnum;
import org.sia.exception.BusinessException;
import org.sia.handler.RequestDataHandler;
import org.sia.mapper.UserPackageMapper;
import org.sia.model.ExchangeCode;
import org.sia.model.Package;
import org.sia.model.UserPackage;
import org.sia.vo.AdminSettingInviteInfoVo;
import org.sia.vo.AdminSettingNotifyInfoVo;
import org.sia.vo.AdminSettingPromptInfoVo;
import org.sia.vo.InviteDto;
import org.sia.vo.request.AdminPkgCategoryEditReqVo;
import org.sia.vo.request.AdminPkgCreateReqVo;
import org.sia.vo.request.AdminPkgSearchReqVo;
import org.sia.vo.request.AdminUpReqVo;
import org.sia.vo.response.AdminPkgEditReqVo;
import org.sia.vo.response.AdminUpResVo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/9 11:58
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserPackageService extends ServiceImpl<UserPackageMapper, UserPackage> {

    private final ConfigService configService;


    /**
     * 扣除积分
     *
     * @return
     */
    public void decrease(String userId, Long quota,Map<Long,Long> temp) {
        if (quota == 0) {
            return;
        }
        // 查询当前生效的套餐包，且剩余积分额度>=quota，套餐截止时间最早的
        log.info(">>> 扣除用户{}积分:{}", userId, quota);
        List<UserPackage> upList = this.baseMapper.getQuotaPkg(userId, quota, DateUtil.now());
        // 套餐包额度已用尽
        if (CollUtil.isEmpty(upList) || upList.stream().mapToLong(UserPackage::getRemain).sum() < quota) {
            AdminSettingPromptInfoVo config = configService.getConfigObject(ConfigKeyEnum.SETTING_PROMPT).toBean(AdminSettingPromptInfoVo.class);
            throw new BusinessException(ResultEnum.GPT_NOT_PKG,config.getQuoteTip());
        }
        // 套餐包积分额度扣除
        List<UserPackage> updateUpList = Lists.newArrayList();
        for (UserPackage up : upList) {
            if (quota == 0L){
                break;
            }
            if (up.getRemain() < quota) {
                quota -= up.getRemain();
                up.setRemain(0L);
                temp.put(up.getId(),up.getRemain());
            } else {
                quota = 0L;
                up.setRemain(up.getRemain()-quota);
                temp.put(up.getId(),quota);
            }
            updateUpList.add(up);
        }
        this.updateBatchById(updateUpList);
    }

    /**
     * 返还套餐包积分
     *
     * @return
     */
    public void increase(Map<Long,Long> pkgCostMap) {
        if (MapUtil.isEmpty(pkgCostMap)) {
            return;
        }
        List<UserPackage> pkgList = this.listByIds(pkgCostMap.keySet().stream().toList());
        for (UserPackage pkg : pkgList) {
            // 套餐包积分额度返还
            pkg.setRemain(pkg.getRemain() + pkgCostMap.getOrDefault(pkg.getId(),0L));
        }
        this.updateBatchById(pkgList);
    }

    /**
     * 重置套餐包
     */
    public void reset() {
        LocalDateTime now = LocalDateTime.now();
        // 查询套餐截止时间大于当前时间且each_day=1的套餐包
        List<UserPackage> userPackageList = this.list(Wrappers.<UserPackage>lambdaQuery()
                .eq(UserPackage::getEachDay, 1)
                .gt(UserPackage::getEndTime, now));
        if (CollUtil.isEmpty(userPackageList)) {
            log.info(">>> 没有需要重置的套餐包");
        }
        userPackageList.forEach(up -> {
            // 重置时间
            up.setResetTime(now);
            // 重置积分
            up.setRemain(up.getIntegral());
        });
        this.updateBatchById(userPackageList);
    }


    /**
     * 新用户尝鲜套餐绑定
     *
     * @param userId
     */
    public void bindNewUser(String userId) {
        AdminSettingInviteInfoVo config = configService.getConfigObject(ConfigKeyEnum.SETTING_INVITE).toBean(AdminSettingInviteInfoVo.class);
        // 用户绑定套餐包
        LocalDateTime now = LocalDateTime.now();
        UserPackage userPackage = new UserPackage();
        userPackage.setPkgId(0L);
        userPackage.setUserId(userId);
        userPackage.setIntegral(config.getNIntegral().longValue());
        userPackage.setRemain(config.getNIntegral().longValue());
        userPackage.setEachDay(0);
        userPackage.setName("新人注册奖励");
        userPackage.setCategory("积分卡");
        // 套餐包生效时间
        userPackage.setStartTime(now);
        userPackage.setEndTime(DateUtil.offsetDay(DateUtil.date(), config.getNExpireDay()).toLocalDateTime());
        this.save(userPackage);
    }

    /**
     * 绑定用户套餐包
     *
     * @param packageInfo
     * @param userId
     */
    public void bindUser(Package packageInfo, String userId, Long exchangeId) {
        // 用户绑定套餐包
        LocalDateTime now = LocalDateTime.now();
        UserPackage userPackage = new UserPackage();
        userPackage.setPkgId(packageInfo.getId());
        userPackage.setUserId(userId);
        userPackage.setIntegral(packageInfo.getIntegral());
        userPackage.setEachDay(packageInfo.getEachDay());
        userPackage.setRemain(packageInfo.getIntegral());
        userPackage.setName(packageInfo.getName());
        // 套餐类型查询
        configService.getConfigList(ConfigKeyEnum.PKG_CATEGORY)
                .stream()
                .map(config -> config.toBean(AdminPkgCategoryEditReqVo.class))
                .filter(config -> config.getKey().equals(packageInfo.getCategory()))
                .findFirst().ifPresent(config -> userPackage.setCategory(config.getName()));
        // 套餐包生效时间
        userPackage.setStartTime(now);
        userPackage.setExchangeId(exchangeId);
        userPackage.setEndTime(DateUtil.offsetDay(DateUtil.date(), packageInfo.getExpireDay().intValue()).toLocalDateTime());
        // 是否是积分卡
        if (packageInfo.getEachDay() == 1) {
            userPackage.setResetTime(now);
        }
        this.save(userPackage);
    }


    /**
     * 绑定用户邀请码
     *
     * @param inviteDto
     */
    public void bindUser(InviteDto inviteDto) {
        // 用户绑定邀请码
        LocalDateTime now = LocalDateTime.now();
        UserPackage userPackage = new UserPackage();
        userPackage.setPkgId(inviteDto.getId());
        userPackage.setUserId(inviteDto.getFrom());
        userPackage.setIntegral(inviteDto.getIntegral());
        userPackage.setEachDay(0);
        userPackage.setRemain(inviteDto.getIntegral());
        userPackage.setName(inviteDto.getName());
        userPackage.setCategory(inviteDto.getCategory());
        // 套餐包生效时间
        userPackage.setStartTime(now);
        userPackage.setEndTime(DateUtil.offsetDay(DateUtil.date(), inviteDto.getExpireDay().intValue()).toLocalDateTime());
        // 邀请者
        this.save(userPackage);
        // 被邀请者
        userPackage.setUserId(inviteDto.getTo());
        this.save(userPackage);

    }

    /**
     * 套餐包解绑(兑换码)
     *
     * @param exchangeIds
     */
    public void unBindUser(List<String> exchangeIds) {
        this.remove(Wrappers.<UserPackage>lambdaQuery()
                .in(UserPackage::getExchangeId, exchangeIds));
    }

}
