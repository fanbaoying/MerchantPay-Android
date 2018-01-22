package demopay.gusheng.merchantpay_android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import demopay.gusheng.merchantpay_android.R;

/**
 * Created by fby on 2017/11/21.
 */

public class MainMenuActivity extends Activity {

    int[] TitleImgCashier = new int[]{R.drawable.onefirstedit,R.drawable.onesecondedit,R.drawable.onethirdedit,R.drawable.onesexdeit,R.drawable.onefouredit,R.drawable.onefiveedit,R.drawable.onesevenedit};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        ImageButton backButton = (ImageButton) findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initData();

    }

    private void initData() {

        //处理结果
        Intent mIntent = getIntent();
        int count = mIntent.getIntExtra("cashier", 0);

        ImageView imageView = (ImageView) findViewById(R.id.menu_imageview);
        imageView.setBackgroundResource(TitleImgCashier[count]);

    }

}
