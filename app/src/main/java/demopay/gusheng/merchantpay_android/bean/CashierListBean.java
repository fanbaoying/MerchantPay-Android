package demopay.gusheng.merchantpay_android.bean;

import java.util.List;

/**
 * Created by fby on 2017/11/16.
 */

public class CashierListBean {

    private List<CashierDataBean> data;
    private String code;
    private String msg;

    public List<CashierDataBean> getData() {
        return data;
    }

    public void setData(List<CashierDataBean> data) {
        this.data = data;
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

    /*{
        createTime = "2017-09-28 11:16:56";
        merchantNo = fa43d925693540fd90fba77ee8a33872;
        mobile = 8;
        realName = zhangsana;
        roleId = f75fa1f4d9be4813a0699f0a4c115da9;
        roleUserId = 870298589192467ab82bc1d14a90f7ed;
        userCode = 3214;
        userId = 52948f1e735d4b59b97cd81b13632a66;
        userStatus = 0;
        username = 8;
    }*/


}
