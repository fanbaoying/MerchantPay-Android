package demopay.gusheng.merchantpay_android.activity.cashier;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.annotations.JsonAdapter;

import java.io.IOException;
import java.net.ConnectException;
import java.util.List;

import demopay.gusheng.merchantpay_android.R;
import demopay.gusheng.merchantpay_android.adapter.CashierAdapter;
import demopay.gusheng.merchantpay_android.bean.CashierDataBean;
import demopay.gusheng.merchantpay_android.bean.CashierListBean;
import demopay.gusheng.merchantpay_android.bean.JudgeBean;
import demopay.gusheng.merchantpay_android.util.Switch;
import demopay.gusheng.merchantpay_android.util.ToastUtil;

import static demopay.gusheng.merchantpay_android.util.OrderRequest.getCashierJson;
import static demopay.gusheng.merchantpay_android.util.Request.postJsonAddCashier;

/**
 * Created by fby on 2017/9/28.
 */

public class AddCashierActivity extends Activity {

    private EditText addName;
    private EditText addPhone;
    private EditText addType;
    private EditText addNumber;
    private EditText addPassword;

    private  String token;
    private  String merchantNo;

    private String dataStr = null;

    private static final String fileName = "logintext";//定义保存的文件的名称

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            if (msg.what == 1) {

                ToastUtil.showShort(AddCashierActivity.this, "新增收银员成功");


            }else if (msg.what == 11) {

                JudgeBean judgeBean = (JudgeBean) msg.obj;

                String message = judgeBean.getMsg();

                ToastUtil.showShort(AddCashierActivity.this, message);

            }else if (msg.what == 404){

                ToastUtil.showShort(AddCashierActivity.this, "网络未链接");
            }else if (msg.what == 406){

                ToastUtil.showShort(AddCashierActivity.this, "网络请求出错");
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcashier);

        addName = (EditText) findViewById(R.id.add_name);
        addPhone = (EditText) findViewById(R.id.add_phone);
        addType = (EditText) findViewById(R.id.add_type);
        addNumber = (EditText) findViewById(R.id.add_number);
        addPassword = (EditText) findViewById(R.id.add_password);


        SharedPreferences share = super.getSharedPreferences(fileName,
                MODE_PRIVATE);

        token = share.getString("token", null);
        merchantNo = share.getString("merchantNo", null);



        ImageButton backButton = (ImageButton) findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button addSaveButton = (Button) findViewById(R.id.btn_addSave);
        addSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                networkData();

            }
        });


    }


    private void networkData(){

        Runnable orderRunnable = new Runnable() {
            @Override
            public void run() {

                CashierDataBean cashierDataBean = new CashierDataBean();

                cashierDataBean.setMerchantNo(merchantNo);
                cashierDataBean.setUsername(addPhone.getText().toString());
                cashierDataBean.setMobile(addPhone.getText().toString());
                cashierDataBean.setPassword(addPassword.getText().toString());
                cashierDataBean.setRealName(addName.getText().toString());
                cashierDataBean.setUserCode(addNumber.getText().toString());

                cashierDataBean.setUserStatus(addType.getText().toString());

                cashierDataBean.setRoleId("f75fa1f4d9be4813a0699f0a4c115da9");
                cashierDataBean.setPageTheme("default");
                cashierDataBean.setQueryAuth("2");

                String cashierDataBeanJson = new Gson().toJson(cashierDataBean);

                Log.i("charge json", cashierDataBeanJson);

                String data = null;
                try {

                    data = postJsonAddCashier(cashierDataBeanJson, token);

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

                    Log.i("charge data", data);

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
