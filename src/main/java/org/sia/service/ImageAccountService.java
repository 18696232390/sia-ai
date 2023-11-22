package org.sia.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.sia.enums.ResultEnum;
import org.sia.exception.BusinessException;
import org.sia.mapper.ImageAccountMapper;
import org.sia.model.ImageAccount;
import org.sia.vo.PageReqVo;
import org.sia.vo.request.*;
import org.sia.vo.response.AdminImageAccountSearchResVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class ImageAccountService extends ServiceImpl<ImageAccountMapper, ImageAccount> {

    @Value("${midjourney.domain}")
    private String MJ_DOMAIN;

    public Page<AdminImageAccountSearchResVo> searchList(PageReqVo<AdminImageAccountSearchReqVo> reqVo) {
        return this.baseMapper.searchList(reqVo.getPage(), reqVo.getCondition());
    }

    public AdminImageAccountSearchResVo info(Long id) {
        return this.baseMapper.info(id);
    }

    public void create(AdminImageAccountCreateReqVo reqVo) {
        ImageAccount config = BeanUtil.toBean(reqVo, ImageAccount.class);
        config.setId(reqVo.getConfig().getLong("discordChannelId"));
        config.setConfig(reqVo.getConfig().toString());
        config.setCreateTime(LocalDateTime.now());
        config.setUpdateTime(LocalDateTime.now());
        config.setState(0);
        this.save(config);
    }

    public void edit(AdminImageAccountEditReqVo reqVo) {
        ImageAccount config = BeanUtil.toBean(reqVo, ImageAccount.class);
        config.setConfig(reqVo.getConfig().toString());
        config.setUpdateTime(LocalDateTime.now());
        this.updateById(config);
    }

    public void control(Long id, String action) {
        ImageAccount imageAccount = this.getById(id);
        if (ObjectUtil.isNull(imageAccount)) {
            throw new BusinessException(ResultEnum.DATA_EMPTY, "绘画账号");
        }
        JSONObject config = JSONUtil.parseObj(imageAccount.getConfig());
        HttpRequest post = HttpUtil.createPost(StrUtil.format("{}/config/action?type={}", MJ_DOMAIN, action))
                .body(JSONUtil.createObj()
                        .set("channelId", config.getStr("discordChannelId"))
                        .set("guildId", config.getStr("discordServerId"))
                        .set("userToken", config.getStr("discordUserToken"))
                        .set("timeoutMinutes", config.getInt("timeout"))
                        .set("queueSize", config.getInt("conQueueSize"))
                        .set("coreSize", config.getInt("waitQueueSize"))
                        .toString());
        if ("start".equals(action)) {
            if (imageAccount.getState() == 1) {
                throw new BusinessException(ResultEnum.ERROR, "服务已启动,请勿重复操作");
            }
            if ("ok".equals(post.execute().body())){
                imageAccount.setState(1);
            }
        } else if ("stop".equals(action)) {
            if (imageAccount.getState() == 0) {
                throw new BusinessException(ResultEnum.ERROR, "服务已停止,请勿重复操作");
            }
            if ("ok".equals(post.execute().body())){
                imageAccount.setState(0);
            }
        } else if ("restart".equals(action)) {
            if ("ok".equals(post.execute().body())){
                imageAccount.setState(1);
            }
        } else {
            throw new BusinessException(ResultEnum.NOT_SUPPORT_TYPE);
        }
        // 保存状态
        imageAccount.setUpdateTime(LocalDateTime.now());
        this.updateById(imageAccount);
    }
}
