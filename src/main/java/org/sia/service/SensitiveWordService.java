package org.sia.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.sia.mapper.AppCategoryMapper;
import org.sia.mapper.SensitiveWordMapper;
import org.sia.model.AppCategory;
import org.sia.model.SensitiveWord;
import org.sia.vo.PageReqVo;
import org.sia.vo.request.*;
import org.sia.vo.response.AdminSensitiveWordSearchResVo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/10 16:34
 */
@Slf4j
@Service
public class SensitiveWordService extends ServiceImpl<SensitiveWordMapper, SensitiveWord> {

    public void create(AdminSensitiveWordCreateReqVo reqVo) {
        SensitiveWord word = BeanUtil.toBean(reqVo, SensitiveWord.class);
        word.setCreateTime(LocalDateTime.now());
        word.setUpdateTime(LocalDateTime.now());
        this.save(word);
    }

    public void edit(AdminSensitiveWordEditReqVo reqVo) {
        SensitiveWord word = BeanUtil.toBean(reqVo, SensitiveWord.class);
        word.setUpdateTime(LocalDateTime.now());
        this.updateById(word);
    }

    public Page<AdminSensitiveWordSearchResVo> searchList(PageReqVo<AdminSensitiveWordSearchReqVo> reqVo) {
        return this.getBaseMapper().searchList(reqVo.getPage(),reqVo.getCondition());
    }

    public AdminSensitiveWordSearchResVo searchInfo(Long id) {
        return this.getBaseMapper().info(id);
    }
}
