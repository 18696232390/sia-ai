package org.sia.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.Platform;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sia.enums.OrderStateEnum;
import org.sia.enums.PayPlatformEnum;
import org.sia.enums.ResultEnum;
import org.sia.exception.BusinessException;
import org.sia.handler.RequestDataHandler;
import org.sia.mapper.OrderMapper;
import org.sia.model.Order;
import org.sia.model.Package;
import org.sia.vo.PageReqVo;
import org.sia.vo.request.AdminOrderSearchReqVo;
import org.sia.vo.response.AdminOrderSearchResVo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/9 21:12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService extends ServiceImpl<OrderMapper, Order> {

    private final PackageService packageService;

    public Page<AdminOrderSearchResVo> searchList(PageReqVo<AdminOrderSearchReqVo> reqVo) {
        return this.baseMapper.searchList(reqVo.getPage(), reqVo.getCondition());
    }

    public AdminOrderSearchResVo searchInfo(Long id) {
        return this.baseMapper.searchInfo(id);
    }

    public void checkState() {
        this.update(Wrappers.<Order>lambdaUpdate()
                .set(Order::getUpdateTime, LocalDateTime.now())
                .set(Order::getState, OrderStateEnum.CANCEL.getCode())
                .eq(Order::getState, OrderStateEnum.UNPAY.getCode())
                .le(Order::getCreateTime, DateUtil.offsetMinute(DateUtil.date(), -30).toLocalDateTime())
        );
    }

    /**
     * 根据套餐ID创建订单信息
     * @param packageId
     */
    public Order createOrderByPkgId(Long packageId, PayPlatformEnum platform){
        // 查询套餐信息
        Package pkgInfo = packageService.getById(packageId);
        if (ObjectUtil.isNull(pkgInfo)) {
            throw new BusinessException(ResultEnum.DATA_EMPTY, "套餐");
        }
        // 套餐价格
        int price = NumberUtil.mul(pkgInfo.getPrice(), pkgInfo.getDiscount()).intValue();
        // 生成订单ID
        String orderId = StrUtil.format("M{}{}", DateUtil.date().toString(DatePattern.PURE_DATETIME_MS_PATTERN), RandomUtil.randomNumbers(6));
        Order order = new Order();
        order.setId(orderId);
        order.setPlatform(platform.getValue());
        order.setPrice(price);
        order.setUserId(RequestDataHandler.getUserId());
        order.setPkgId(packageId);
        order.setDescription(pkgInfo.getName());
        order.setState(OrderStateEnum.UNPAY.getCode());
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        this.save(order);
        return order;
    }
}
