package demopay.gusheng.merchantpay_android.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.unionpay.cloudpos.DeviceException;
import com.unionpay.cloudpos.printer.Format;
import com.unionpay.cloudpos.printer.PrinterDevice;

import java.io.IOException;
import java.math.BigDecimal;

import demopay.gusheng.merchantpay_android.R;
import demopay.gusheng.merchantpay_android.bean.ChargeBean;
import demopay.gusheng.merchantpay_android.bean.JudgeBean;
import demopay.gusheng.merchantpay_android.bean.OrderDetailBean;
import demopay.gusheng.merchantpay_android.bean.PayResoultBean;
import demopay.gusheng.merchantpay_android.bean.PaymentRequest;
import demopay.gusheng.merchantpay_android.util.Request;
import demopay.gusheng.merchantpay_android.util.ToastUtil;
import demopay.gusheng.merchantpay_android.zxing.encoding.EncodingHandler;

import static demopay.gusheng.merchantpay_android.util.OrderRequest.getDetailJson;
import static demopay.gusheng.merchantpay_android.util.OrderRequest.getPayResoultJson;

/**
 * Created by fby on 2017/9/6.
 */

public class SweepActivity extends AppCompatActivity {

    ImageView QrCode;
    Button changbutton;
    ImageButton backbutton;

    TextView merchantPayMoney;

    private String data;

    private String orderNo;
    private int amount;

    private  String token;
    private  String merchantNo;


    private static final String fileName = "logintext";//定义保存的文件的名称


    //    平台流水号
    private String trxNoStr;

//    打印机
    private PrinterDevice printerDevice;
    private Format format;

    private String payChannelStr;

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            if (msg.what == 1) {

//                ToastUtil.showShort(SweepActivity.this, "打印设备关闭成功");

                Intent mIntent = new Intent(SweepActivity.this, PayOrderActivity.class);
                mIntent.putExtra("flag", true);
//                    saveToDatabase();
                startActivity(mIntent);

            }else if (msg.what == 2){

//                ToastUtil.showShort(SweepActivity.this, "打印设备关闭失败");
            }else if (msg.what == 8) {

                OrderDetailBean orderDetailBean = (OrderDetailBean) msg.obj;
                String status = orderDetailBean.getResult().getStatus();

                if (status.equals("1")) {

                    //                关闭定时器
                    selfHandler.removeCallbacks(runnable);

//                    ToastUtil.showShort(SweepActivity.this, "收款成功");

                    String merchantName = orderDetailBean.getResult().getMerchantName();
                    Log.i("charge response-json", merchantName);
                    String merchantNoStr = orderDetailBean.getResult().getMerchantNo();
                    Log.i("charge response-json", merchantNoStr);

                    String payChannel = orderDetailBean.getResult().getPayChannel();
                    Log.i("charge response-json", payChannel);

                    if (payChannel.equals("WEIXIN")) {

                        payChannelStr = "微信支付";
                    }else {

                        payChannelStr = " ";
                    }

                    String completeTime = orderDetailBean.getResult().getCompleteTime();
                    Log.i("charge response-json", completeTime);

                    String orderAmount = orderDetailBean.getResult().getAmount();
                    Log.i("charge response-json", orderAmount);


                    Double amount = Double.valueOf(orderAmount)/100;

                    String orderAmountStr = String.valueOf(amount);

//                    开启打印机
//                    starPrint(merchantName, merchantNoStr, payChannelStr, completeTime, orderAmountStr);


                    Intent mIntent = new Intent(SweepActivity.this, PayOrderActivity.class);
                    mIntent.putExtra("flag", true);
//                    saveToDatabase();
                    startActivity(mIntent);


                }

            }else if (msg.what == 81) {
                //                关闭定时器
                selfHandler.removeCallbacks(runnable);

                JudgeBean judgeBean = (JudgeBean) msg.obj;
                String message = judgeBean.getMsg();
                ToastUtil.showShort(SweepActivity.this, message);
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sweep);

        SharedPreferences share = super.getSharedPreferences(fileName,
                    MODE_PRIVATE);

        token = share.getString("token", null);
        merchantNo = share.getString("merchantNo", null);

        Log.i("charge token", merchantNo);

        Intent intent = getIntent();
        data = intent.getStringExtra("payMoney");
        Log.i("charge data", data);


