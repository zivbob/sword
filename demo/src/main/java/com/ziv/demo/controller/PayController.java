package com.ziv.demo.controller;

import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayNativeOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.ziv.demo.entity.AliPayOrder;
import com.ziv.demo.utils.RequestUtils;
import com.ziv.demo.config.AlipayProperties;
import entity.Result;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "pay")
public class PayController {

    @Resource
    private WxPayService wxPayService;

    @Resource
    private AlipayClient alipayClient;

    @Resource
    private AlipayProperties alipayProperties;

    @GetMapping("wxPay")
    public Result test (@RequestParam String tradeNo, HttpServletRequest request) {
        Result result;
        try {
            String ip = RequestUtils.getRealIp(request);
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            // 商品描述
            orderRequest.setBody("云服务-续费");
            // 订单编号
            orderRequest.setOutTradeNo(tradeNo);
            // 订单金额（分）
            orderRequest.setTotalFee(120000);
            // 用户IP地址
            orderRequest.setSpbillCreateIp(ip);
            // 通知地址
            orderRequest.setNotifyUrl("http://192.168.1.93:8080/wxPayTest/notify");
            // 交易类型
            orderRequest.setTradeType("NATIVE");
            // 商品Id
            orderRequest.setProductId("1");
            WxPayNativeOrderResult payResult = wxPayService.createOrder(orderRequest);
            result = Result.success(payResult);
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.createByErrorMessage(e.getMessage());
        }
        return result;
    }

    @PostMapping(value = "wxPay/notify")
    public String parseOrderNotifyResult(@RequestBody String xmlData) throws WxPayException {
        final WxPayOrderNotifyResult notifyResult = wxPayService.parseOrderNotifyResult(xmlData);
        // TODO 根据自己业务场景需要构造返回对象
        return WxPayNotifyResponse.success("成功");
    }

    @PostMapping(value = "aliPay")
    public Result test (@RequestParam String orderNo) {
        Result result;
        try {
            System.out.println(orderNo);
            //设置请求参数
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            alipayRequest.setReturnUrl(alipayProperties.getReturnUrl());
            alipayRequest.setNotifyUrl(alipayProperties.getNotifyUrl());

            AliPayOrder order = new AliPayOrder();
            order.setProduct_code("FAST_INSTANT_TRADE_PAY");
            order.setOut_trade_no(orderNo);
            order.setTotal_amount(new BigDecimal(12300));
            order.setSubject("会员充值");
            order.setBody("年度会员");

            alipayRequest.setBizContent(JSONUtil.toJsonStr(order));
            String resultStr = alipayClient.pageExecute(alipayRequest).getBody();

            // 处理支付html
            Document document = Jsoup.parse(resultStr);
            Elements js = document.getElementsByTag("script");
            Elements form = document.getElementsByTag("form");
            StringBuilder jsBuilder = new StringBuilder();
            StringBuilder formBuilder = new StringBuilder();
            js.forEach(param -> jsBuilder.append(param.html()));
            form.forEach(param -> formBuilder.append(param.toString()));
            Map map = new HashMap(2);

            map.put("form", formBuilder.toString());
            map.put("js", jsBuilder.toString());
            result = Result.success(map);

        } catch (Exception e) {
            e.printStackTrace();
            result = Result.createByErrorMessage(e.getMessage());
        }
        return result;
    }

    @PostMapping(value = "aliPay/notify")
    public void notifyResponse() {
        System.err.println("异步通知");
    }
}
