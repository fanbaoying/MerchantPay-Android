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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.ConnectException;

import demopay.gusheng.merchantpay_android.R;
import demopay.gusheng.merchantpay_android.bean.CashierDataBean;
import demopay.gusheng.merchantpay_android.bean.CashierListBean;
import demopay.gusheng.merchantpay_android.bean.JudgeBean;
import demopay.gusheng.merchantpay_android.util.ToastUtil;

import static demopay.gusheng.merchantpay_android.util.Request.postJsonAddCashier;
import static demopay.gusheng.merchantpay_android.util.Request.putJsonCashier;

/**
 * Created by fby on 2017/9/27.
 */

public class EditCashierActivity extends Activity {

    private CashierDataBean cashierDataBean;

    private EditText editName;
    private EditText editPhone;
    private TextView editNumber;
    private EditText editPassword;
    private TextView editType;

    private  String token;
    private  String merchantNo;

    private String dataStr = null;

    private static final String fileName = "logintext";//定义保存的文件的名称

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            if (msg.what == 1) {

                ToastUtil.showShort(EditCashierActivity.this, "修改收银员成功");


            }else if (msg.what == 11) {

                JudgeBean judgeBean = (JudgeBean) msg.obj;

                String message = judgeBean.getMsg();

                ToastUtil.showShort(EditCashierActivity.this, message);

            }else if (msg.what == 404){

                ToastUtil.showShort(EditCashierActivity.this, "网络未链接");
            }else if (msg.what == 406){

                ToastUtil.showShort(EditCashierActivity.this, "网络请求出错");
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editcashier);

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

        Button addSaveButton = (Button) findViewById(R.id.btn_editsave);
        addSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                networkData();

            }
        });

        initData();

    }

    private void initData() {

        //处理结果
        Intent mIntent = getIntent();
        String cashierDetail = mIntent.getStringExtra("cashier");
        cashierDataBean = new Gson().fromJson(cashierDetail, CashierDataBean.class);

        String realName = cashierDataBean.getRealName();
        String mobile = cashierDataBean.getMobile();
        String userCode = cashierDataBean.getUserCode();
        String userStatus = cashierDataBean.getUserStatus();

        editName = (EditText) findViewById(R.id.content_editname);
        editName.setText(realName);

        editPhone = (EditText) findViewById(R.id.content_editphone);
        editPhone.setText(mobile);

        editNumber = (TextView) findViewById(R.id.content_editnumber);
        editNumber.setText(userCode);

        editPassword = (EditText) findViewById(R.id.content_editpassword);
        editPassword.setText("*******");

        editType = (TextView) findViewById(R.id.content_edittype);
        editType.setText(userStatus);


    }

    private void networkData(){

        Runnable orderRunnable = new Runnable() {
            @Override
            public void run() {

//                CashierDataBean cashierDataBean = new CashierDataBean();

                cashierDataBean.setMobile(editPhone.getText().toString());
                cashierDataBean.setPassword(editPassword.getText().toString());
                cashierDataBean.setRealName(editName.getText().toString());

                String cashierDataBeanJson = new Gson().toJson(cashierDataBean);

                Log.i("charge json", cashierDataBeanJson);

                String data = null;
                try {

                    data = putJsonCashier(cashierDataBean, token);

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
