package demopay.gusheng.merchantpay_android.bean;

/**
 * Created by fby on 2017/6/16.
 */

public class OrderNumBean {

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

        private String payAmount;
        private String payNum;
        private String refundAmount;
        private String refundNum;

        public String getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(String payAmount) {
            this.payAmount = payAmount;
        }

        public String getPayNum() {
            return payNum;
        }

        public void setPayNum(String payNum) {
            this.payNum = payNum;
        }

        public String getRefundAmount() {
            return refundAmount;
        }

        public void setRefundAmount(String refundAmount) {
            this.refundAmount = refundAmount;
        }

        public String getRefundNum() {
            return refundNum;
        }

        public void setRefundNum(String refundNum) {
            this.refundNum = refundNum;
        }
    }
}
