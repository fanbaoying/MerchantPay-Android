package demopay.gusheng.merchantpay_android.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.unionpay.cloudpos.DeviceException;
import com.unionpay.cloudpos.POSTerminal;
import com.unionpay.cloudpos.printer.Format;
import com.unionpay.cloudpos.printer.PrinterDevice;
import com.wizarpos.paymentrouter.aidl.IWizarPayment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;

import demopay.gusheng.merchantpay_android.InputFilter.CashierInputFilter;
import demopay.gusheng.merchantpay_android.R;
import demopay.gusheng.merchantpay_android.activity.cashier.AddCashierActivity;
import demopay.gusheng.merchantpay_android.activity.cashier.CashierActivity;
import demopay.gusheng.merchantpay_android.adapter.ServiceObject;
import demopay.gusheng.merchantpay_android.bean.BankCardBean;
import demopay.gusheng.merchantpay_android.bean.ChargeBean;
import demopay.gusheng.merchantpay_android.bean.JudgeBean;
import demopay.gusheng.merchantpay_android.bean.OrderDetailBean;
import demopay.gusheng.merchantpay_android.bean.PaymentRequest;
import demopay.gusheng.merchantpay_android.util.Constant;

import demopay.gusheng.merchantpay_android.util.Request;
import demopay.gusheng.merchantpay_android.util.ToastUtil;
import demopay.gusheng.merchantpay_android.zxing.sweepactivity.CaptureActivity;

import static demopay.gusheng.merchantpay_android.util.OrderRequest.getDetailJson;
import static demopay.gusheng.merchantpay_android.util.OrderRequest.getPayResoultJson;


/**
 * Created by fby on 2017/9/5.
 */

public class PayMoneyActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnQrCode; // 扫码
    TextView tvResult; // 结果

    TextView moneyEditText;//金额
    private int paymoney;

    private String orderNo;
    private int amount;


    public String payChaneel = null;

    /**
     * 微信
     */
    private static final String CHANNEL_WECHAT = "WEIXIN";

    /**
     * 支付宝
     */
    private static final String CHANNEL_ALIPAY = "ALI";


    private  String token;
    private  String merchantNo;

    private ImageButton backImageButton;

    private static final String fileName = "logintext";//定义保存的文件的名称


    private PrinterDevice printerDevice;
    private Format format;

    private String payChannelStr;


    private int i = 0;

    private String SignOnStr;

//    平台流水号
    private String trxNoStr;

//    银行卡收款
//    private IWizarPayment wizarPayment = null;

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            if (msg.what == 1) {

                ToastUtil.showShort(PayMoneyActivity.this, "打印设备关闭成功");

                Intent mIntent = new Intent(PayMoneyActivity.this, PayOrderActivity.class);
                mIntent.putExtra("flag", true);
//                    saveToDatabase();
                startActivity(mIntent);

            }else if (msg.what == 2){

                ToastUtil.showShort(PayMoneyActivity.this, "打印设备关闭失败");
            }else if (msg.what == 8) {

                ChargeBean chargeBean = (ChargeBean) msg.obj;
                String status = chargeBean.getResult().getStatus();

                if (status.equals("SUCCESS")) {

                    //                关闭定时器
                    selfHandler.removeCallbacks(runnable);

                    ToastUtil.showShort(PayMoneyActivity.this, "收款成功");

                    String merchantName = chargeBean.getResult().getMerchantName();
                    Log.i("charge response-json", merchantName);
                    String merchantNo = chargeBean.getResult().getMerchantNo();
                    Log.i("charge response-json", merchantNo);

                    String payChannel = chargeBean.getResult().getPayChannel();
                    Log.i("charge response-json", payChannel);

                    if (payChannel.equals("WEIXIN")) {

                        payChannelStr = "微信支付";
                    }else {

                        payChannelStr = " ";
                    }

                    String orderTime = chargeBean.getResult().getOrderTime();
                    Log.i("charge response-json", orderTime);

                    String orderAmount = chargeBean.getResult().getOrderAmount();
                    Log.i("charge response-json", orderAmount);


                    Double amount = Double.valueOf(orderAmount)/100;

                    String orderAmountStr = String.valueOf(amount);

//                    开启打印机
//                    starPrint(merchantName, merchantNo, payChannelStr, orderTime, orderAmountStr);

                    Intent mIntent = new Intent(PayMoneyActivity.this, PayOrderActivity.class);
                    mIntent.putExtra("flag", true);
//                    saveToDatabase();
                    startActivity(mIntent);

                }

            }else if (msg.what == 81) {
                //                关闭定时器
                selfHandler.removeCallbacks(runnable);

                JudgeBean judgeBean = (JudgeBean) msg.obj;
                String message = judgeBean.getMsg();
                ToastUtil.showShort(PayMoneyActivity.this, message);
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymoneyactivity_first);

