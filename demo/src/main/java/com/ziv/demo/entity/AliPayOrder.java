package com.ziv.demo.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 支付宝支付订单
 *
 * @author ziv
 * @date 2019-06-18
 */
@Data
public class AliPayOrder {
    /**
     * 商户订单号
     */
    private String out_trade_no;

    /**
     * 销售产品码
     */
    private String product_code;

    /**
     * 订单总金额
     */
    private BigDecimal total_amount;

    /**
     * 订单标题
     */
    private String subject;

    /**
     * 订单描述
     */
    private String body;
}
