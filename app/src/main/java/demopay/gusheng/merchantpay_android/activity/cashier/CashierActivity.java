package demopay.gusheng.merchantpay_android.activity.cashier;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import demopay.gusheng.merchantpay_android.R;
import demopay.gusheng.merchantpay_android.adapter.CashierAdapter;
import demopay.gusheng.merchantpay_android.bean.CashierDataBean;
import demopay.gusheng.merchantpay_android.bean.CashierListBean;
import demopay.gusheng.merchantpay_android.bean.JudgeBean;
import demopay.gusheng.merchantpay_android.bean.LoginDataBean;
import demopay.gusheng.merchantpay_android.util.Switch;
import demopay.gusheng.merchantpay_android.util.ToastUtil;

import static demopay.gusheng.merchantpay_android.util.OrderRequest.getCashierJson;

/**
 * Created by fby on 2017/9/26.
 */

public class CashierActivity extends Activity {

    private ListView listView = null;


    private  String token;
    private  String merchantNo;

    private Switch switchButton;

    private String dataStr = null;

    private static final String fileName = "logintext";//定义保存的文件的名称

    private List<CashierDataBean> cashierDataBeen;

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            if (msg.what == 1) {

                CashierListBean cashierListBean = (CashierListBean) msg.obj;

                cashierDataBeen = cashierListBean.getData();

                listView.setAdapter(new CashierAdapter(CashierActivity.this, cashierDataBeen));


            }else if (msg.what == 11) {

                JudgeBean judgeBean = (JudgeBean) msg.obj;

                String message = judgeBean.getMsg();

                ToastUtil.showShort(CashierActivity.this, message);

            }else if (msg.what == 404){

                ToastUtil.showShort(CashierActivity.this, "网络未链接");
            }else if (msg.what == 406){

                ToastUtil.showShort(CashierActivity.this, "网络请求出错");
            }

        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);

        ImageButton backButton = (ImageButton) findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ImageButton addButton = (ImageButton) findViewById(R.id.btn_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(CashierActivity.this, AddCashierActivity.class);
                startActivity(mIntent);
            }
        });

        SharedPreferences share = super.getSharedPreferences(fileName,
                MODE_PRIVATE);

        token = share.getString("token", null);
        merchantNo = share.getString("merchantNo", null);



        networkData();


        listView = (ListView) findViewById(R.id.cashierlist);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Log.i("charge position", String.valueOf(position));
                Intent mIntent = new Intent(CashierActivity.this, CashierDataActivity.class);



                CashierDataBean cashierDataBean = (CashierDataBean) listView.getAdapter().getItem(position);
                String cashierDataBeanjson = new Gson().toJson(cashierDataBean);
                mIntent.putExtra("cashier", cashierDataBeanjson);
                startActivity(mIntent);

            }
        });

        initView();//初始化控件方法
    }


    private void initView() {


    }

    private void networkData(){

        Runnable orderRunnable = new Runnable() {
            @Override
            public void run() {
                String data = null;
                try {

                    data = getCashierJson(merchantNo, token);

                } catch (ConnectException e) {
                    e.printStackTrace();

                    dataStr = "404";

                    Log.i("charge data", "网络未链接");


                } catch (IOException e) {
                    e.printStackTrace();

                    dataStr = "406";

                }

                if (data == null) {

                    if (dataStr.equals("404")) {

                        Message msg = new Message();
                        msg.what = 404;
                        mHandler.sendMessage(msg);

                    }else {

                        Message msg = new Message();
                        msg.what = 406;
                        mHandler.sendMessage(msg);

                    }


                }else {

                    Log.i("charge response", data);

                    JudgeBean judgeBean = new Gson().fromJson(data, JudgeBean.class);

                    String respCode = judgeBean.getCode();
                    if (respCode.equals("0")){

                        CashierListBean charge = new Gson().fromJson(data, CashierListBean.class);
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

            }
        };
        // 必须异步调用
        Thread payThread = new Thread(orderRunnable);
        payThread.start();

    }

}