        merchantPayMoney = (TextView) findViewById(R.id.merchant_sweep);

        merchantPayMoney.setText("¥ " + data);


        String urlmoney = changeY2F(Double.valueOf(data));

        Log.i("change qgcode2", urlmoney);

        int paymoneys = Integer.parseInt(urlmoney);


        new PaymentTask().execute(new PaymentRequest("WEIXIN", paymoneys, "13"));


        changbutton = (Button) findViewById(R.id.changebutton);
        changbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        backbutton = (ImageButton) findViewById(R.id.btn_back);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public static String changeY2F(Double amount){
        return BigDecimal.valueOf(amount).multiply(new BigDecimal(100)).setScale(0).toString();
    }

    private class PaymentTask extends AsyncTask<PaymentRequest, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Log.i("charge response-json", "hahah");
        }

        @Override
        protected String doInBackground(PaymentRequest... pr) {
            PaymentRequest paymentRequest = pr[0];
            String data = null;

//            Log.i("charge response-json", "hahah");

            orderNo = getTime();
            String channel = paymentRequest.getChannel();
            amount = paymentRequest.getAmount();



            try {
                data = Request.pay(orderNo, amount, channel, "13", 2, token);
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

                PayResoultBean payResoultBean = new Gson().fromJson(data, PayResoultBean.class);
                String credential = payResoultBean.getResult().getCredential();

                trxNoStr = payResoultBean.getResult().getTrxNo();

                Log.i("charge trxNoStr", trxNoStr);


                QrCode = (ImageView) findViewById(R.id.QrCode);

                try {
                    //获取输入的文本信息
//                    String str = "123456789";
                    if(credential != null && !"".equals(credential.trim())){
                        //根据输入的文本生成对应的二维码并且显示出来
                        Bitmap mBitmap = EncodingHandler.createQRCode(credential.toString(), 500);
                        if(mBitmap != null){

                            ToastUtil.showShort(SweepActivity.this, "二维码生成成功！");
                            QrCode.setImageBitmap(mBitmap);

                            //                开始定时器
                            selfHandler.postDelayed(runnable, 5000);

                        }
                    }else{

                        ToastUtil.showShort(SweepActivity.this, "文本信息不能为空！");
                    }
                } catch (WriterException e) {
                    e.printStackTrace();
                }



//
//                if (status.equals("SUCCESS")) {
//
//                    ToastUtil.showShort(SweepActivity.this, "收款成功");
//
//                }

            }else {

                ToastUtil.showShort(SweepActivity.this, message);
                return;
            }

        }
    }

//    获取时间加随机数
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
                selfHandler.postDelayed(this,5000);
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
                    data = getDetailJson(trxNoStr, token);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.i("charge response", data);

                JudgeBean judgeBean = new Gson().fromJson(data, JudgeBean.class);

                String respCode = judgeBean.getCode();

                if (respCode.equals("0")){

                    OrderDetailBean orderDetailBean = new Gson().fromJson(data, OrderDetailBean.class);
                    Message msg = new Message();
                    msg.what = 8;
                    msg.obj = orderDetailBean;
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


    //  打印机函数
    private void starPrint(final String merchantName, final String merchantNo, final String payChannel, final String orderTime, final String orderAmount) {
        // TODO Auto-generated method stub
        try {

            printerDevice.open();

//            ToastUtil.showShort(PayMoneyActivity.this, "打印机设备打开成功");

            format = new Format();
            try {
                if (printerDevice.queryStatus() == printerDevice.STATUS_OUT_OF_PAPER) {

                    ToastUtil.showShort(SweepActivity.this, "打印机缺纸");
                } else if (printerDevice.queryStatus() == printerDevice.STATUS_PAPER_EXIST) {

                    ToastUtil.showShort(SweepActivity.this, "打印机状态正常，开始打印");
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
                ToastUtil.showShort(SweepActivity.this, "查看打印机状态失败");
                de.printStackTrace();
                //          关闭打印机
                stopPrint();
            }
        } catch (DeviceException de) {
            de.printStackTrace();
            ToastUtil.showShort(SweepActivity.this, "打印机设备打开失败");
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


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //                关闭定时器
        selfHandler.removeCallbacks(runnable);

    }


}
