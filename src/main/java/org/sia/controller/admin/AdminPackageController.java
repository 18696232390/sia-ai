package org.sia.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.sia.service.AppInfoService;
import org.sia.service.PackageService;
import org.sia.vo.PageReqVo;
import org.sia.vo.R;
import org.sia.vo.request.*;
import org.sia.vo.response.AdminAppSearchResVo;
import org.sia.vo.response.AdminPkgEditReqVo;
import org.sia.vo.response.AdminPkgSearchResVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/admin/package")
@RequiredArgsConstructor
public class AdminPackageController {

    private final PackageService packageService;

    /**
     * 套餐列表
     * @return
     */
    @PostMapping("searchList")
    public R<Page<AdminPkgSearchResVo>> searchList(@RequestBody @Validated PageReqVo<AdminPkgSearchReqVo> reqVo){
        return R.success(packageService.searchList(reqVo));
    }

    /**
     * 套餐简要列表
     * @return
     */
    @GetMapping("simpleList")
    public R<List<AdminPkgSearchResVo>> simpleList(){
        return R.success(packageService.simpleList());
    }

    /**
     * 删除套餐
     * @return
     */
    @GetMapping("remove")
    public R<Void> remove(@RequestParam Long id){
        packageService.removeById(id);
        return R.success();
    }

    /**
     * 套餐详情
     * @return
     */
    @GetMapping("info")
    public R<AdminPkgSearchResVo> info(@RequestParam Long id){
        return R.success(packageService.searchInfo(id));
    }

    /**
     * 创建套餐
     * @return
     */
    @PostMapping("create")
    public R<Void> create(@RequestBody @Validated AdminPkgCreateReqVo reqVo){
        packageService.create(reqVo);
        return R.success();
    }

    /**
     * 编辑套餐
     * @return
     */
    @PostMapping("edit")
    public R<Void> edit(@RequestBody @Validated AdminPkgEditReqVo reqVo){
        packageService.edit(reqVo);
        return R.success();
    }


    /**
     * 套餐类型列表
     * @return
     */
    @GetMapping("category/list")
    public R<List<JSONObject>> categoryList(){
        return R.success(packageService.categoryList());
    }

    /**
     * 套餐类型编辑
     * @return
     */
    @PostMapping("category/edit")
    public R<Void> editCategory(@RequestBody @Validated List<AdminPkgCategoryEditReqVo> reqVo){
        packageService.editCategory(reqVo);
        return R.success();
    }
}
