package org.sia.controller.admin;

import lombok.RequiredArgsConstructor;
import org.sia.service.AdminService;
import org.sia.vo.R;
import org.sia.vo.response.HomeInfoResVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/23 16:56
 */
@RestController
@RequestMapping("v1/admin/home")
@RequiredArgsConstructor
public class AdminHomeController {

    private final AdminService adminService;

    /**
     * 统计信息
     * @return
     */
    @GetMapping("info")
    public R<HomeInfoResVo> info(){
        return R.success(adminService.info());
    }
}
