package demopay.gusheng.merchantpay_android.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import demopay.gusheng.merchantpay_android.R;
import demopay.gusheng.merchantpay_android.util.ToastUtil;

import com.wizarpos.paymentrouter.aidl.IWizarPayment;
import android.view.View.OnClickListener;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by fby on 2017/11/3.
 */

public class BankCardActivity extends Activity implements OnClickListener {

    private TextView amount;
    private String data;

    private IWizarPayment wizarPayment = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bankcard);

        ImageButton backButton = (ImageButton) findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        data = intent.getStringExtra("payMoney");
        Log.i("charge data", data);

        amount = (TextView) findViewById(R.id.bankcard_content);

        amount.setText("金额：RMB" + data);


//        if(v.getId() != R.id.bankcard_bind && wizarPayment == null)
//        {
////            textInfo.setText("未连接服务");
//            Log.i("charge data", "未连接服务");
//
//            return;
//        }

        Button bankcardbindButton = (Button) findViewById(R.id.bankcard_bind);
        bankcardbindButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject param = new JSONObject();
                String ret = "ok";
                try
                {

                    Log.i("charge data", "判断走了");


                    param.put("AppID", "");
                    param.put("AppName", "");

                    switch(v.getId())
                    {

                        case R.id.bankcard_bind:
                            bindService();
                            break;
                    }


                }

                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });

        Button bankcardunbindButton = (Button) findViewById(R.id.bankcard_unbind);
        bankcardunbindButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject param = new JSONObject();
                String ret = "ok";
                try
                {

                    Log.i("charge data", "判断走了");


                    param.put("AppID", "");
                    param.put("AppName", "");

                    switch(v.getId())
                    {

                        case R.id.bankcard_unbind:
                            unBindService();
                            break;
                    }

                }

                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });

        Button bankcardloginButton = (Button) findViewById(R.id.bankcard_login);
        bankcardloginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject param = new JSONObject();
                String ret = "ok";
                try
                {

                    Log.i("charge data", "判断走了");


                    param.put("AppID", "");
                    param.put("AppName", "");

                    switch(v.getId())
                    {

                        case R.id.bankcard_login:
                            param.put("OptCode", "01");
                            param.put("OptPass", "0000");
                            ret = wizarPayment.login(param.toString());
                            break;
                    }

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
        });

        Button bankcardsaleButton = (Button) findViewById(R.id.bankcard_sale);
        bankcardsaleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject param = new JSONObject();
                String ret = "ok";
                try
                {

                    Log.i("charge data", "判断走了");


                    param.put("AppID", "");
                    param.put("AppName", "");

                    switch(v.getId())
                    {

                        case R.id.bankcard_sale:
                            param.put("TransAmount", "1");
                            param.put("TransType", 1);
                            param.put("ExternTransFlag", "1");
                            ret = wizarPayment.payCash(param.toString());
                            break;
                    }

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
        });

//        Button bankcardvoidsaleButton = (Button) findViewById(R.id.bankcard_voidsale);
//        bankcardvoidsaleButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                JSONObject param = new JSONObject();
//                String ret = "ok";
//                try
//                {
//
//                    Log.i("charge data", "判断走了");
//
//
//                    param.put("AppID", "");
//                    param.put("AppName", "");
//
//                    switch(v.getId())
//                    {
//
//                        case R.id.bankcard_voidsale:
////                param.put("TransType", 4);
////                ret = wizarPayment.consumeCancel(param.toString());
//                            ret = wizarPayment.consumeCancel(param.toString());
//                            break;
//                    }
//
//                    Log.i("charge data", "result:"+ret);
//
//                }
//
//                catch (JSONException e)
//                {
//                    e.printStackTrace();
//                }
//                catch (RemoteException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        });


        Button bankcardfindLogineButton = (Button) findViewById(R.id.bankcard_findlogin);
        bankcardfindLogineButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject param = new JSONObject();
                String ret = "ok";
                try
                {

                    Log.i("charge data", "查询走了");


                    param.put("AppID", "");
                    param.put("AppName", "");

                    switch(v.getId())
                    {

                        case R.id.bankcard_findlogin:
                            param.put("ProtocolVersion", "201701");
//                            param.put("TransType", 500);
                            param.put("ReqTransDate", "20171106");
                            ret = wizarPayment.getPOSInfo(param.toString());
                            Log.i("charge data find", "result:"+ret);

//                            ToastUtil.showShort(BankCardActivity.this, "已签到");
//                            ret = wizarPayment.consumeCancel(param.toString());
                            break;
                    }

//                    Log.i("charge data", "result:"+ret);

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
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unBindService();
    }


//    绑定到支付应用调用服务。
    private ServiceConnection serviceConnection = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name)
        {
            wizarPayment = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service)
        {
            wizarPayment = IWizarPayment.Stub.asInterface(service);
        }
    };

    public void bindService()
    {
        try
        {
            //绑定支付服务
            bindService(new Intent(IWizarPayment.class.getName()), serviceConnection, Context.BIND_AUTO_CREATE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void unBindService()
    {
        if(wizarPayment != null)
        {
            unbindService(serviceConnection);
            wizarPayment = null;
        }
    }

        @Override
        public void onClick(View v)
        {
            Log.i("charge data", "点击事件走了");

        JSONObject param = new JSONObject();
        String ret = "ok";
        if(v.getId() != R.id.bankcard_bind && wizarPayment == null)
        {
//            textInfo.setText("未连接服务");
            Log.i("charge data", "未连接服务");

            return;
        }
        try
        {

            Log.i("charge data", "判断走了");


            param.put("AppID", "");
            param.put("AppName", "");

            switch(v.getId())
            {

                case R.id.bankcard_bind:
                    bindService();
                    break;
                case R.id.bankcard_unbind:
                    unBindService();
                    break;
                case R.id.bankcard_login:
                    param.put("OptCode", "01");
                    param.put("OptPass", "0000");
                    ret = wizarPayment.login(param.toString());
                    break;
                case R.id.bankcard_sale:
                    param.put("TransAmount", "1");
                    param.put("TransType", 1);
                    param.put("ExternTransFlag", "1");
                    ret = wizarPayment.payCash(param.toString());
                    break;
//                case R.id.bankcard_voidsale:
////                param.put("TransType", 4);
////                ret = wizarPayment.consumeCancel(param.toString());
//                    ret = wizarPayment.consumeCancel(param.toString());
//                    break;
            }

//            textInfo.setText("result:"+ret);
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
