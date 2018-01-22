package demopay.gusheng.merchantpay_android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import demopay.gusheng.merchantpay_android.Fragement.FirstFragement;
import demopay.gusheng.merchantpay_android.Fragement.LookFragement;
import demopay.gusheng.merchantpay_android.R;

/**
 * Created by fby on 2017/9/13.
 */

public class LookActivity extends AppCompatActivity {

    private LookFragement f1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look);

        initFragment1();

    }


    //显示第一个fragment
    private void initFragment1(){
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
        if(f1 == null){
            f1 = new LookFragement();
            transaction.add(R.id.main_look_layout, f1);
        }
//        //隐藏所有fragment
//        hideFragment(transaction);
//        //显示需要显示的fragment
//        transaction.show(f1);


        //提交事务
        transaction.commit();
    }

}