//        打印机
//        printerDevice = (PrinterDevice) POSTerminal.getInstance(getApplicationContext()).getDevice(
//                "cloudpos.device.printer");

        SharedPreferences share = super.getSharedPreferences(fileName,
                MODE_PRIVATE);

        token = share.getString("token", null);
        merchantNo = share.getString("merchantNo", null);

        Log.i("charge token", token);


        backImageButton = (ImageButton) findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        moneyEditText = (TextView) findViewById(R.id.moneyEditText);
        InputFilter[] filters = {new CashierInputFilter()};
        moneyEditText.setFilters(filters);

        initView();
    }

    private void initView() {

        btnQrCode = (Button) findViewById(R.id.wechatbutton);
        btnQrCode.setOnClickListener(this);

        btnQrCode = (Button) findViewById(R.id.bankcardbutton);
        btnQrCode.setOnClickListener(this);

        JSONObject param = new JSONObject();
        String ret = "ok";

        if(ServiceObject.getInstance().IWizarPayment == null) {
            Log.i("charge data cardButton", "未连接服务");
        }else {

            try
            {

                Log.i("charge data", "查询走了");


                param.put("AppID", "");
                param.put("AppName", "");

                param.put("ProtocolVersion", "201701");
//                            param.put("TransType", 500);
                param.put("ReqTransDate", "20171106");
                ret = ServiceObject.getInstance().IWizarPayment.getPOSInfo(param.toString());
                Log.i("charge data find", "result:"+ret);

//            ToastUtil.showShort(PayMoneyActivity.this, "查询成功");

                BankCardBean bankCardBean = new Gson().fromJson(ret, BankCardBean.class);
                String SignOn = bankCardBean.getSignOn();

                Log.i("charge data find", SignOn);

                SignOnStr = SignOn;

            }

            catch (JSONException e)
            {
                e.printStackTrace();
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }

        }



    }

    // 开始扫码
    private void startQrCode(String typeButton) {

        if (typeButton.equals("0002")) {

//            new Thread(new ThreadShow()).start();

            if (moneyEditText.getText().toString().equals("")){

                Toast.makeText(PayMoneyActivity.this, "请输入收款金额", Toast.LENGTH_SHORT).show();
                return;

            }else if (SignOnStr.equals("0")){

                Toast.makeText(PayMoneyActivity.this, "请先做签到", Toast.LENGTH_SHORT).show();
                return;

            }else {

//                Intent mIntent = new Intent(PayMoneyActivity.this, BankCardActivity.class);
//
//                mIntent.putExtra("payMoney", moneyEditText.getText().toString());
//
//                startActivity(mIntent);

                JSONObject param = new JSONObject();
                String ret = "ok";
                if(ServiceObject.getInstance().IWizarPayment == null) {
                    Log.i("charge data cardButton", "未连接服务");
                }else {

                    try
                    {

                        Log.i("charge data", "判断走了");


                        param.put("AppID", "");
                        param.put("AppName", "");

                        String urlmoney = changeY2F(Double.valueOf(moneyEditText.getText().toString()));
                        int paymoneys = Integer.parseInt(urlmoney);

                        param.put("TransAmount", String.valueOf(paymoneys));
                        param.put("TransType", 1);
                        param.put("ExternTransFlag", "1");
                        ret = ServiceObject.getInstance().IWizarPayment.payCash(param.toString());
                        Log.i("charge data", "result:"+ret);

                    }

                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    catch (RemoteException e)
                    {
                        e.printStackTrace();
                    }
                }

            }

        }else {

//            new Thread(new ThreadShow()).stop();

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // 申请权限
                ActivityCompat.requestPermissions(PayMoneyActivity.this, new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
                return;
            }

            if (moneyEditText.getText().toString().equals("")){

                Toast.makeText(PayMoneyActivity.this, "请输入收款金额", Toast.LENGTH_SHORT).show();
                return;

            }else {

//            bignum1 = new BigDecimal(moneyEditText.getText().toString());

                // 二维码扫码
            Intent intent = new Intent(PayMoneyActivity.this, CaptureActivity.class);

            intent.putExtra("payMoney", moneyEditText.getText().toString());

            startActivityForResult(intent, Constant.REQ_QR_CODE);



            }

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wechatbutton:
                startQrCode("2001");
                payChaneel = "WEIXIN";
                break;
            case R.id.bankcardbutton:
                startQrCode("0002");
                payChaneel = "ALI";
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == Constant.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            //将扫描出的信息显示出来

            Log.i("change qgcode", scanResult);



            String urlmoney = changeY2F(Double.valueOf(moneyEditText.getText().toString()));

            Log.i("change qgcode2", urlmoney);

            int paymoneys = Integer.parseInt(urlmoney);

            Log.i("change qgcode1", String.valueOf(paymoneys));



            new PaymentTask().execute(new PaymentRequest(payChaneel, paymoneys, scanResult));

        }
    }

    public static String changeY2F(Double amount){
        return BigDecimal.valueOf(amount).multiply(new BigDecimal(100)).setScale(0).toString();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.REQ_PERM_CAMERA:
                // 摄像头权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.i("charge quanxian", "已走");
                    // 获得授权
                    startQrCode("");
                } else {

                    Log.i("charge jinzhi ", "已走");
                    // 被禁止授权
                    Toast.makeText(PayMoneyActivity.this, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    private class PaymentTask extends AsyncTask<PaymentRequest, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("charge response-json", "hahah");
        }

        @Override
        protected String doInBackground(PaymentRequest... pr) {
            PaymentRequest paymentRequest = pr[0];
            String data = null;



            orderNo = getTime();
            String channel = paymentRequest.getChannel();
            amount = paymentRequest.getAmount();


            Log.i("charge amount", String.valueOf(amount));

            String openId = paymentRequest.getScanResult();

            try {
                data = Request.pay(orderNo, amount, channel, openId, 1, token);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(String data) {

            Log.i("charge response-json", data);

            JudgeBean judgeBean = new Gson().fromJson(data, JudgeBean.class);
            String respCode = judgeBean.getCode();
            String message = judgeBean.getMsg();
            if (respCode.equals("0")){


                ChargeBean chargeBean = new Gson().fromJson(data, ChargeBean.class);
                String status = chargeBean.getResult().getStatus();

                if (status.equals("SUCCESS")) {

                    ToastUtil.showShort(PayMoneyActivity.this, "收款成功");

                    String merchantName = chargeBean.getResult().getMerchantName();
                    Log.i("charge response-json", merchantName);
                    String merchantNo = chargeBean.getResult().getMerchantNo();
                    Log.i("charge response-json", merchantNo);

                    String payChannel = chargeBean.getResult().getPayChannel();
                    Log.i("charge response-json", payChannel);

                    if (payChannel.equals("WEIXIN")) {

                        payChannelStr = "微信支付";
                    }else {

                        payChannelStr = " ";
                    }

                    String orderTime = chargeBean.getResult().getOrderTime();
                    Log.i("charge response-json", orderTime);

                    String orderAmount = chargeBean.getResult().getOrderAmount();
                    Log.i("charge response-json", orderAmount);


                    Double amount = Double.valueOf(orderAmount)/100;

                    String orderAmountStr = String.valueOf(amount);

//                    开启打印机
//                    starPrint(merchantName, merchantNo, payChannelStr, orderTime, orderAmountStr);

//                    关闭打印机

//                    stopPrint();

                }else if(status.equals("2")) {

                    trxNoStr = chargeBean.getResult().getTrxNo();

                    //                开始定时器
                    selfHandler.postDelayed(runnable, 20000);


                }

            }else {

                ToastUtil.showShort(PayMoneyActivity.this, message);
                return;
            }

        }
    }


//  打印机函数
    private void starPrint(final String merchantName, final String merchantNo, final String payChannel, final String orderTime, final String orderAmount) {
        // TODO Auto-generated method stub
        try {

            printerDevice.open();

//            ToastUtil.showShort(PayMoneyActivity.this, "打印机设备打开成功");

            format = new Format();
            try {
                if (printerDevice.queryStatus() == printerDevice.STATUS_OUT_OF_PAPER) {

                    ToastUtil.showShort(PayMoneyActivity.this, "打印机缺纸");
                } else if (printerDevice.queryStatus() == printerDevice.STATUS_PAPER_EXIST) {

                    ToastUtil.showShort(PayMoneyActivity.this, "打印机状态正常，开始打印");
                    Thread thread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            try {

                                format = new Format();
                                printerDevice.printText("\n");
                                format.setParameter("align", "center");
                                format.setParameter("bold", "true");
                                format.setParameter("size", "medium");
                                printerDevice.printText(format, "银联POS签购单");
                                printerDevice.printText("\n");
                                printerDevice.printText("\n");

                                format.clear();
                                format.setParameter("align", "left");
                                format.setParameter("size", "small");
                                printerDevice.printText(format, "商户名称：" + merchantName + "\n");
                                printerDevice.printText(format, "商户编号：" + merchantNo + "\n");
                                printerDevice.printText(format, "终端编号：12345610\n");
                                printerDevice.printText(format, "操作员号：01\n");
                                printerDevice.printText(format, "交易类型：" + payChannel + "\n");
                                printerDevice.printText(format, "日期时间：" + orderTime + "\n");
                                printerDevice.printText(format, "金额：" + orderAmount + "元\n");
                                printerDevice.printlnText( "持卡人签名：");
                                printerDevice.printlnText("CARD HOLDER SIGNATURE");
                                printerDevice.printlnText("\n");
                                printerDevice.printlnText("本人确认以上交易，同意将其写入本卡账户");
                                printerDevice.printlnText(
                                        "I ACKNOWLEDGE SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICES");
                                printerDevice.printlnText("\n");
                                printerDevice.printlnText("\n");
                                //          关闭打印机
                                stopPrint();
                            } catch (DeviceException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                //          关闭打印机



                                stopPrint();

                            }

//                            关闭打印机

                            stopPrint();

                        }
                    });
                    thread.start();
                }
            } catch (DeviceException de) {
                ToastUtil.showShort(PayMoneyActivity.this, "查看打印机状态失败");
                de.printStackTrace();
                //          关闭打印机
                stopPrint();
            }
        } catch (DeviceException de) {
            de.printStackTrace();
            ToastUtil.showShort(PayMoneyActivity.this, "打印机设备打开失败");
            //          关闭打印机
            stopPrint();
        }

    }

