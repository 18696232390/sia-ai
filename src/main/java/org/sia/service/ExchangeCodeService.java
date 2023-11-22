package org.sia.service;

import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sia.enums.ResultEnum;
import org.sia.exception.BusinessException;
import org.sia.handler.RequestDataHandler;
import org.sia.mapper.ExchangeCodeMapper;
import org.sia.model.ExchangeCode;
import org.sia.model.Package;
import org.sia.vo.PageReqVo;
import org.sia.vo.request.ExchangeCodeBatchGenerateReqVo;
import org.sia.vo.request.ExchangeReqVo;
import org.sia.vo.response.ExchangeResVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/13 18:41
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeCodeService extends ServiceImpl<ExchangeCodeMapper, ExchangeCode> {

    private final UserPackageService userPackageService;
    private final PackageService packageService;

    public Page<ExchangeResVo> list(PageReqVo<ExchangeReqVo> reqVo) {
        return this.baseMapper.searchList(reqVo.getPage(),reqVo.getCondition());
    }

    @Transactional
    public void trade(String code) {
        // 查询兑换码
        ExchangeCode exchangeCode = this.getOne(Wrappers.<ExchangeCode>lambdaQuery().eq(ExchangeCode::getCode, code));
        if (ObjectUtil.isNull(exchangeCode) || ObjectUtil.isNotEmpty(exchangeCode.getUserId())){
            throw new BusinessException(ResultEnum.DATA_EMPTY,"兑换码");
        }
        String userId = RequestDataHandler.getUserId();
        LocalDateTime now = LocalDateTime.now();
        // 兑换码信息修改
        exchangeCode.setUserId(userId);
        exchangeCode.setExchangeTime(now);
        this.updateById(exchangeCode);
        // 查询套餐包
        Package pkg = packageService.getById(exchangeCode.getPkgId());
        // 套餐包绑定
        userPackageService.bindUser(pkg,RequestDataHandler.getUserId(),exchangeCode.getId());
    }

    public void batchGenerate(ExchangeCodeBatchGenerateReqVo reqVo) {
        LocalDateTime now = LocalDateTime.now();
        List<ExchangeCode> exchangeCodeList = Lists.newArrayList();
        for (int i = 0; i < reqVo.getNumber(); i++) {
            ExchangeCode code = new ExchangeCode();
            code.setCode(IdUtil.nanoId());
            code.setPkgId(reqVo.getPkgId());
            code.setCreateTime(now);
            exchangeCodeList.add(code);
        }
        this.saveBatch(exchangeCodeList);
    }
    @Transactional
    public void remove(String ids) {
        if (StrUtil.isBlank(ids)){
            return;
        }
        List<String> idList = StrUtil.split(ids, ',');
        // 删除兑换码
        this.removeBatchByIds(idList);
        // 删除用户绑定的套餐包
        userPackageService.unBindUser(idList);
    }
}
