package demopay.gusheng.merchantpay_android.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import android.view.View.OnClickListener;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.ConnectException;
import java.util.List;
import java.util.Map;

import demopay.gusheng.merchantpay_android.R;
import demopay.gusheng.merchantpay_android.activity.cashier.CashierActivity;
import demopay.gusheng.merchantpay_android.bean.CashierDataBean;
import demopay.gusheng.merchantpay_android.bean.CashierListBean;
import demopay.gusheng.merchantpay_android.bean.JudgeBean;
import demopay.gusheng.merchantpay_android.util.Switch;
import demopay.gusheng.merchantpay_android.util.ToastUtil;

import static android.content.Context.MODE_PRIVATE;
import static demopay.gusheng.merchantpay_android.util.OrderRequest.getCashierJson;
import static demopay.gusheng.merchantpay_android.util.Request.putJsonCashier;

/**
 * Created by fby on 2017/9/26.
 */

public class CashierAdapter extends BaseAdapter implements OnClickListener {

    private Context context;
    private final List<CashierDataBean> lists;
//    private  CashierDataBean item;
//    private ToggleButton mToggleButton;

    private String dataStr = null;

    private  String token;
    private  String merchantNo;

    private static final String fileName = "logintext";//定义保存的文件的名称

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            if (msg.what == 1) {

                ToastUtil.showShort(context, "修改成功");
            }else if (msg.what == 11) {

                CashierListBean cashierListBean = (CashierListBean) msg.obj;

                String message = cashierListBean.getMsg();

                ToastUtil.showShort(context, message);

            }else if (msg.what == 404){

                ToastUtil.showShort(context, "网络未链接");
            }else if (msg.what == 406){

                ToastUtil.showShort(context, "网络请求出错");
            }

        }
    };


    public CashierAdapter(Context context, List<CashierDataBean> list) {
        this.context = context;
        this.lists = list;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public CashierDataBean getItem(int position) {
        return (CashierDataBean) lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SharedPreferences share = context.getSharedPreferences(fileName,
                MODE_PRIVATE);

        token = share.getString("token", null);
        merchantNo = share.getString("merchantNo", null);


        final CashierAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new CashierAdapter.ViewHolder();
            convertView = View.inflate(context, R.layout.list_cashier, null);
            viewHolder.nameTitle = (TextView) convertView.findViewById(R.id.nameTitle);
            viewHolder.phoneTitle = (TextView) convertView.findViewById(R.id.phoneTitle);
            viewHolder.switchButton = (Switch) convertView.findViewById(R.id.switchid);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CashierAdapter.ViewHolder) convertView.getTag();
        }

        final CashierDataBean item = getItem(position);

//        Log.i("charge realname", item.getRealName());
//        Log.i("charge getUsername", item.getUsername());

        viewHolder.nameTitle.setText((String)item.getRealName());
        viewHolder.phoneTitle.setText((String)item.getUsername());

        if (item.getUserStatus().equals("0")) {

            viewHolder.switchButton.close();

        }else {

            viewHolder.switchButton.open();
        }

        viewHolder.switchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.switchButton.isOpen()) {

                    Log.i("charge response", "打开");
                    viewHolder.switchButton.close();

                    item.setUserStatus("1");
                    switchUserStatus(item);

                }else {

                    Log.i("charge response", "关闭");

                    viewHolder.switchButton.open();
                    item.setUserStatus("0");
                    switchUserStatus(item);
                }
            }
        });


        return convertView;
    }

    static class ViewHolder {

        TextView nameTitle;
        TextView phoneTitle;
        Switch switchButton;

    }


    //响应按钮点击事件,调用子定义接口，并传入View
    @Override
     public void onClick(View v) {

//         mCallback.click(v);

     }


     private void switchUserStatus(final CashierDataBean item) {

         Runnable orderRunnable = new Runnable() {
             @Override
             public void run() {
                 String data = null;
                 try {

                     data = putJsonCashier(item, token);

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

                     JudgeBean judgeBean = new Gson().fromJson(data, JudgeBean.class);

                     String respCode = judgeBean.getCode();
                     if (respCode.equals("0")){

                         Message msg = new Message();
                         msg.what = 1;
                         mHandler.sendMessage(msg);

                     }else {

                         Message msg = new Message();
                         msg.what = 11;
                         msg.obj = judgeBean;
                         mHandler.sendMessage(msg);

                     }

                 }

             }
         };
         // 必须异步调用
         Thread payThread = new Thread(orderRunnable);
         payThread.start();

     }



}




//        Log.i("charge position", String.valueOf(position));
////添加监听事件
//        viewHolder.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                if(isChecked){
//
//
//                    Log.i("charge loginid", "已开启");
//
//                }else{
//
//                    Log.i("charge loginid", "已关闭");
//                }
//
//            }
//        });