package org.sia.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.sia.mapper.AppCategoryMapper;
import org.sia.model.AppCategory;
import org.sia.vo.request.AdminCategoryCreateReqVo;
import org.sia.vo.request.AdminCategoryEditReqVo;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/10 16:34
 */
@Slf4j
@Service
public class AppCategoryService extends ServiceImpl<AppCategoryMapper, AppCategory> {

    public void create(AdminCategoryCreateReqVo reqVo) {
        AppCategory category = BeanUtil.toBean(reqVo, AppCategory.class);
        this.save(category);
    }

    public void edit(AdminCategoryEditReqVo reqVo) {
        AppCategory category = BeanUtil.toBean(reqVo, AppCategory.class);
        this.updateById(category);
    }
}
