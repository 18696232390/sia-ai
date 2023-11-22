package org.sia.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.sia.service.AppInfoService;
import org.sia.service.SensitiveWordService;
import org.sia.vo.PageReqVo;
import org.sia.vo.R;
import org.sia.vo.request.*;
import org.sia.vo.response.AdminAppSearchResVo;
import org.sia.vo.response.AdminSensitiveWordSearchResVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/23 16:56
 */
@RestController
@RequestMapping("v1/admin/content/safe")
@RequiredArgsConstructor
public class AdminContentSafeController {

    private final SensitiveWordService sensitiveWordService;

    /**
     * [敏感词库]列表
     * @return
     */
    @PostMapping("/sensitiveWord/searchList")
    public R<Page<AdminSensitiveWordSearchResVo>> searchSensitiveWordList(@RequestBody @Validated PageReqVo<AdminSensitiveWordSearchReqVo> reqVo){
        return R.success(sensitiveWordService.searchList(reqVo));
    }

    /**
     * [敏感词库]删除敏感词
     * @return
     */
    @GetMapping("/sensitiveWord/remove")
    public R<Void> removeSensitiveWord(@RequestParam Long id){
        sensitiveWordService.removeById(id);
        return R.success();
    }

    /**
     * [敏感词库]敏感词详情
     * @return
     */
    @GetMapping("/sensitiveWord/info")
    public R<AdminSensitiveWordSearchResVo> sensitiveWordInfo(@RequestParam Long id){
        return R.success(sensitiveWordService.searchInfo(id));
    }

    /**
     * [敏感词库]创建
     * @return
     */
    @PostMapping("/sensitiveWord/create")
    public R<Void> createSensitiveWord(@RequestBody @Validated AdminSensitiveWordCreateReqVo reqVo){
        sensitiveWordService.create(reqVo);
        return R.success();
    }

    /**
     * [敏感词库]编辑
     * @return
     */
    @PostMapping("/sensitiveWord/edit")
    public R<Void> editSensitiveWord(@RequestBody @Validated AdminSensitiveWordEditReqVo reqVo){
        sensitiveWordService.edit(reqVo);
        return R.success();
    }
}
