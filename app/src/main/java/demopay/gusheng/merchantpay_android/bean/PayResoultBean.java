package demopay.gusheng.merchantpay_android.bean;

/**
 * Created by fby on 2017/11/8.
 */

public class PayResoultBean {

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

        private String TrxNo;

        private String credential;

        public String getTrxNo() {
            return TrxNo;
        }

        public void setTrxNo(String trxNo) {
            TrxNo = trxNo;
        }

        public String getCredential() {
            return credential;
        }

        public void setCredential(String credential) {
            this.credential = credential;
        }
    }

}
