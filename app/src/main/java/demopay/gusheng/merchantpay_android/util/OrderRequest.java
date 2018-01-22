package demopay.gusheng.merchantpay_android.util;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by fby on 2017/6/14.
 */

public class OrderRequest {
//    status 状态  1：支付成功   8:退款成功 空：全部
    //    size 当前页面多少数据
    //    no   当前在第几页
    //    payType  支付方式  2002 支付宝 2001 微信 0002银联
//    %@?status=%@&size=%@&no=%@&payType=%@

    private static final String MAINURL = "http://139.198.3.34:8082/";
    private static final String LJJURL = "http://192.168.50.18:8082/";

    private static final String ORDER_URL = MAINURL + "api/pay/core/getAllPay";

    private static final String ORDERDETAIL_URL = MAINURL + "api/pay/core/getByTrxNo/";

    private static final String PAYRESOULT_URL = MAINURL + "api/pay/chargesWap";

    private static final String ShopCollection_URL = MAINURL + "api/generate/page/queryQrcodeCollections";

    private static final String TodayData_URL = MAINURL + "api/pay/core/queryReportSumDay/";

    private static final String CASHIERLIST_URL = MAINURL + "api/generate/list/queryRoleUser?roleId=f75fa1f4d9be4813a0699f0a4c115da9";

    /*订单详情*/
    public static String getDetailJson(String trxNo, String token) throws IOException {

        String orderDetail_URL = ORDERDETAIL_URL+trxNo;
        Log.i("charge codeurl", orderDetail_URL);

        okhttp3.Request request = new okhttp3.Request.Builder().url(orderDetail_URL).get().header("token", token).build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }
    /*退款接口*/
    public static String getPayResoultJson(String merchantNo, String trxNo, String token) throws IOException {

        String orderDetail_URL = PAYRESOULT_URL+"?merchantNo="+merchantNo+"&trxNo="+trxNo;
        Log.i("charge codeurl", orderDetail_URL);

        okhttp3.Request request = new okhttp3.Request.Builder().url(orderDetail_URL).get().header("token", token).build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    /*get请求*/
    public static String getJson(String url, String token) throws IOException {

        okhttp3.Request request = new okhttp3.Request.Builder().url(url).get().header("token", token).build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }



    /*今日数据*/
    public static String getTodayDataJson(String merchantNo, String token) throws IOException {

        String todayData_URL = TodayData_URL+merchantNo;
        Log.i("charge codeurl", todayData_URL);

        okhttp3.Request request = new okhttp3.Request.Builder().url(todayData_URL).get().header("token", token).build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    public static String ShopCollectionJson(String url, String token, String merchantNo) throws IOException {

        String codeUrl = url+"?rownum="+1+"&merchantNo="+merchantNo;

        Log.i("charge codeurl", codeUrl);

        okhttp3.Request request = new okhttp3.Request.Builder().url(codeUrl).get().header("token", token).build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }


    /*订单列表*/
    public static String orderURL(String status, String size, String no, String payType, String token) throws IOException {

        String order_URL = ORDER_URL+"?status="+status+"&size="+size+"&no="+no+"&payType="+payType;

        return getJson(order_URL, token);
    }

    public static String orderdetailURL(String url, String trxNo) {

        String order_URL = url+trxNo;

        return order_URL;
    }

    /*收银员列表*/
    public static String getCashierJson(String merchantNo, String token) throws IOException {

        String Cashier_URL = CASHIERLIST_URL+"&merchantNo="+merchantNo;

        okhttp3.Request request = new okhttp3.Request.Builder().url(Cashier_URL).get().header("token", token).build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }





}
