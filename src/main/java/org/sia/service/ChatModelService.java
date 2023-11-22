package org.sia.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.sia.enums.ConfigKeyEnum;
import org.sia.enums.ResultEnum;
import org.sia.exception.BusinessException;
import org.sia.mapper.ChatModelMapper;
import org.sia.model.ChatModel;
import org.sia.vo.request.AdminChatModelEditReqVo;
import org.sia.vo.response.AdminChatModelInfoResVO;
import org.sia.vo.response.ChatModelShowResVo;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatModelService extends ServiceImpl<ChatModelMapper, ChatModel> {

    public List<AdminChatModelInfoResVO> list(Integer custom) {
        return this.baseMapper.infoList(custom, null);
    }

    public List<AdminChatModelInfoResVO> usableList() {
        return this.baseMapper.infoList(null, 1);
    }
    public List<ChatModelShowResVo> showList() {
        return this.baseMapper.showList();
    }

    public void edit(List<AdminChatModelEditReqVo> reqVo) {
        this.remove(Wrappers.<ChatModel>lambdaQuery().eq(ChatModel::getCustom,1));
        // 检测是否有重复模型
        List<String> modelNameList = reqVo.stream()
                .map(AdminChatModelEditReqVo::getName)
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(modelNameList)){
            throw new BusinessException(ResultEnum.ERROR,"请填写自定义模型名称");
        }
        long flag = this.count(Wrappers.<ChatModel>lambdaQuery().in(ChatModel::getName, modelNameList));
        if (flag > 0){
            throw new BusinessException(ResultEnum.ERROR,"模型名称已存在");
        }
        List<ChatModel> modelList = reqVo.stream().map(data -> BeanUtil.toBean(data, ChatModel.class)).collect(Collectors.toList());
        this.saveBatch(modelList);
    }

    public void editDefault(AdminChatModelEditReqVo reqVo) {
        if (ObjectUtil.isNull(reqVo.getId())){
            throw new BusinessException(ResultEnum.ERROR,"请填写ID");
        }
        this.updateById(BeanUtil.toBean(reqVo,ChatModel.class));
    }

    public void delete(Long id) {
        this.remove(Wrappers.<ChatModel>lambdaQuery().eq(ChatModel::getId, id).eq(ChatModel::getCustom, 1));
    }


    public Long getCostQuota(String model) {
        if (StrUtil.hasBlank(model)) {
            throw new BusinessException(ResultEnum.DATA_EMPTY, "模型");
        }
        // 查询模型列表（在用）
        List<ChatModel> modelList = this.list(Wrappers.<ChatModel>lambdaQuery().eq(ChatModel::getOpen, 1));
        Optional<ChatModel> opt = modelList.stream().filter(modelDto -> modelDto.getName().equals(model)).sorted(Comparator.comparing(ChatModel::getCustom)).findFirst();
        return opt.map(chatModel -> chatModel.getIntegral().longValue()).orElse(0L);
    }
}
