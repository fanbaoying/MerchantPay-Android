package demopay.gusheng.merchantpay_android.Fragement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.ConnectException;

import demopay.gusheng.merchantpay_android.R;
import demopay.gusheng.merchantpay_android.activity.ForgetpsdActivity;
import demopay.gusheng.merchantpay_android.activity.MainActivity;
import demopay.gusheng.merchantpay_android.activity.OrderDetailActicity;
import demopay.gusheng.merchantpay_android.activity.PayMoneyActivity;
import demopay.gusheng.merchantpay_android.activity.RegisterActivity;
import demopay.gusheng.merchantpay_android.activity.ViewPagerAdapter;
import demopay.gusheng.merchantpay_android.bean.JudgeBean;
import demopay.gusheng.merchantpay_android.bean.LoginBean;
import demopay.gusheng.merchantpay_android.bean.LoginDataBean;
import demopay.gusheng.merchantpay_android.bean.OrderDetailBean;
import demopay.gusheng.merchantpay_android.util.ToastUtil;

import static android.content.Context.MODE_PRIVATE;
import static demopay.gusheng.merchantpay_android.util.OrderRequest.getJson;
import static demopay.gusheng.merchantpay_android.util.OrderRequest.orderdetailURL;
import static demopay.gusheng.merchantpay_android.util.Request.loginData;

/**
 * Created by fby on 2017/9/7.
 */

public class LoginFragement extends Fragment {

    private static final String fileName = "logintext";//定义保存的文件的名称
    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;
    MenuItem prevMenuItem;

    //三个fragment
    private FirstFragement f1;
    private SecondFragement f2;

    private TabbarFragement tabbarbutton;

    //底部三个按钮
    private Button foot1;
    private Button foot2;


    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;


    private EditText phonetext;
    private EditText passwordtext;

    private Button merchaneLoginButton;
    private Button cashierLoginButton;

    private ImageView iamgemerchant;
    private EditText merchantnumber;

    private String loginType;

    private String dataStr = null;


    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            if (msg.what == 1) {

                LoginDataBean loginDataBean = (LoginDataBean) msg.obj;

                String token = loginDataBean.getToken();
                String shortName = loginDataBean.getUserInfo().getShortName();
                String merchantNo = loginDataBean.getUserInfo().getMerchantNo();
                String username = loginDataBean.getUserInfo().getUsername();
                String userStatus = loginDataBean.getUserInfo().getUserStatus();

                SharedPreferences share = getActivity().getSharedPreferences(fileName, MODE_PRIVATE); //实例化
                SharedPreferences.Editor editor = share.edit(); //使处于可编辑状态
                editor.putInt("loginid", 0); //设置保存的数据
                editor.putString("token", token);
                editor.putString("shortName", shortName);
                editor.putString("merchantNo", merchantNo);
                editor.putString("username", username);
                editor.putString("userStatus", userStatus);

                editor.commit(); //提交数据保存


                FragmentManager fgManager = getFragmentManager();
                //Activity用来管理它包含的Frament，通过getFramentManager()获取
                FragmentTransaction fragmentTransaction = fgManager.beginTransaction();
//获取Framgent事务
                Fragment fragment = fgManager.findFragmentById(R.id.main_frame_layout);
//删除一个Fragment之前，先通过FragmentManager的findFragmemtById()，找到对应的Fragment
                fragmentTransaction.remove(fragment);
//删除获取到的Fragment
//指定动画，可以自己添加
                String tag = null;
                fragmentTransaction.addToBackStack(tag);
//如果需要，添加到back栈中
                fragmentTransaction.commit();

                viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
                bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.setOnNavigationItemSelectedListener(
                        new BottomNavigationView.OnNavigationItemSelectedListener() {
                            @Override
                            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.item_call:
                                        Log.i("charge loginid", "tabbar1");
                                        viewPager.setCurrentItem(0,false);
                                        break;
                                    case R.id.item_mail:
                                        Log.i("charge loginid", "tabbar2");
                                        viewPager.setCurrentItem(1,false);
                                        break;
                                }
                                return false;
                            }
                        });

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        if (prevMenuItem != null) {
                            prevMenuItem.setChecked(false);
                        } else {
                            bottomNavigationView.getMenu().getItem(0).setChecked(false);
                        }
                        bottomNavigationView.getMenu().getItem(position).setChecked(true);
                        prevMenuItem = bottomNavigationView.getMenu().getItem(position);

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                setupViewPager(viewPager);

            }else if (msg.what == 11) {

                LoginDataBean loginDataBean = (LoginDataBean) msg.obj;

                String message = loginDataBean.getMsg();

//                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                ToastUtil.showShort(getActivity(), message);

            }else if (msg.what == 404){

                ToastUtil.showShort(getActivity(), "网络未链接");
            }else if (msg.what == 406){

                ToastUtil.showShort(getActivity(), "网络请求出错");
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_login,container,false);
//        View view = inflater.inflate(R.layout.fragment_login, null);

        loginType = "1";


        Log.i("charge loginid", "1");

        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.i("charge loginid", "2");

        merchantnumber = (EditText) getActivity().findViewById(R.id.merchant_number);

