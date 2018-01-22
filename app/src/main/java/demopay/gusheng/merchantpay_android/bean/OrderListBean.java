package demopay.gusheng.merchantpay_android.bean;

/**
 * Created by fby on 2017/6/14.
 */

public class OrderListBean {

    private ResultBean result;

    private String respCode;
    private String message;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public static class ResultBean {

//名称
        private String[] productName;
//类型
        private String[] payWayCode;
//        价格
        private String[] amount;
//        时间
        private String[] createTime;
//        平台流水号
        private String[] trxNo;
//        订单状态
        private String[] status;

        public String[] getProductName() {
            return productName;
        }

        public void setProductName(String[] productName) {
            this.productName = productName;
        }

        public String[] getPayWayCode() {
            return payWayCode;
        }

        public void setPayWayCode(String[] payWayCode) {
            this.payWayCode = payWayCode;
        }

        public String[] getAmount() {
            return amount;
        }

        public void setAmount(String[] amount) {
            this.amount = amount;
        }

        public String[] getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String[] createTime) {
            this.createTime = createTime;
        }

        public String[] getTrxNo() {
            return trxNo;
        }

        public void setTrxNo(String[] trxNo) {
            this.trxNo = trxNo;
        }

        public String[] getStatus() {
            return status;
        }

        public void setStatus(String[] status) {
            this.status = status;
        }
    }
}
