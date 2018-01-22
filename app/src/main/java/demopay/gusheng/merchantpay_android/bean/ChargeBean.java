package demopay.gusheng.merchantpay_android.bean;

/**
 * 作者：王海洋
 * 时间：2017/3/10 18:39
 */

public class ChargeBean {

    /**
     * ResBody : {"respCode":"0000","respMsg":"成功","credential":"https://openapi.alipay.com/gateway.do?sign=P%2Fn%2FrtwXAoXl9VibhrDHsIBh8O54ipqbtoCbHUXdU0DC0qRvkzXs90%2BnMbMK3LnW6JaERp4NMqnA7V6SyvepgR978nMGyyWe4mBcjAEfD%2Blk7UJVHfVQVkLm6Z%2FwwTAi5lSrr02uge1Xbc%2B21e3yaa5jVYVNfZi7asLq1QR8JFw%3D&biz_content=%7B%22out_trade_no%22%3A%221%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%2211%22%2C%22total_amount%22%3A%220.03%22%7D&timestamp=2017-03-10+18%3A34%3A09&sign_type=RSA&notify_url=http%3A%2F%2Fagree.tunnel.qydev.com%2Fagree-pay-web-gateway%2FbackRcv%2F%2F20170310183409204315930%2Fnotice.do&charset=utf-8&method=alipay.trade.app.pay&app_id=2017030706087735&format=JSON&version=1.0"}
     * ResHeader : {"ErrorMsg":"交易成功","ErrorCode":"000000","RespDate":"2017-03-10 10:34:09"}
     *
     * {"result":{"credential":"sign=igdhMSMJMgsezm%2BkDTNLcd%2Ftri2ZPgZ5TMFVA5sD%2Fsj4jBtBOTPgjgwWJDAM8gkf3dp7GEJjYlCuN0s6CFQQdS43fyxqCXub%2B%2F%2BNg6KfWKhqnY%2FSww%2FV1nX%2B9pUoZvc%2F6V3jCryazcBQPlnOPLq%2FzJhknzC5M5Q4dLXKDhsXJnI%3D&charset=UTF-8&biz_content=%7B%22out_trade_no%22%3A%221495433227%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E6%94%AF%E4%BB%98%E6%B5%8B%E8%AF%95-1495433227%22%2C%22timeout_express%22%3A%2210m%22%2C%22total_amount%22%3A%220.01%22%7D&method=alipay.trade.app.pay&format=JSON&notify_url=http%3A%2F%2F139.198.3.34%3A8080%2Fagree-pay-web-gateway%2Fapi%2Fv1%2Fnotice%2F20170522140710484870351&version=1.0&app_id=2017030706087735&sign_type=RSA&timestamp=2017-05-22+14%3A07%3A10"
     * ,"TrxNo":"20170522140710484870351"}
     ,"message":"成功"
     ,"respCode":"000000"}
     */

    private ResultBean result;

    private String code;
    private String msg;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class ResultBean {
        /**
         * respCode : 0000
         * respMsg : 成功
         * credential : https://openapi.alipay.com/gateway.do?sign=P%2Fn%2FrtwXAoXl9VibhrDHsIBh8O54ipqbtoCbHUXdU0DC0qRvkzXs90%2BnMbMK3LnW6JaERp4NMqnA7V6SyvepgR978nMGyyWe4mBcjAEfD%2Blk7UJVHfVQVkLm6Z%2FwwTAi5lSrr02uge1Xbc%2B21e3yaa5jVYVNfZi7asLq1QR8JFw%3D&biz_content=%7B%22out_trade_no%22%3A%221%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%2211%22%2C%22total_amount%22%3A%220.03%22%7D&timestamp=2017-03-10+18%3A34%3A09&sign_type=RSA&notify_url=http%3A%2F%2Fagree.tunnel.qydev.com%2Fagree-pay-web-gateway%2FbackRcv%2F%2F20170310183409204315930%2Fnotice.do&charset=utf-8&method=alipay.trade.app.pay&app_id=2017030706087735&format=JSON&version=1.0

         */

        private String orderAmount;
        private String orderTime;

        private String trxNo;
        private String payType;
        private String payChannel;
        private String merchantOrderNo;
        private String productName;
        private String status;
        private String merchantNo;

        private String credential;


        private String bankTrxNo;
        private String merchantName;

        public String getTrxNo() {
            return trxNo;
        }

        public void setTrxNo(String trxNo) {
            this.trxNo = trxNo;
        }

        public String getBankTrxNo() {
            return bankTrxNo;
        }

        public void setBankTrxNo(String bankTrxNo) {
            this.bankTrxNo = bankTrxNo;
        }

        public String getMerchantName() {
            return merchantName;
        }

        public void setMerchantName(String merchantName) {
            this.merchantName = merchantName;
        }

        public String getCredential() {
            return credential;
        }

        public void setCredential(String credential) {
            this.credential = credential;
        }

        public String getOrderAmount() {
            return orderAmount;
        }

        public void setOrderAmount(String orderAmount) {
            this.orderAmount = orderAmount;
        }

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
        }


        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }

        public String getPayChannel() {
            return payChannel;
        }

        public void setPayChannel(String payChannel) {
            this.payChannel = payChannel;
        }

        public String getMerchantOrderNo() {
            return merchantOrderNo;
        }

        public void setMerchantOrderNo(String merchantOrderNo) {
            this.merchantOrderNo = merchantOrderNo;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMerchantNo() {
            return merchantNo;
        }

        public void setMerchantNo(String merchantNo) {
            this.merchantNo = merchantNo;
        }
    }
}
