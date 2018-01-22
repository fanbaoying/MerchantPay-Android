package demopay.gusheng.merchantpay_android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Timer;
import java.util.TimerTask;

import demopay.gusheng.merchantpay_android.Fragement.LoginFragement;
import demopay.gusheng.merchantpay_android.R;

/**
 * Created by fby on 2017/9/11.
 */

public class ChargepsdActivity extends Activity {

    private EditText editText;
    private CheckBox checkBox;

    private EditText phonetext;

    private Button timeButton;

    private Button okpasswordbutton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chargepsd);

        ImageButton backButton = (ImageButton) findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        editText = (EditText) findViewById(R.id.newpassword);
        checkBox = (CheckBox) findViewById(R.id.CheckBox);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    //如果选中，显示密码
                    editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    checkBox.setText("隐藏");
                }else{
                    //否则隐藏密码
                    editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    checkBox.setText("显示");

                }

            }
        });


        phonetext = (EditText) findViewById(R.id.phonetext);
        timeButton = (Button) findViewById(R.id.timebutton);
        //new倒计时对象,总共的时间,每隔多少秒更新一次时间
        final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000,1000);

        //给Button设置点击时间,触发倒计时
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCountDownTimer.start();
            }
        });


        okpasswordbutton = (Button) findViewById(R.id.okpasswordbutton);
        okpasswordbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(ChargepsdActivity.this, MainActivity.class);
                startActivity(mIntent);
            }
        });


    }

    //复写倒计时
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            timeButton.setClickable(false);
            timeButton.setText(l/1000+"秒");

            Log.i("time", "zoule");

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            timeButton.setText("重新获取");
            //设置可点击
            timeButton.setClickable(true);
        }
    }



}