//    停止打印
    private void stopPrint() {
        // TODO Auto-generated method stub
        try {
            printerDevice.close();

            Message msg = new Message();
            msg.what = 1;
            mHandler.sendMessage(msg);

        } catch (DeviceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            Message msg = new Message();
            msg.what = 1;
            mHandler.sendMessage(msg);
        }
    }

    private String getTime() {
        long time = System.currentTimeMillis() / 1000;

        return String.valueOf(time);
    }

//    定时器

    Handler selfHandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                selfHandler.postDelayed(this,20000);
                FindPayResult();


            }catch (Exception e){
                System.out.println("exception " + e);

            }
        }
    };

    private void FindPayResult () {

        Runnable orderRunnable = new Runnable() {
            @Override
            public void run() {
                String data = null;


                try {
                    data = getPayResoultJson(merchantNo,trxNoStr, token);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.i("charge response", data);

                JudgeBean judgeBean = new Gson().fromJson(data, JudgeBean.class);

                String respCode = judgeBean.getCode();

                if (respCode.equals("0")){

                    ChargeBean charge = new Gson().fromJson(data, ChargeBean.class);
                    Message msg = new Message();
                    msg.what = 8;
                    msg.obj = charge;
                    mHandler.sendMessage(msg);

                }else {

                    Message msg = new Message();
                    msg.what = 81;
                    msg.obj = judgeBean;
                    mHandler.sendMessage(msg);

                }
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(orderRunnable);
        payThread.start();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //                关闭定时器
        selfHandler.removeCallbacks(runnable);

    }


}
