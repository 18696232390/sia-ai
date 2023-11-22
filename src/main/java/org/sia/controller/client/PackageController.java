package org.sia.controller.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.sia.service.PackageService;
import org.sia.vo.PageReqVo;
import org.sia.vo.R;
import org.sia.vo.request.PackageUserReqVo;
import org.sia.vo.response.PackageListResVo;
import org.sia.vo.response.PackageUserResVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 会员套餐
 * @Author: 高灶顺
 * @CreateDate: 2023/8/9 11:29
 */
@RestController
@RequestMapping("v1/package")
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;

    /**
     * 套餐列表
     * @return
     */
    @GetMapping("list")
    public R<List<PackageListResVo>> list() {
        return R.success(packageService.getList());
    }

    /**
     * 我的套餐
     * @return
     */
    @PostMapping("user/myList")
    public R<Page<PackageUserResVo>> myList(@RequestBody @Validated PageReqVo<PackageUserReqVo> reqVo) {
        return R.success(packageService.getMyList(reqVo));
    }

}
