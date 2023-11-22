package org.sia.controller.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.sia.service.InviteService;
import org.sia.vo.PageReqVo;
import org.sia.vo.R;
import org.sia.vo.response.InviteInfoResVo;
import org.sia.vo.response.InviteRecordResVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/14 20:32
 */
@RestController
@RequestMapping("v1/invite")
@RequiredArgsConstructor
public class InviteController {
    private final InviteService inviteService;

    /**
     * 我的邀请码
     * @return
     */
    @GetMapping("info")
    public R<InviteInfoResVo> info(){
        return R.success(inviteService.info());
    }

    /**
     * 邀请列表
     * @return
     */
    @PostMapping("list")
    public R<Page<InviteRecordResVo>> list(@RequestBody @Validated PageReqVo reqVo){
        return R.success(inviteService.list(reqVo));
    }

}
