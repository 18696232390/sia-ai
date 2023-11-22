package org.sia.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.RequiredArgsConstructor;
import org.sia.service.AdminService;
import org.sia.service.ImageAccountService;
import org.sia.vo.PageReqVo;
import org.sia.vo.R;
import org.sia.vo.request.AdminImageAccountCreateReqVo;
import org.sia.vo.request.AdminImageAccountEditReqVo;
import org.sia.vo.request.AdminImageAccountSearchReqVo;
import org.sia.vo.request.ImageTaskSearchReqVo;
import org.sia.vo.response.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/23 16:56
 */
@RestController
@RequestMapping("v1/admin/image")
@RequiredArgsConstructor
public class AdminImageController {

    private final AdminService adminService;
    private final ImageAccountService imageAccountService;

    /**
     * 绘画列表
     *
     * @return
     */
    @PostMapping("searchList")
    public R<Page<ImageTaskSearchResVo>> searchList(@RequestBody @Validated PageReqVo<ImageTaskSearchReqVo> reqVo) {
        return R.success(adminService.searchImageList(reqVo));
    }


    /**
     * 删除记录
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/delete")
    public R<Void> deleteImage(@RequestParam String id) {
        adminService.deleteImage(id);
        return R.success();
    }


    /**
     * 绘画服务状态信息
     *
     * @return
     */
    @GetMapping(value = "/service/info")
    public R<ImageServiceInfoResVo> serviceInfo() {
        return R.success(adminService.serviceInfo());
    }


    /**
     * 绘画账号池列表
     *
     * @param reqVo
     * @return
     */
    @PostMapping(value = "/account/searchList")
    public R<Page<AdminImageAccountSearchResVo>> searchAccountList(@RequestBody @Validated PageReqVo<AdminImageAccountSearchReqVo> reqVo) {
        return R.success(imageAccountService.searchList(reqVo));
    }

    /**
     * 创建绘画账号
     *
     * @param reqVo
     * @return
     */
    @PostMapping(value = "/account/create")
    public R<Void> createAccount(@RequestBody @Validated AdminImageAccountCreateReqVo reqVo) {
        imageAccountService.create(reqVo);
        return R.success();
    }

    /**
     * 编辑绘画账号
     *
     * @param reqVo
     * @return
     */
    @PostMapping(value = "/account/edit")
    public R<Void> editAccount(@RequestBody @Validated AdminImageAccountEditReqVo reqVo) {
        imageAccountService.edit(reqVo);
        return R.success();
    }

    /**
     * 绘画账号详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/account/info")
    public R<AdminImageAccountSearchResVo> accountInfo(@RequestParam Long id) {
        return R.success(imageAccountService.info(id));
    }


    /**
     * 删除绘画账号
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/account/delete")
    public R<Void> deleteAccount(@RequestParam Long id) {
        imageAccountService.removeById(id);
        return R.success();
    }


    /**
     * 绘画账号服务启动/重启/停止
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/account/control")
    public R<Void> controlAccount(@RequestParam Long id, @RequestParam String action) {
        imageAccountService.control(id, action);
        return R.success();
    }
}
