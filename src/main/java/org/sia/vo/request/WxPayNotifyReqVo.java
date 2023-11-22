package org.sia.vo.request;

import cn.hutool.json.JSONObject;
import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/16 14:14
 */
@Data
public class WxPayNotifyReqVo {
    // 通知ID:EV-2018022511223320873
    private String id;
    // 通知创建时间:2015-05-20T13:29:35+08:00
    private String create_time;
    // 通知类型:TRANSACTION.SUCCESS
    private String event_type;
    // 通知数据类型:encrypt-resource
    private String resource_type;
    // 通知数据:json
    private JSONObject resource;
    // 回调摘要:支付成功
    private String summary;
}
