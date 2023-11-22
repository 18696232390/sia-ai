package org.sia.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.sia.model.AppCategory;
import org.sia.service.AppCategoryService;
import org.sia.service.AppInfoService;
import org.sia.vo.PageReqVo;
import org.sia.vo.R;
import org.sia.vo.request.*;
import org.sia.vo.response.AdminAppSearchResVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/23 16:56
 */
@RestController
@RequestMapping("v1/admin/category")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final AppCategoryService categoryService;

    /**
     * 分类列表
     *
     * @param group
     * @return
     */
    @GetMapping("list")
    public R<List<AppCategory>> list(@RequestParam(value = "group", required = false) String group) {
        return R.success(categoryService.list(Wrappers.<AppCategory>lambdaQuery()
                .eq(StrUtil.isNotBlank(group), AppCategory::getGroup, group)
                .orderByDesc(AppCategory::getSort))
        );
    }

    /**
     * 删除分类
     *
     * @return
     */
    @GetMapping("remove")
    public R<Void> remove(@RequestParam Long id) {
        categoryService.removeById(id);
        return R.success();
    }

    /**
     * 分类详情
     *
     * @return
     */
    @GetMapping("info")
    public R<AppCategory> info(@RequestParam Long id) {
        return R.success(categoryService.getById(id));
    }

    /**
     * 创建分类
     *
     * @return
     */
    @PostMapping("create")
    public R<Void> create(@RequestBody @Validated AdminCategoryCreateReqVo reqVo) {
        categoryService.create(reqVo);
        return R.success();
    }

    /**
     * 编辑分类
     *
     * @return
     */
    @PostMapping("edit")
    public R<Void> edit(@RequestBody @Validated AdminCategoryEditReqVo reqVo) {
        categoryService.edit(reqVo);
        return R.success();
    }
}
