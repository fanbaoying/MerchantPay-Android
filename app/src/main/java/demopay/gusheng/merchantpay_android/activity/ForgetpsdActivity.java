package demopay.gusheng.merchantpay_android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import demopay.gusheng.merchantpay_android.R;

/**
 * Created by fby on 2017/9/8.
 */

public class ForgetpsdActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpsd);

        ImageButton backButton = (ImageButton) findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button nextbutton = (Button) findViewById(R.id.phoneNext);
        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(ForgetpsdActivity.this, ChargepsdActivity.class);
                startActivity(mIntent);
            }
        });

    }
}