//        商户按钮
        merchaneLoginButton = (Button) getActivity().findViewById(R.id.login_merchant);
        merchaneLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iamgemerchant = (ImageView) getActivity().findViewById(R.id.imageview_merchant);
                iamgemerchant.setVisibility(View.GONE);
//                merchantnumber = (EditText) getActivity().findViewById(R.id.merchant_number);
                merchantnumber.setVisibility(View.GONE);

                merchaneLoginButton.setTextColor(Color.parseColor("#000000"));
                merchaneLoginButton.setBackgroundColor(Color.parseColor("#FFFFFF"));

                cashierLoginButton.setTextColor(Color.parseColor("#FFFFFF"));
                cashierLoginButton.setBackgroundColor(Color.parseColor("#b5bcc0"));

                loginType = "1";

            }
        });


//        收银员按钮
        cashierLoginButton = (Button) getActivity().findViewById(R.id.login_cashier);
        cashierLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iamgemerchant = (ImageView) getActivity().findViewById(R.id.imageview_merchant);
                iamgemerchant.setVisibility(View.VISIBLE);
//                merchantnumber = (EditText) getActivity().findViewById(R.id.merchant_number);
                merchantnumber.setVisibility(View.VISIBLE);


                cashierLoginButton.setTextColor(Color.parseColor("#000000"));
                cashierLoginButton.setBackgroundColor(Color.parseColor("#FFFFFF"));

                merchaneLoginButton.setTextColor(Color.parseColor("#FFFFFF"));
                merchaneLoginButton.setBackgroundColor(Color.parseColor("#b5bcc0"));

                loginType = "2";

            }
        });



//        忘记密码
        Button forgetpassword = (Button) getActivity().findViewById(R.id.forgetpassword);

        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), ForgetpsdActivity.class));

            }
        });

//        注册按钮
        Button register = (Button) getActivity().findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RegisterActivity.class));
            }
        });


        phonetext = (EditText) getActivity().findViewById(R.id.login_phonetext);

        passwordtext = (EditText) getActivity().findViewById(R.id.login_passwordtext);

        phonetext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (!hasFocus) {
                    System.out.println("失去焦点");
                    // 失去焦点
                    phonetext.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(phonetext.getWindowToken(), 0);
                }
            }
        });

        passwordtext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (!hasFocus) {
                    System.out.println("失去焦点");
                    // 失去焦点
                    passwordtext.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(passwordtext.getWindowToken(), 0);
                }
            }
        });


//        登录

        Button login = (Button) getActivity().findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Runnable orderRunnable = new Runnable() {
                    @Override
                    public void run() {
                        String data = null;

                        try {

//                            data = loginData("demo", "8");


                            Log.i("charge phonetext", phonetext.getText().toString());
                            Log.i("charge passwordtext", passwordtext.getText().toString());

                            String phoneTextStr = phonetext.getText().toString();
                            String passwordtextStr = passwordtext.getText().toString();
                            String merchantnumberStr = merchantnumber.getText().toString();

                            data = loginData(phoneTextStr, passwordtextStr, "1", loginType);

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

                            LoginDataBean loginDataBean = new Gson().fromJson(data, LoginDataBean.class);

                            String code = loginDataBean.getCode();


                            Log.i("charge code", code);

                            if (code.equals("0")){

                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = loginDataBean;
                                mHandler.sendMessage(msg);

                            }else{

                                Message msg = new Message();
                                msg.what = 11;
                                msg.obj = loginDataBean;
                                mHandler.sendMessage(msg);


                            }
                        }


                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(orderRunnable);
                payThread.start();


            }

        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        adapter.addFragment(new FirstFragement());
        adapter.addFragment(new SecondFragement());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }

    //显示第一个fragment
    private void initFragment1(){
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
        if(f1 == null){
            f1 = new FirstFragement();
            transaction.add(R.id.main_frame_layout, f1);
        }
//        //隐藏所有fragment
//        hideFragment(transaction);
//        //显示需要显示的fragment
//        transaction.show(f1);


        //提交事务
        transaction.commit();
    }

    //隐藏所有的fragment
    private void hideFragment(FragmentTransaction transaction){
        if(f1 != null){
            transaction.hide(f1);
        }
        if(f2 != null){
            transaction.hide(f2);
        }
    }

}
