package org.sia.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sia.enums.ResultEnum;
import org.sia.exception.BusinessException;
import org.sia.handler.RequestDataHandler;
import org.sia.mapper.ImagePromptMapper;
import org.sia.model.ImagePrompt;
import org.sia.vo.PageReqVo;
import org.sia.vo.request.ImagePromptConfigReqVo;
import org.sia.vo.request.ImagePromptSearchReqVo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/10 16:34
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImagePromptService extends ServiceImpl<ImagePromptMapper, ImagePrompt> {

    private final ConfigService configService;

    public void config(ImagePromptConfigReqVo reqVo) {
        ImagePrompt prompt = BeanUtil.toBean(reqVo,ImagePrompt.class);
        prompt.setConfig(reqVo.getConfig().toString());
        prompt.setUserId(RequestDataHandler.getUserId());
        prompt.setUpdateTime(LocalDateTime.now());
        this.saveOrUpdate(prompt);
    }

    public Page<JSONObject> searchList(PageReqVo<ImagePromptSearchReqVo> reqVo) {
        if (ObjectUtil.isNull(reqVo.getCondition())){
            reqVo.setCondition(new ImagePromptSearchReqVo());
        }
        reqVo.getCondition().setUserId(RequestDataHandler.getUserId());
        Page<JSONObject> result = this.baseMapper.searchList(reqVo.getPage(), reqVo.getCondition());
        result.getRecords().forEach(record->record.put("config",record.getJSONObject("config")));
        return result;
    }

    public JSONObject getConfigJson(Boolean isPublic,Long id) {
        ImagePrompt prompt = this.getById(id);
        if (ObjectUtil.isNull(prompt)){
            throw new BusinessException(ResultEnum.DATA_EMPTY,"自定义配置");
        }
        return JSONUtil.parseObj(prompt.getConfig());
    }
}
