package demopay.gusheng.merchantpay_android.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import demopay.gusheng.merchantpay_android.R;
import demopay.gusheng.merchantpay_android.bean.JudgeBean;
import demopay.gusheng.merchantpay_android.bean.OrderDetailBean;
import demopay.gusheng.merchantpay_android.util.Request;

import static demopay.gusheng.merchantpay_android.util.OrderRequest.getDetailJson;
import static demopay.gusheng.merchantpay_android.util.OrderRequest.orderdetailURL;
import static demopay.gusheng.merchantpay_android.util.OrderRequest.getJson;
import static demopay.gusheng.merchantpay_android.util.OrderRequest.orderdetailURL;

/**
 * Created by fby on 2017/6/13.
 */

public class OrderDetailActicity extends Activity {

    String trxNostring;

    String amount;

    private Button refundbtn = null;


//    private static final String ORDER_URL = "http://pay.soongrande.com/agree-pay-app-gateway/api/app1/getByTrxNo/";

//    private static final String REFUND_URL = "http://pay.soongrande.com/agree-pay-web-gateway/api/v1/refund";


    private  String token;
    private  String merchantNo;


    private static final String fileName = "logintext";//定义保存的文件的名称


    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            if (msg.what == 1) {

                OrderDetailBean charge = (OrderDetailBean) msg.obj;
                String productName = charge.getResult().getProductName();
                String merchantOrderNo = charge.getResult().getMerchantOrderNo();
                String bankTrxNo = charge.getResult().getBankTrxNo();
                String payChannel = charge.getResult().getPayChannel();
                amount = charge.getResult().getAmount();
                String status = charge.getResult().getStatus();
                String createTime = charge.getResult().getCreateTime();
                String completeTime = charge.getResult().getCreateTime();

                Log.i("charge name", productName);

                TextView productNameView = (TextView) findViewById(R.id.content1);
                productNameView.setText(productName);

                TextView merchantOrderNoView = (TextView) findViewById(R.id.content2);
                merchantOrderNoView.setText(merchantOrderNo);

                TextView bankTrxNoView = (TextView) findViewById(R.id.content3);
                bankTrxNoView.setText(bankTrxNo);

                TextView payChannelView = (TextView) findViewById(R.id.content4);
                payChannelView.setText(payChannel);
                if (payChannel.equals("2002")) {
                    payChannelView.setText("支付宝");
                } else if (payChannel.equals("2001")) {
                    payChannelView.setText("微信");
                } else if (payChannel.equals("0002")) {
                    payChannelView.setText("银联");
                }

                TextView amountView = (TextView) findViewById(R.id.content5);
                amountView.setText("¥" + amount);

                TextView statusView = (TextView) findViewById(R.id.content6);
                if (status.equals("1")) {
                    statusView.setText("已支付");
                    refundbtn.setVisibility(View.VISIBLE);
                    Log.i("charge btn", "显示按钮");
                } else if (status.equals("8")) {
                    statusView.setText("已退款");
                    refundbtn.setVisibility(View.GONE);
                    Log.i("charge btn", "隐藏按钮");
                }

                TextView createTimeView = (TextView) findViewById(R.id.content7);
                createTimeView.setText(createTime);

                TextView completeTimeView = (TextView) findViewById(R.id.content8);
                completeTimeView.setText(completeTime);

            }else if (msg.what == 11){

                JudgeBean judgeBean = (JudgeBean) msg.obj;

                String message = judgeBean.getMsg();

                new AlertDialog.Builder(OrderDetailActicity.this).setTitle("系统提示")
                        .setMessage(message).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
            }else if (msg.what == 2){

                JudgeBean judgeBean = (JudgeBean) msg.obj;

                String message = judgeBean.getMsg();

                new AlertDialog.Builder(OrderDetailActicity.this).setTitle("系统提示")
                        .setMessage(message).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        refundbtn.setVisibility(View.GONE);
                    }
                }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        refundbtn.setVisibility(View.GONE);
                        finish();
                    }
                }).show();
            }else if (msg.what == 21){

                JudgeBean judgeBean = (JudgeBean) msg.obj;

                String message = judgeBean.getMsg();

                new AlertDialog.Builder(OrderDetailActicity.this).setTitle("系统提示")
                        .setMessage(message).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        trxNostring = intent.getStringExtra("trxNo");
        setContentView(R.layout.activity_orderdetail);


        SharedPreferences share = super.getSharedPreferences(fileName,
                MODE_PRIVATE);

        token = share.getString("token", null);
        merchantNo = share.getString("merchantNo", null);

        Log.i("charge token", token);

        refundbtn = (Button) findViewById(R.id.refundbtn);

        ImageView imageview = (ImageView) findViewById(R.id.iv_order_back);

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("charge btn", "走了");
                finish();
            }
        });



        Runnable orderRunnable = new Runnable() {
            @Override
            public void run() {
                String data = null;

//                String orderurl = orderdetailURL(ORDER_URL, trxNostring);

//                Log.i("charge url", orderurl);

                try {
                    data = getDetailJson(trxNostring, token);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.i("charge response", data);

                JudgeBean judgeBean = new Gson().fromJson(data, JudgeBean.class);

                String respCode = judgeBean.getCode();
//                Log.i("charge response", respCode);

                if (respCode.equals("0")){

                    OrderDetailBean charge = new Gson().fromJson(data, OrderDetailBean.class);
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = charge;
                    mHandler.sendMessage(msg);

                }else {

                    Message msg = new Message();
                    msg.what = 11;
                    msg.obj = judgeBean;
                    mHandler.sendMessage(msg);

                }
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(orderRunnable);
        payThread.start();





    }


    public void refundClick(View view){

        Log.i("charge btn", "走了");

        Runnable refundRunnable = new Runnable() {
            @Override
            public void run() {
                String data = null;

//                System.out.println((int));

                int number = (int)((Math.random()*9+1)*100000);

                Date now = new Date();
                String newTime = getNewTime(now);

                String refundNum = newTime + number;

                Log.i("charge time", refundNum);

                float amountStr = Float.valueOf(amount)* 100;
                BigDecimal bd = new BigDecimal(amountStr);
                BigDecimal setScale = bd.setScale(0,BigDecimal.ROUND_HALF_DOWN);

                try {
                    data = Request.refund(trxNostring, setScale, refundNum, token);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.i("charge refund", data);

                JudgeBean judgeBean = new Gson().fromJson(data, JudgeBean.class);

                String respCode = judgeBean.getCode();
                if (respCode.equals("0")){

                    OrderDetailBean charge = new Gson().fromJson(data, OrderDetailBean.class);
                    Message msg = new Message();
                    msg.what = 2;
                    msg.obj = judgeBean;
                    mHandler.sendMessage(msg);

                }else {

                    Message msg = new Message();
                    msg.what = 21;
                    msg.obj = judgeBean;
                    mHandler.sendMessage(msg);

                }

            }
        };
        // 必须异步调用
        Thread refundThread = new Thread(refundRunnable);
        refundThread.start();

    }

    private static String getNewTime(Date now) {
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyyMMddHHmmss");

        return sdfs.format(now);
    }

}
