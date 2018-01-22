package demopay.gusheng.merchantpay_android.util;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import demopay.gusheng.merchantpay_android.bean.CashierDataBean;
import demopay.gusheng.merchantpay_android.bean.LoginBean;
import demopay.gusheng.merchantpay_android.bean.PayBean;
import demopay.gusheng.merchantpay_android.bean.RefundBean;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Request {
    /**
     * 支付请求URL
     */

    private static final String MAINURL = "http://139.198.3.34:8082/";
    private static final String LJJURL = "http://192.168.50.18:8082/";

//    支付
//    private static final String CHARGE_URL = "http://pay.soongrande.com/agree-pay-web-gateway/scan/charges";

    private static final String CHARGE_URL = MAINURL + "api/pay/charges";
//    private static final String CHARGE_URL = LJJURL + "api/pay/charges";


//    退款
//    private static final String REFUND_URL = "http://pay.soongrande.com/agree-pay-web-gateway/api/v1/refund";
    private static final String REFUND_URL = MAINURL + "api/pay/refund";
//    登录
    private static final String LOGIN_URL = MAINURL + "api/login";
//    private static final String LOGIN_URL = "http://192.168.50.18:8082/api/login";

//    收银员修改接口
    private static final String CashierEdit_URL = MAINURL + "api/modifyUser";

//    收银员新增接口
    private static final String CashierAdd_URL = MAINURL + "api/register";


    public static String pay(String orderNo, int amount, String channel, String openId, int type, String token) throws IOException {
        PayBean payBean = new PayBean();

        payBean.setPayChannel(channel);
        Date now = new Date();
        String newTime = getNewTime(now);
        payBean.setOrderTime(newTime);
        payBean.setMerchantNo("12345678");


        payBean.setMerchantOrderNo(orderNo);

        payBean.setProductName("支付测试-" + orderNo);
        payBean.setAmount(String.valueOf(amount));
        payBean.setReturnUrl("");
        payBean.setNotifyUrl("");
        payBean.setOrderPeriod("10");
        payBean.setDesc("11");
        payBean.setTrxType("");
        payBean.setFundIntoType("");

        if (type == 1) {
            payBean.setOpenId(openId);
            payBean.setPayType("MICROPAY");
        }else {

            payBean.setPayType("NATIVE");
        }


        String json = new Gson().toJson(payBean);

        Log.i("charge json",json);
        Log.i("charge json",CHARGE_URL);

        return postJson(CHARGE_URL, json, token);
    }

    public static String refund(String trxNo, BigDecimal amount, String outRefundNo, String token) throws IOException {
        RefundBean refundBean = new RefundBean();

        refundBean.setTrxNo(trxNo);
        refundBean.setOutRefundNo(outRefundNo);
        refundBean.setAmount(String.valueOf(amount));

        String json = new Gson().toJson(refundBean);

        Log.i("charge json",json);

        return postJson(REFUND_URL, json, token);
    }



    public static String postJson(String url, String json, String token) throws IOException {
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, json);

        okhttp3.Request request = new okhttp3.Request.Builder().url(url).post(body).header("token", token).build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }


    public static String loginData(String userName, String password, String merchantNo, String type) throws  IOException {

        Log.i("charge login", LOGIN_URL);

        LoginBean loginBean = new LoginBean();

        if (type.equals("2")) {

            loginBean.setMobile(userName);
            loginBean.setPassword(password);
            loginBean.setMerchantNo(merchantNo);
            loginBean.setType(type);

        }else {
            loginBean.setUserName(userName);
            loginBean.setPassword(password);

        }

        String json = new Gson().toJson(loginBean);

        Log.i("charge login", json);

        return postJsonLogin(LOGIN_URL, json);
    }

    public static String postJsonLogin(String url, String json) throws IOException {
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, json);
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).post(body).build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
//        response.code();
//        response.body().contentLength();
//        Log.i("charge login", String.valueOf(response.code()));
//        Log.i("charge login", String.valueOf(response.body().contentLength()));

        return response.body().string();
    }


    private static String getNewTime(Date now) {
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sdfs.format(now);
    }

    /*收银员状态改变*/

    public static String putJsonCashier(CashierDataBean cashierDataBean, String token) throws IOException {

        String json = new Gson().toJson(cashierDataBean);

        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, json);

        okhttp3.Request request = new okhttp3.Request.Builder().url(CashierEdit_URL).put(body).header("token", token).build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    /*新增收银员*/
    public static String postJsonAddCashier(String json, String token) throws IOException {

        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, json);

        okhttp3.Request request = new okhttp3.Request.Builder().url(CashierAdd_URL).post(body).header("token", token).build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }



}
