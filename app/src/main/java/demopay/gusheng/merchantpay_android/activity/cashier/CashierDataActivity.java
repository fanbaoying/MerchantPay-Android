package demopay.gusheng.merchantpay_android.activity.cashier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;

import demopay.gusheng.merchantpay_android.R;
import demopay.gusheng.merchantpay_android.bean.CashierDataBean;
import demopay.gusheng.merchantpay_android.bean.JudgeBean;

/**
 * Created by fby on 2017/9/27.
 */

public class CashierDataActivity extends Activity {

    private CashierDataBean cashierDataBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashierdata);

        ImageButton backButton = (ImageButton) findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton editButton = (ImageButton) findViewById(R.id.btn_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent = new Intent(CashierDataActivity.this, EditCashierActivity.class);
                String cashierDataBeanjson = new Gson().toJson(cashierDataBean);
                mIntent.putExtra("cashier", cashierDataBeanjson);
                startActivity(mIntent);

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

        TextView contentName = (TextView) findViewById(R.id.content_name);
        contentName.setText(realName);

        TextView contentPhone = (TextView) findViewById(R.id.content_phone);
        contentPhone.setText(mobile);

        TextView contentNumber = (TextView) findViewById(R.id.content_number);
        contentNumber.setText(userCode);

        TextView contentType = (TextView) findViewById(R.id.content_type);
        contentType.setText(userStatus);


    }

}
