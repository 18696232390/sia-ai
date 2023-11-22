package org.sia.service;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sia.enums.ConfigKeyEnum;
import org.sia.handler.RequestDataHandler;
import org.sia.mapper.InviteRecordMapper;
import org.sia.model.InviteRecord;
import org.sia.model.SystemConfig;
import org.sia.model.User;
import org.sia.vo.AdminSettingBaseInfoVo;
import org.sia.vo.AdminSettingInviteInfoVo;
import org.sia.vo.PageReqVo;
import org.sia.vo.response.InviteInfoResVo;
import org.sia.vo.response.InviteRecordResVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/14 20:36
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InviteService extends ServiceImpl<InviteRecordMapper, InviteRecord> {

    private final ConfigService configService;
    private final UserService userService;

    public InviteInfoResVo info() {
        // 查询当前登录用户信息
        String userId = RequestDataHandler.getUserId();
        User user = userService.getById(userId);
        // 获取邀请模板配置
        AdminSettingInviteInfoVo config = configService.getConfigObject(ConfigKeyEnum.SETTING_INVITE).toBean(AdminSettingInviteInfoVo.class);
        AdminSettingBaseInfoVo baseConfig = configService.getConfigObject(ConfigKeyEnum.SETTING_BASE).toBean(AdminSettingBaseInfoVo.class);
        // 组装数据
        InviteInfoResVo result = new InviteInfoResVo();
        String code = user.getInviteCode();
        result.setCode(code);
        result.setUrl(StrUtil.format("{}/login?type=register&code={}", baseConfig.getDomain(), code));

        result.setContent(StrUtil.format(config.getTemplate(),
                MapUtil.builder("{code}", code)
                        .put("{url}", result.getUrl())
                        .put("{integral}", config.getIIntegral().toString())
                        .put("{day}", config.getIExpireDay().toString())
                        .build()));
        return result;
    }

    public Page<InviteRecordResVo> list(PageReqVo reqVo) {
        return this.baseMapper.pageList(reqVo.getPage(), RequestDataHandler.getUserId());
    }
}
