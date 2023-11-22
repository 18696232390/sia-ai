package org.sia.controller.client;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wechat.pay.java.service.payments.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sia.service.PayService;
import org.sia.vo.R;
import org.sia.vo.request.WxPayNotifyReqVo;
import org.sia.vo.response.PayOrderInfoResVo;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/9 21:03
 */
@Slf4j
@RequestMapping("v1/pay")
@RestController
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    /**
     * 支付宝预支付订单二维码
     *
     * @return
     */
    @GetMapping("/alipay/qrcode")
    public R<PayOrderInfoResVo> getAlipayQrcode(@RequestParam Long packageId) {
        return R.success(payService.prePayAlipayInfo(packageId));
    }

    /**
     * 微信预支付订单二维码
     *
     * @return
     */
    @GetMapping("/wechat/qrcode")
    public R<JSONObject> getWechatQrcode(@RequestParam Long packageId) {
        return R.success(payService.prePayWechatInfo(packageId));
    }


    /**
     * 支付宝异步通知
     *
     * @return
     * @throws IOException
     */
    @PostMapping("/alipay/notify")
    public String notifyAliPay(
            // 支付宝交易号，支付宝交易凭证号。
            // 2013112011001004330000121536
            @RequestParam("trade_no") String tradeNo,
            // 商家订单号。原支付请求的商家订单号
            // 6823789339978248
            @RequestParam("out_trade_no") String outTradeNo,
            // 买家支付宝账号 ID。以 2088 开头的纯 16 位数字
            // 20881***524333
            @RequestParam("buyer_id") String buyerId,
            // 交易状态。交易目前所处状态，详情可查看下表 交易状态说明
            // TRADE_CLOSED
            @RequestParam("trade_status") String tradeStatus,
            // 商品描述。该订单的备注、描述、明细等。对应请求时的 body 参数，会在通知中原样传回
            // XXX交易内容
            @RequestParam(value = "body",required = false) String body,
            // 实收金额。商家在交易中实际收到的款项，单位为人民币（元），精确到小数点后 2 位
            // 20.00
            @RequestParam("receipt_amount") BigDecimal receiptAmount,
            // 订单标题/商品标题/交易标题/订单关键字等，是请求时对应参数，会在通知中原样传回
            @RequestParam("subject") String subject

    ) {
        log.info(">>> 支付宝回调 <<<");
        log.info(">>> 支付宝交易号:{}", tradeNo);
        log.info(">>> 系统订单号:{}", outTradeNo);
        log.info(">>> 买家支付宝账号ID:{}", buyerId);
        log.info(">>> 订单状态:{}", tradeStatus);
        log.info(">>> 实收金额:{}元", receiptAmount);
        log.info(">>> 订单标题:{}", subject);
        log.info(">>> 商品描述:{}", body);
        if ("TRADE_SUCCESS".equals(tradeStatus)) {
            payService.finishOrder(outTradeNo, JSONUtil.createObj()
                    .set("subject", subject)
                    .set("tradeNo", tradeNo)
                    .set("body", body)
                    .set("buyerId", buyerId)
                    .toString());
        }
        return "success";
    }


    /**
     * 微信支付异步通知
     *
     * @return
     * @throws IOException
     */
    @PostMapping("/wechat/notify")
    public String notifyWxPay(
            @RequestHeader("Wechatpay-Signature") String signature,
            @RequestHeader("Wechatpay-Serial") String serial,
            @RequestHeader("Wechatpay-Nonce") String nonce,
            @RequestHeader("Wechatpay-Timestamp") String timestamp,
            @RequestBody String body) {
        log.info(">>> 微信支付回调 <<<");
        log.info(">>> 签名:{}", signature);
        log.info(">>> BODY数据:{}", body);
        if ("TRANSACTION.SUCCESS".equals(  JSONUtil.parseObj(body).getStr("event_type"))) {
            Transaction transaction = payService.getWxPayTransaction(signature, serial, nonce, timestamp, body);
            payService.finishOrder(transaction.getOutTradeNo(), JSONUtil.createObj()
                    .set("subject", "-")
                    .set("tradeNo", transaction.getTransactionId())
                    .set("body", "-")
                    .set("buyerId", transaction.getPayer().getOpenid())
                    .set("data", transaction)
                    .toString());
        }
        return "success";
    }

    /**
     * 获取订单支付状态
     *
     * @param orderId
     * @return
     */
    @GetMapping("/order/state")
    public R<String> getOrderState(@RequestParam String orderId) {
        return R.success(payService.getOrderState(orderId));
    }


}
