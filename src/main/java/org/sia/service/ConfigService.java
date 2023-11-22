package org.sia.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sia.enums.ConfigKeyEnum;
import org.sia.enums.ResultEnum;
import org.sia.exception.BusinessException;
import org.sia.mapper.ConfigMapper;
import org.sia.model.SystemConfig;
import org.sia.vo.AdminChatBaseInfoVo;
import org.sia.vo.request.ConfigInitReqVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/10 16:34
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigService extends ServiceImpl<ConfigMapper, SystemConfig> implements InitializingBean {

    private final Map<String, String> CONFIG_MAP = Maps.newHashMap();
    private final Map<String, List<String>> IGNORE_MAP = Maps.newHashMap();

    public List<JSONObject> getConfigList(ConfigKeyEnum key) {
        return JSONUtil.parseArray(CONFIG_MAP.get(key.getKey())).toList(JSONObject.class);
    }

    public JSONObject getConfigObject(ConfigKeyEnum key) {
        return JSONUtil.parseObj(CONFIG_MAP.get(key.getKey()));
    }


    public Object getConfig(String key) {
        String configStr = CONFIG_MAP.get(key);
        if (StrUtil.isBlank(configStr)) {
            SystemConfig config = this.getById(key);
            configStr = config.getConfig();
            CONFIG_MAP.put(key, configStr);
            if (StrUtil.isNotBlank(config.getIgnore())) {
                IGNORE_MAP.put(key, StrUtil.split(config.getIgnore(), ","));
            }
        }
        if (JSONUtil.isTypeJSONObject(configStr)) {
            JSONObject result = JSONUtil.parseObj(configStr);
            List<String> ignoreKeyList = IGNORE_MAP.get(key);
            if (CollUtil.isNotEmpty(ignoreKeyList)){
                ignoreKeyList.forEach(result::remove);
            }
            return result;
        }
        return JSONUtil.parseArray(configStr);
    }

    public void updateConfig(ConfigInitReqVo config) {
        this.saveOrUpdate(BeanUtil.toBean(config, SystemConfig.class));
        CONFIG_MAP.put(config.getKey(), config.getConfig());
    }

    @Override
    public void afterPropertiesSet() {
        this.list().forEach(config -> {
            CONFIG_MAP.put(config.getKey(), config.getConfig());
            IGNORE_MAP.put(config.getKey(),StrUtil.split(config.getIgnore(),","));
        });
    }


}
