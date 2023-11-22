package org.sia.service;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.*;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import cn.hutool.json.JSONObject;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.Detail;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sia.config.AlipayConfig;
import org.sia.config.WxPayConfig;
import org.sia.enums.ConfigKeyEnum;
import org.sia.enums.OrderStateEnum;
import org.sia.enums.PayPlatformEnum;
import org.sia.enums.ResultEnum;
import org.sia.exception.BusinessException;
import org.sia.exception.CallBackException;
import org.sia.model.Order;
import org.sia.model.Package;
import org.sia.vo.AdminSettingPayInfoVo;
import org.sia.vo.request.WxPayNotifyReqVo;
import org.sia.vo.response.PayOrderInfoResVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/9 21:12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayService implements InitializingBean {

    private final AlipayConfig alipayConfig;
    private final WxPayConfig wxPayConfig;
    private final PackageService packageService;
    private final OrderService orderService;
    private final UserPackageService userPackageService;
    private final ConfigService configService;
    // 支付二维码参数
    private static final int QRCODE_HEIGHT = 300;
    private static final int QRCODE_WIDTH = 300;
    private static final String QRCODE_TYPE = "png";

    private static final Image ALIPAY_LOGO = ImgUtil.read(ResourceUtil.getStream("logo/alipay.png"));
    private static final Image WXPAY_LOGO = ImgUtil.read(ResourceUtil.getStream("logo/wxpay.png"));

    private RSAAutoCertificateConfig wxPayCaConfig;

    @Override
    public void afterPropertiesSet() {
        // 初始化alipay
        Factory.setOptions(alipayConfig);
        // 初始化wxpay配置
        this.wxPayCaConfig = this.getWxPayConfig();
    }

    private RSAAutoCertificateConfig getWxPayConfig() {
        AdminSettingPayInfoVo config = configService.getConfigObject(ConfigKeyEnum.SETTING_PAY).toBean(AdminSettingPayInfoVo.class);

        // 使用自动更新平台证书的RSA配置
        // 建议将 config 作为单例或全局静态对象，避免重复的下载浪费系统资源
        return new RSAAutoCertificateConfig.Builder()
                .merchantId(config.getWxMchId())
                .privateKey(config.getWxPrivateKey())
                .merchantSerialNumber(config.getWxMchSerialNumber())
                .apiV3Key(config.getWxApiV3Key())
                .build();
    }

    public PayOrderInfoResVo prePayAlipayInfo(Long packageId) {
        // 创建订单
        Order order = orderService.createOrderByPkgId(packageId, PayPlatformEnum.ALIPAY);
        // 获取预支付链接
        String payUrl = this.createAlipayQrCode(order);
        // 组装数据
        PayOrderInfoResVo result = new PayOrderInfoResVo();
        result.setBase64(QrCodeUtil.generateAsBase64(payUrl, QrConfig.create()
                        .setHeight(QRCODE_HEIGHT)
                        .setWidth(QRCODE_WIDTH)
                        .setImg(ALIPAY_LOGO),
                QRCODE_TYPE));
        result.setOrderId(order.getId());
        return result;
    }

    /**
     * 创建支付宝二维码
     *
     * @return
     */
    private String createAlipayQrCode(Order order) {
        String price = NumberUtil.toStr(NumberUtil.div(order.getPrice().intValue(), 100, 2, RoundingMode.DOWN));
        try {
            AlipayTradePrecreateResponse response = Factory.Payment.FaceToFace()
                    .preCreate(order.getDescription(), order.getId(), price);
            if (!ResponseChecker.success(response)) {
                throw new BusinessException(ResultEnum.ALIPAY_ERROR, response.msg + "," + response.subMsg);
            }
            return response.getQrCode();
        } catch (Exception exception) {
            throw new BusinessException(ResultEnum.ALIPAY_ERROR, exception.getMessage());
        }
    }

    /**
     * 支付宝成功回调
     *
     * @param orderId
     */
    @Transactional
    public void finishOrder(String orderId, String info) {
        // 查询订单，找到套餐包
        Order order = orderService.getById(orderId);
        order.setState(OrderStateEnum.PAY.getCode());
        order.setInfo(info);
        order.setUpdateTime(LocalDateTime.now());
        orderService.updateById(order);
        Package packageInfo = packageService.getById(order.getPkgId());
        // 绑定套餐包
        userPackageService.bindUser(packageInfo, order.getUserId(), null);
    }

    public String getOrderState(String orderId) {
        Order order = orderService.getById(orderId);
        if (ObjectUtil.isNull(order)) {
            throw new BusinessException(ResultEnum.DATA_EMPTY, "订单");
        }
        return order.getState();
    }

    /**
     * 微信支付预生成二维码
     */
    public PayOrderInfoResVo prePayWechatInfo(Long packageId) {
        // 创建订单
        Order order = orderService.createOrderByPkgId(packageId, PayPlatformEnum.WXPAY);
        // 创建支付链接
        String payUrl = this.createWxPayQrCode(order);
        // 组装参数
        PayOrderInfoResVo result = new PayOrderInfoResVo();
        result.setOrderId(order.getId());
        result.setBase64(QrCodeUtil.generateAsBase64(payUrl, QrConfig.create()
                        .setHeight(QRCODE_HEIGHT)
                        .setWidth(QRCODE_WIDTH)
                        .setImg(WXPAY_LOGO),
                QRCODE_TYPE));
        return result;
    }

    private String createWxPayQrCode(Order order) {
        // 构建service
        NativePayService service = new NativePayService.Builder().config(wxPayCaConfig).build();
        // request.setXxx(val)设置所需参数，具体参数可见Request定义
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(order.getPrice());
        request.setAmount(amount);
        request.setDescription(order.getDescription());
        request.setAppid(wxPayConfig.getAppId());
        request.setMchid(wxPayConfig.getMchId());
        request.setDescription(wxPayConfig.getDescription());
        request.setNotifyUrl(wxPayConfig.getNotifyUrl());
        request.setOutTradeNo(order.getId());
        // 调用下单方法，得到应答
        PrepayResponse response = service.prepay(request);
        // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
        return response.getCodeUrl();
    }

    /**
     * 解析微信支付回调数据
     *
     * @param signature
     * @param serial
     * @param nonce
     * @param timestamp
     * @param body
     * @return
     */
    public Transaction getWxPayTransaction(String signature, String serial, String nonce, String timestamp, String body) {
        // 初始化 NotificationParser
        NotificationParser parser = new NotificationParser(wxPayCaConfig);
        // 构造 RequestParam
        RequestParam requestParam = new RequestParam.Builder()
                .serialNumber(serial)
                .nonce(nonce)
                .signature(signature)
                .timestamp(timestamp)
                .body(body)
                .build();
        try {
            return parser.parse(requestParam, Transaction.class);
        } catch (ValidationException e) {
            // 签名验证失败，返回 401 UNAUTHORIZED 状态码
            log.error("微信支付回调签名验证失败", e);
            throw new CallBackException(ResultEnum.FAIL, "微信支付回调签名验证失败");
        }

    }
}
