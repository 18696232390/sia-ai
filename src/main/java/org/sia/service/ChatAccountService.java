package org.sia.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sia.enums.ChatPlatformEnum;
import org.sia.enums.ResultEnum;
import org.sia.exception.BusinessException;
import org.sia.mapper.ChatAccountMapper;
import org.sia.model.ChatAccount;
import org.sia.vo.PageReqVo;
import org.sia.vo.request.AdminChatAccountCreateReqVo;
import org.sia.vo.request.AdminChatAccountEditReqVo;
import org.sia.vo.request.AdminChatAccountSearchReqVo;
import org.sia.vo.response.AdminChatAccountResVo;
import org.sia.vo.response.AdminChatPlatformResVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatAccountService extends ServiceImpl<ChatAccountMapper, ChatAccount> implements InitializingBean {


    private final static Map<String,List<ChatPlatformEnum>> platformMap = Maps.newConcurrentMap();

    public Page<AdminChatAccountResVo> searchList(PageReqVo<AdminChatAccountSearchReqVo> reqVo) {
        return this.baseMapper.searchList(reqVo.getPage(),reqVo.getCondition());
    }

    public AdminChatAccountResVo info(Long id) {
        return this.baseMapper.info(id);
    }

    public void create(AdminChatAccountCreateReqVo reqVo) {
        ChatAccount config = BeanUtil.toBean(reqVo, ChatAccount.class);
        config.setCreateTime(LocalDateTime.now());
        config.setUpdateTime(LocalDateTime.now());
        config.setState(0);
        this.save(config);
        this.intPlatformModelList();
        SpringUtil.getBean(ChatMessageService.class).initChatGptConfig();
    }

    public void edit(AdminChatAccountEditReqVo reqVo) {
        ChatAccount config = BeanUtil.toBean(reqVo, ChatAccount.class);
        config.setUpdateTime(LocalDateTime.now());
        this.updateById(config);
        this.intPlatformModelList();
        SpringUtil.getBean(ChatMessageService.class).initChatGptConfig();
    }

    public void test(Long id) {
        // GPT账号信息
//        ChatAccount config = this.getById(id);
    }

    public List<AdminChatPlatformResVo> platformList() {
        return Arrays.stream(ChatPlatformEnum.values()).map(dto->{
            AdminChatPlatformResVo data =new AdminChatPlatformResVo();
            data.setKey(dto.getKey());
            data.setValue(dto.getValue());
            return data;
        }).collect(Collectors.toList());
    }

    public void updateValid(Long id) {
        ChatAccount config = this.getById(id);
        if (ObjectUtil.isNull(config)){
            throw new BusinessException(ResultEnum.DATA_EMPTY,"对话账号");
        }
        config.setIsValid(config.getIsValid() == 0?1:0);
        config.setUpdateTime(LocalDateTime.now());
        this.updateById(config);
        this.intPlatformModelList();
        SpringUtil.getBean(ChatMessageService.class).initChatGptConfig();
    }


    public ChatPlatformEnum getPlatform(String model) {
        List<ChatPlatformEnum> platformList = platformMap.get(model);
        if (CollUtil.isEmpty(platformList)){
            throw new BusinessException(ResultEnum.FAIL,"不存在的模型");
        }
        return platformList.get(RandomUtil.randomInt(platformList.size()));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.intPlatformModelList();
    }

    private void intPlatformModelList(){
        // 可用平台模型集信息
        List<ChatAccount> accountList = this.list(Wrappers.<ChatAccount>lambdaQuery().eq(ChatAccount::getIsValid, 1));
        // 遍历循环
        for (ChatAccount chatAccount : accountList) {
            String platformKey = chatAccount.getPlatform();
            ChatPlatformEnum platform = ChatPlatformEnum.get(platformKey);
            for (String model : StrUtil.split(chatAccount.getModel(), ',')) {
                List<ChatPlatformEnum> platformList = platformMap.getOrDefault(model, Lists.newArrayList());
                platformList.add(platform);
                platformMap.put(model,platformList);
            }
        }
    }
}
