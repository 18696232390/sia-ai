package org.sia.controller.admin;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.sia.model.User;
import org.sia.service.AppInfoService;
import org.sia.service.UserService;
import org.sia.vo.PageReqVo;
import org.sia.vo.R;
import org.sia.vo.request.*;
import org.sia.vo.response.AdminAppSearchResVo;
import org.sia.vo.response.AdminUpResVo;
import org.sia.vo.response.AdminUserSearchResVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    /**
     * 用户列表
     * @return
     */
    @PostMapping("searchList")
    public R<Page<AdminUserSearchResVo>> searchList(@RequestBody @Validated PageReqVo<AdminUserSearchReqVo> reqVo){
        return R.success(userService.searchList(reqVo));
    }

    /**
     * 删除用户
     * @return
     */
    @GetMapping("remove")
    public R<Void> remove(@RequestParam Long id){
        userService.remove(Wrappers.<User>lambdaQuery().eq(User::getUserId,id));
        return R.success();
    }

    /**
     * 用户详情
     * @return
     */
    @GetMapping("info")
    public R<AdminUserSearchResVo> info(@RequestParam String userId){
        return R.success(userService.searchInfo(userId));
    }


    /**
     * 编辑用户
     * @return
     */
    @PostMapping("edit")
    public R<Void> edit(@RequestBody @Validated AdminUserEditReqVo reqVo){
        userService.edit(reqVo);
        return R.success();
    }

    /**
     * 重置密码
     * @param reqVo
     * @return
     */
    @PostMapping("password/reset")
    public R<Void> resetPassword(@RequestBody @Validated AdminPwdResetReqVo reqVo){
        userService.resetPassword(reqVo);
        return R.success();
    }

    /**
     * 用户套餐列表
     * @param reqVo
     * @return
     */
    @PostMapping("/package/searchList")
    public R<Page<AdminUpResVo>> searchUpList(@RequestBody @Validated PageReqVo<AdminUpReqVo> reqVo){
        return R.success(userService.searchUpList(reqVo));
    }


    /**
     * 删除用户套餐
     * @param id
     * @return
     */
    @GetMapping("/package/remove")
    public R<Void> removeUp(@RequestParam Long id){
        userService.removeUp(id);
        return R.success();
    }

    /**
     * 编辑用户套餐
     * @param reqVo
     * @return
     */
    @PostMapping("/package/edit")
    public R<Void> editUp(@RequestBody @Validated AdminUpEditReqVo reqVo){
        userService.editUp(reqVo);
        return R.success();
    }

}
