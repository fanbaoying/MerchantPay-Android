package demopay.gusheng.merchantpay_android.bean;

public class PaymentRequest {
    private String channel;
    private  String scanResult;
    private int amount;

    public PaymentRequest(String channel, int amount, String scanResult) {
        this.channel = channel;
        this.amount = amount;
        this.scanResult = scanResult;
    }

    public String getScanResult() {
        return scanResult;
    }

    public String getChannel() {
        return channel;
    }

    public int getAmount() {
        return amount;
    }
}