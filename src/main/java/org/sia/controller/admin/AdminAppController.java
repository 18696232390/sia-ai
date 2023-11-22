package org.sia.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.sia.service.AdminService;
import org.sia.service.AppInfoService;
import org.sia.vo.PageReqVo;
import org.sia.vo.R;
import org.sia.vo.request.AdminAppCreateReqVo;
import org.sia.vo.request.AdminAppEditReqVo;
import org.sia.vo.request.AdminAppSearchReqVo;
import org.sia.vo.response.AdminAppSearchResVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/23 16:56
 */
@RestController
@RequestMapping("v1/admin/app")
@RequiredArgsConstructor
public class AdminAppController {

    private final AppInfoService appInfoService;

    /**
     * 应用列表
     * @return
     */
    @PostMapping("searchList")
    public R<Page<AdminAppSearchResVo>> searchList(@RequestBody @Validated PageReqVo<AdminAppSearchReqVo> reqVo){
        return R.success(appInfoService.searchList(reqVo));
    }

    /**
     * 删除应用
     * @return
     */
    @GetMapping("remove")
    public R<Void> remove(@RequestParam Long id){
        appInfoService.removeById(id);
        return R.success();
    }

    /**
     * 应用详情
     * @return
     */
    @GetMapping("info")
    public R<AdminAppSearchResVo> info(@RequestParam Long id){
        return R.success(appInfoService.searchInfo(id));
    }

    /**
     * 创建应用
     * @return
     */
    @PostMapping("create")
    public R<Void> create(@RequestBody @Validated AdminAppCreateReqVo reqVo){
        appInfoService.create(reqVo);
        return R.success();
    }

    /**
     * 编辑应用
     * @return
     */
    @PostMapping("edit")
    public R<Void> edit(@RequestBody @Validated AdminAppEditReqVo reqVo){
        appInfoService.edit(reqVo);
        return R.success();
    }
}
