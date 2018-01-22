package demopay.gusheng.merchantpay_android.activity;

import android.app.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import demopay.gusheng.merchantpay_android.R;

/**
 * Created by fby on 2017/11/5.
 */

public class BankLoginActivity extends Activity implements OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bankcard);

    }

    @Override
    public void onClick(View v) {

        Log.i("charge data", "点击事件走了");

    }
}
