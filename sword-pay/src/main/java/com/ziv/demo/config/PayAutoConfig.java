package com.ziv.demo.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.binarywang.spring.starter.wxjava.pay.properties.WxPayProperties;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 支付自动配置
 *
 * @author ziv
 * @date 2019-06-20
 */
@Configuration
@EnableConfigurationProperties({WxPayProperties.class, AlipayProperties.class})
@ConditionalOnClass(WxPayService.class)
@ConditionalOnProperty(prefix = "wx.pay", value = "enabled", matchIfMissing = true)
public class PayAutoConfig {
    @Resource
    private AlipayProperties alipayProperties;

    @Resource
    private WxPayProperties wxPayProperties;

    @Bean
    public AlipayClient getAliPayClient() {
        System.err.println("初始化支付客户端");
        // 初始化AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(
                this.alipayProperties.getGatewayUrl(),
                this.alipayProperties.getAppId(),
                this.alipayProperties.getMerchantPrivateKey(),
                this.alipayProperties.getFormat(),
                this.alipayProperties.getCharset(),
                this.alipayProperties.getAlipayPublicKey(),
                this.alipayProperties.getSignType()
        );
        System.err.println("初始化支付客户端完成");
        System.err.println(this.alipayProperties);
        return alipayClient;
    }

    /**
     * 构造微信支付服务对象
     * @return 微信支付service
     */
    @Bean
    @ConditionalOnMissingBean(WxPayService.class)
    public WxPayService wxPayService() {
        final WxPayServiceImpl wxPayService = new WxPayServiceImpl();
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(StringUtils.trimToNull(this.wxPayProperties.getAppId()));
        payConfig.setMchId(StringUtils.trimToNull(this.wxPayProperties.getMchId()));
        payConfig.setMchKey(StringUtils.trimToNull(this.wxPayProperties.getMchKey()));
        payConfig.setSubAppId(StringUtils.trimToNull(this.wxPayProperties.getSubAppId()));
        payConfig.setSubMchId(StringUtils.trimToNull(this.wxPayProperties.getSubMchId()));
        payConfig.setKeyPath(StringUtils.trimToNull(this.wxPayProperties.getKeyPath()));
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }
}
