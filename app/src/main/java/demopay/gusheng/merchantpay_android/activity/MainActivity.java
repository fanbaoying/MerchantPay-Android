package demopay.gusheng.merchantpay_android.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.BottomNavigationView;
import android.widget.Button;
import android.widget.Toast;

import com.wizarpos.paymentrouter.aidl.IWizarPayment;

import org.json.JSONException;
import org.json.JSONObject;

import demopay.gusheng.merchantpay_android.Fragement.FirstFragement;
import demopay.gusheng.merchantpay_android.Fragement.LoginFragement;
import demopay.gusheng.merchantpay_android.Fragement.SecondFragement;
import demopay.gusheng.merchantpay_android.Fragement.TabbarFragement;
import demopay.gusheng.merchantpay_android.R;
import demopay.gusheng.merchantpay_android.adapter.ServiceObject;
import demopay.gusheng.merchantpay_android.util.Constant;
import demopay.gusheng.merchantpay_android.zxing.sweepactivity.CaptureActivity;

public class MainActivity extends AppCompatActivity {

//    @InjectView(R.id.activity_main)
//    RelativeLayout activityMain;

    //三个fragment
    private FirstFragement f1;
    private SecondFragement f2;
    private LoginFragement f3;

    private TabbarFragement tabbarbutton;

    //底部三个按钮
    private Button foot1;
    private Button foot2;

    private static final String fileName = "logintext";//定义保存的文件的名称


    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    MenuItem prevMenuItem;
//
//    View viewLab;

    private IWizarPayment wizarPayment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        //        银行卡连接服务
        JSONObject param = new JSONObject();

        try
        {

            param.put("AppID", "");
            param.put("AppName", "");

            Log.i("charge loginid", "连接服务");
            bindService();

        }catch (JSONException e)
        {
            e.printStackTrace();
        }

        //第一次初始化首页默认显示第一个fragment
        SharedPreferences share = super.getSharedPreferences(fileName,
                MODE_PRIVATE);
        int count = share.getInt("loginid", 1);

        if (count == 0) {


//            initFragment1();

            viewPager = (ViewPager) findViewById(R.id.viewpager);
            bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

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

        }else {

            initFragment3();

            bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

            bottomNavigationView.getMenu().getItem(0).setChecked(true);
            bottomNavigationView.getMenu().getItem(1).setChecked(false);
            bottomNavigationView.setVisibility(View.GONE);
        }




    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FirstFragement());
        adapter.addFragment(new SecondFragement());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }


//    //显示第一个fragment
//    private void initFragment1(){
//        //开启事务，fragment的控制是由事务来实现的
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
//        if(f1 == null){
//            f1 = new FirstFragement();
//            transaction.replace(R.id.main_frame_layout, f1);
//        }
//        //隐藏所有fragment
//        hideFragment(transaction);
//        //显示需要显示的fragment
//        transaction.show(f1);
//
//
//        //提交事务
//        transaction.commit();
//    }


//    //显示第二个fragment
//    private void initFragment2(){
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//        if(f2 == null){
//            f2 = new SecondFragement();
//            transaction.add(R.id.main_frame_layout,f2);
//        }
//        hideFragment(transaction);
//        transaction.show(f2);
//
//
////        if(f2 == null) {
////            f2 = new MyFragment("联系人");
////        }
////        transaction.replace(R.id.main_frame_layout, f2);
//
//        transaction.commit();
//    }

    //显示登录fragment
    private void initFragment3(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if(f3 == null){
            f3 = new LoginFragement();

        }
        transaction.replace(R.id.main_frame_layout,f3);

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


//    public void tabbar1(View view){
//
//        initFragment1();
//    }
//
//    public void tabbar2(View view){
//
//        initFragment2();
//    }


//    public void loginButton(View view){
//
//        SharedPreferences share = getSharedPreferences(fileName, MODE_PRIVATE); //实例化
//        SharedPreferences.Editor editor = share.edit(); //使处于可编辑状态
//        editor.putInt("loginid", 0); //设置保存的数据
//        editor.commit(); //提交数据保存
//
//        initFragment1();
//
//        Log.i("charge loginid", "2222");
//
//    }


    public void oneClick(View view){

//        Toast.makeText(MainActivity.this, "会员管理", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
        intent.putExtra("cashier", 0);
        startActivity(intent);

    }

    public void twoClick(View view){

//        Toast.makeText(MainActivity.this, "会员集点", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
        intent.putExtra("cashier", 1);
        startActivity(intent);

    }

    public void thirdClick(View view){

//        Toast.makeText(MainActivity.this, "会员红包", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
        intent.putExtra("cashier", 2);
        startActivity(intent);
    }

    public void fourClick(View view){

//        Toast.makeText(MainActivity.this, "会员通知", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
        intent.putExtra("cashier", 3);
        startActivity(intent);
    }

    public void fiveClick(View view){

//        Toast.makeText(MainActivity.this, "会员储值", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
        intent.putExtra("cashier", 4);
        startActivity(intent);
    }

    public void sexClick(View view){

//        Toast.makeText(MainActivity.this, "点餐", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
        intent.putExtra("cashier", 5);
        startActivity(intent);
    }

    public void eightClick(View view){

        Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
        intent.putExtra("cashier", 6);
        startActivity(intent);
    }


    //    银行卡初始化
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
            ServiceObject.getInstance().IWizarPayment = wizarPayment;
        }

        public void onServiceConnected(ComponentName name, IBinder service)
        {
            wizarPayment = IWizarPayment.Stub.asInterface(service);
            ServiceObject.getInstance().IWizarPayment = wizarPayment;
        }
    };

    public void bindService()
    {
        try
        {
            //绑定支付服务
//            bindService(new Intent(IWizarPayment.class.getName()), serviceConnection, Context.BIND_AUTO_CREATE);

            Intent intent = new Intent(IWizarPayment.class.getName());
            intent.setPackage("com.wizarpos.paymentrouter");
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

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
            Log.i("charge unlogin", "退出连接");

            /* 解除和支付服务的绑定 */
            unbindService(serviceConnection);
            wizarPayment = null;
        }
    }


}
