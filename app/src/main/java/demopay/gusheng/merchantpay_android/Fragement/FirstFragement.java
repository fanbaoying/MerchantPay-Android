package demopay.gusheng.merchantpay_android.Fragement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.util.Date;

import demopay.gusheng.merchantpay_android.R;
import demopay.gusheng.merchantpay_android.activity.BankCardActivity;
import demopay.gusheng.merchantpay_android.activity.LookActivity;
import demopay.gusheng.merchantpay_android.activity.PayMoneyActivity;
import demopay.gusheng.merchantpay_android.bean.ChargeBean;
import demopay.gusheng.merchantpay_android.bean.JudgeBean;
import demopay.gusheng.merchantpay_android.bean.OrderDetailBean;
import demopay.gusheng.merchantpay_android.bean.TodayDataBean;
import demopay.gusheng.merchantpay_android.util.Request;
import demopay.gusheng.merchantpay_android.util.ToastUtil;

import static android.content.Context.MODE_PRIVATE;
import static demopay.gusheng.merchantpay_android.util.OrderRequest.getTodayDataJson;

/**
 * Created by fby on 2017/9/1.
 */

public class FirstFragement extends Fragment {

    BottomNavigationView bottomNavigationView;

    private  String token;
    private  String merchantNo;

    private TextView todaycontent1;
    private TextView todaycontent2;
    private TextView todaycontent3;
    private TextView todaycontent4;

    private String dataStr = null;

    private static final String fileName = "logintext";//定义保存的文件的名称

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            if (msg.what == 2) {

                TodayDataBean todayDataBean = (TodayDataBean) msg.obj;
                String payAmount = todayDataBean.getResult().getPayAmount();
                String payNum = todayDataBean.getResult().getPayNum();
                String refundAmount = todayDataBean.getResult().getRefundAmount();
                String refundNum = todayDataBean.getResult().getRefundNum();

                Double amountDouble = Double.valueOf(payAmount);
                String amountStr = String.valueOf(amountDouble/100);

                Double refundAmountDouble = Double.valueOf(refundAmount);
                String refundAmountStr = String.valueOf(refundAmountDouble/100);

                todaycontent1 = (TextView) getActivity().findViewById(R.id.todaycontent1);
                todaycontent2 = (TextView) getActivity().findViewById(R.id.todaycontent2);
                todaycontent3 = (TextView) getActivity().findViewById(R.id.todaycontent3);
                todaycontent4 = (TextView) getActivity().findViewById(R.id.todaycontent4);

                todaycontent1.setText(amountStr);
                todaycontent2.setText(payNum);
                todaycontent3.setText(refundAmountStr);
                todaycontent4.setText(refundNum);


            }else if (msg.what == 21){

                JudgeBean judgeBean = (JudgeBean) msg.obj;
                String message = judgeBean.getMsg();

                ToastUtil.showShort(getActivity(), message);
            }else if (msg.what == 404){

                ToastUtil.showShort(getActivity(), "网络未链接");
            }else if (msg.what == 406){

                ToastUtil.showShort(getActivity(), "网络请求出错");
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_first,container,false);

//        View view = inflater.inflate(R.layout.fragment_first, null);

        SharedPreferences share = getActivity().getSharedPreferences(fileName,
                MODE_PRIVATE);

        token = share.getString("token", null);
        merchantNo = share.getString("merchantNo", null);

        Log.i("charge token", token);


        todayData();

        return view;

    }

    private void todayData() {

        Runnable refundRunnable = new Runnable() {
            @Override
            public void run() {
                String data = null;

                try {
                    data = getTodayDataJson(merchantNo, token);

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
//                    String message = judgeBean.getMsg();
//                    Log.i("charge message", message);

                    if (respCode.equals("0")){

                        TodayDataBean todayDataBean = new Gson().fromJson(data, TodayDataBean.class);
                        Message msg = new Message();
                        msg.what = 2;
                        msg.obj = todayDataBean;
                        mHandler.sendMessage(msg);

                    }else {

                        Message msg = new Message();
                        msg.what = 21;
                        msg.obj = judgeBean;
                        mHandler.sendMessage(msg);

                    }

                }



            }
        };
        // 必须异步调用
        Thread refundThread = new Thread(refundRunnable);
        refundThread.start();

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ImageView sweepImageView = (ImageView) getActivity().findViewById(R.id.image1);

        sweepImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(getActivity(), "nihao", Toast.LENGTH_LONG).show();

                startActivity(new Intent(getActivity(), PayMoneyActivity.class));

            }
        });

        ImageView lookImageView = (ImageView) getActivity().findViewById(R.id.image2);
        lookImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), LookActivity.class));

//                //获取fragment的实例
//                Fragment lookfragment=new LookFragement();
//                //获取Fragment的管理器
//                FragmentManager fragmentManager=getFragmentManager();
//                //开启fragment的事物,在这个对象里进行fragment的增删替换等操作。
//                FragmentTransaction ft=fragmentManager.beginTransaction();
//                //跳转到fragment，第一个参数为所要替换的位置id，第二个参数是替换后的fragment
//                ft.replace(R.id.main_frame_layout,lookfragment);
//                //提交事物
//                ft.commit();
//
//
//                bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
//                bottomNavigationView.getMenu().getItem(0).setChecked(true);
//                bottomNavigationView.getMenu().getItem(1).setChecked(false);
//                bottomNavigationView.setVisibility(View.GONE);



            }
        });


    }
}
