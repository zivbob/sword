package entity;

/**
 * 支付宝支付状态类
 *
 * @author ziv
 * @date 2019-06-18
 */
public class AliPayStatus {

    /**
     * 交易创建，等待买家付款
     */
    public final static String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";

    /**
     * 未付款交易超时关闭，或支付完成后全额退款
     */
    public final static String TRADE_CLOSED = "TRADE_CLOSED";

    /**
     * 交易支付成功
     */
    public final static String TRADE_SUCCESS = "TRADE_SUCCESS";

    /**
     * 交易结束，不可退款
     */
    public final static String TRADE_FINISHED = "TRADE_FINISHED";
}
