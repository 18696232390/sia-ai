package org.sia.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.sia.service.AppInfoService;
import org.sia.service.OrderService;
import org.sia.vo.PageReqVo;
import org.sia.vo.R;
import org.sia.vo.request.AdminAppCreateReqVo;
import org.sia.vo.request.AdminAppEditReqVo;
import org.sia.vo.request.AdminAppSearchReqVo;
import org.sia.vo.request.AdminOrderSearchReqVo;
import org.sia.vo.response.AdminAppSearchResVo;
import org.sia.vo.response.AdminOrderSearchResVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("v1/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    /**
     * 订单列表
     * @return
     */
    @PostMapping("searchList")
    public R<Page<AdminOrderSearchResVo>> searchList(@RequestBody @Validated PageReqVo<AdminOrderSearchReqVo> reqVo){
        return R.success(orderService.searchList(reqVo));
    }


    /**
     * 删除订单
     * @return
     */
    @GetMapping("remove")
    public R<AdminOrderSearchResVo> remove(@RequestParam String id){
        orderService.removeById(id);
        return R.success();
    }


    /**
     * 订单详情
     * @return
     */
    @GetMapping("info")
    public R<AdminOrderSearchResVo> info(@RequestParam Long id){
        return R.success(orderService.searchInfo(id));
    }


}
