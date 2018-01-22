package demopay.gusheng.merchantpay_android.bean;

/**
 * Created by fby on 2017/6/19.
 */

public class RefundBean {

    private String trxNo;
    private String outRefundNo;
    private String amount;

    public String getTrxNo() {
        return trxNo;
    }

    public void setTrxNo(String trxNo) {
        this.trxNo = trxNo;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
