package org.sia.controller.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.sia.service.AppInfoService;
import org.sia.service.PackageService;
import org.sia.vo.PageReqVo;
import org.sia.vo.R;
import org.sia.vo.request.PackageUserReqVo;
import org.sia.vo.response.AppInfoResVo;
import org.sia.vo.response.PackageListResVo;
import org.sia.vo.response.PackageUserResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("v1/app")
@RequiredArgsConstructor
public class AppController {

    private final AppInfoService appInfoService;

    /**
     * 应用列表
     * @return
     */
    @GetMapping("list")
    public R<List<AppInfoResVo>> list() {
        return R.success(appInfoService.getList());
    }

    /**
     * 应用详情
     * @param id
     * @return
     */
    @GetMapping("info")
    public R<AppInfoResVo.App> info(@RequestParam Long id) {
        return R.success(appInfoService.info(id));
    }

}
