package demopay.gusheng.merchantpay_android.bean;

import java.util.List;

/**
 * Created by fby on 2017/10/10.
 */
//HashMap
public class ShopCollectionDataBean {


    private String code;

    private List<DataBean> data;

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        private String qrHeight;
        private String qrWidth;

        private String styleNo;
        private String updateUser;

        private String qrcodeNo;
        private String updateTime;

        private String foregroundcolor;
        private String backgroundcolor;

        private String radioButton;
        private String qrcodeStatus;

        private String createTime;
        private String qrcodeContent;

        private String createUser;
        private String qrcodeformat;

        private String qrcodeName;
        private String merchantNo;

        public String getQrHeight() {
            return qrHeight;
        }

        public void setQrHeight(String qrHeight) {
            this.qrHeight = qrHeight;
        }

        public String getQrWidth() {
            return qrWidth;
        }

        public void setQrWidth(String qrWidth) {
            this.qrWidth = qrWidth;
        }

        public String getStyleNo() {
            return styleNo;
        }

        public void setStyleNo(String styleNo) {
            this.styleNo = styleNo;
        }

        public String getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(String updateUser) {
            this.updateUser = updateUser;
        }

        public String getQrcodeNo() {
            return qrcodeNo;
        }

        public void setQrcodeNo(String qrcodeNo) {
            this.qrcodeNo = qrcodeNo;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getForegroundcolor() {
            return foregroundcolor;
        }

        public void setForegroundcolor(String foregroundcolor) {
            this.foregroundcolor = foregroundcolor;
        }

        public String getBackgroundcolor() {
            return backgroundcolor;
        }

        public void setBackgroundcolor(String backgroundcolor) {
            this.backgroundcolor = backgroundcolor;
        }

        public String getRadioButton() {
            return radioButton;
        }

        public void setRadioButton(String radioButton) {
            this.radioButton = radioButton;
        }

        public String getQrcodeStatus() {
            return qrcodeStatus;
        }

        public void setQrcodeStatus(String qrcodeStatus) {
            this.qrcodeStatus = qrcodeStatus;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getQrcodeContent() {
            return qrcodeContent;
        }

        public void setQrcodeContent(String qrcodeContent) {
            this.qrcodeContent = qrcodeContent;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public String getQrcodeformat() {
            return qrcodeformat;
        }

        public void setQrcodeformat(String qrcodeformat) {
            this.qrcodeformat = qrcodeformat;
        }

        public String getQrcodeName() {
            return qrcodeName;
        }

        public void setQrcodeName(String qrcodeName) {
            this.qrcodeName = qrcodeName;
        }

        public String getMerchantNo() {
            return merchantNo;
        }

        public void setMerchantNo(String merchantNo) {
            this.merchantNo = merchantNo;
        }
    }

}
