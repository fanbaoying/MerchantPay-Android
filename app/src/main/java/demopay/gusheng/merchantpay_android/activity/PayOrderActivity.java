package demopay.gusheng.merchantpay_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import demopay.gusheng.merchantpay_android.R;

/**
 * 支付成功、失败界面
 */
public class PayOrderActivity extends BaseActivity {
    @InjectView(R.id.tv_apo_sure)
    TextView tvApoSure;
    @InjectView(R.id.rl_apo_title)
    RelativeLayout rlApoTitle;
    @InjectView(R.id.iv_apo_state)
    ImageView ivApoState;
    @InjectView(R.id.tv_apo_state)
    TextView tvApoState;
    @InjectView(R.id.tv_apo_info)
    TextView tvApoInfo;
    @InjectView(R.id.tv_apo_infoDetail)
    TextView tvApoInfoDetail;
    @InjectView(R.id.bt_apo_look)
    Button btApoLook;
    @InjectView(R.id.bt_apo_restart)
    Button btApoRestart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payorder);
        ButterKnife.inject(this);
        //处理结果
        Intent mIntent = getIntent();
        Boolean flag = mIntent.getBooleanExtra("flag", false);
        dealResult(flag);

        tvApoInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void dealResult(boolean b) {
        //处理相关的业务逻辑
        //处理页面的UI
        changUI(b);
    }

    private void changUI(boolean b) {
        if (b) {
            ivApoState.setImageResource(R.drawable.success);
            tvApoState.setText("恭喜你,支付成功");
            tvApoInfo.setText("温馨提示");
            tvApoInfoDetail.setText(R.string.pay_info_detail);
            btApoLook.setVisibility(View.INVISIBLE);
            btApoRestart.setVisibility(View.INVISIBLE);
        }else {
            ivApoState.setImageResource(R.drawable.fail);
            tvApoState.setText("支付失败,请重新支付");
            tvApoInfo.setText("订单信息");
            tvApoInfoDetail.setText(R.string.pay_info);
            btApoLook.setVisibility(View.VISIBLE);
            btApoRestart.setVisibility(View.VISIBLE);
        }

    }

    @OnClick({R.id.bt_apo_look, R.id.bt_apo_restart,R.id.tv_apo_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_apo_look:
                Toast.makeText(this, "查看订单", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_apo_restart:
                Toast.makeText(this, "重新支付", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_apo_sure:
                startActivity(new Intent(this,MainActivity.class));
                break;
        }
    }
}
