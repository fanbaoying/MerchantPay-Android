package demopay.gusheng.merchantpay_android.Fragement;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import demopay.gusheng.merchantpay_android.R;
import demopay.gusheng.merchantpay_android.activity.OrderDetailActicity;
import demopay.gusheng.merchantpay_android.adapter.LookAdapter;
import demopay.gusheng.merchantpay_android.bean.JudgeBean;
import demopay.gusheng.merchantpay_android.bean.OrderListBean;
import demopay.gusheng.merchantpay_android.bean.OrderNumBean;

import static android.content.Context.MODE_PRIVATE;
import static demopay.gusheng.merchantpay_android.util.OrderRequest.getJson;
import static demopay.gusheng.merchantpay_android.util.OrderRequest.orderURL;

/**
 * Created by fby on 2017/9/12.
 */

public class LookFragement extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View view = null;

    private ListView listView = null;
    String[] productNames = null;
    String[] payWayCodes = null;
    String[] amounts = null;
    String[] createTimes = null;
    String[] trxNos = null;
    String[] statuss = null;

    String[] productName = null;
    String[] payWayCode = null;
    String[] amount = null;
    String[] createTime = null;
    String[] trxNo = null;
    String[] status = null;

    List<Map<String, Object>> list = null;

    private TextView payAmount = null;
    private TextView payNum = null;
    private TextView refundAmount = null;
    private TextView refundNum = null;

    private Button typebtn = null;
    private Button orderbtn = null;
    private Button timebtn = null;

    private Button wxButton = null;
    private Button alipayButton = null;
    private Button uppayButton = null;
    private Button allButton = null;

    private Button orderpayBtn = null;
    private Button ordernopayBtn = null;
    private Button orderallBtn = null;

    private ImageView typeimg = null;
    private ImageView orderimg = null;
    private ImageView timeimg = null;

    private PopupWindow typepopup = null;
    private PopupWindow orderpopup = null;
    private PopupWindow timepopup = null;

    private SwipeRefreshLayout swipeLayout;

    int pagenumber = 1;

    //    status 状态  1：支付成功   8:退款成功 空：全部
    //    size 当前页面多少数据
    //    no   当前在第几页
    //    payType  支付方式  2002 支付宝 2001 微信 0002银联

    String orderstatus = "";
    String payType = "";


//    private static final String Statics_URL = "http://pay.soongrande.com/agree-pay-app-gateway/api/app1/getStatics";

//    private static final String Statics_URL = "http://139.198.3.34:8082/api/pay/core/getAllPay";

    private  String token;
    private  String merchantNo;


    private static final String fileName = "logintext";//定义保存的文件的名称

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            if (msg.what == 1) {

                OrderListBean charge = (OrderListBean) msg.obj;

                if (pagenumber == 1) {

                    productNames = charge.getResult().getProductName();
                    payWayCodes = charge.getResult().getPayWayCode();
                    amounts = charge.getResult().getAmount();
                    createTimes = charge.getResult().getCreateTime();
                    trxNos = charge.getResult().getTrxNo();
                    statuss = charge.getResult().getStatus();

                }else {
                    String[] productNameone = charge.getResult().getProductName();
                    productNames = new String[productNameone.length + productName.length];
                    productName = productNameone;

                    String[] payWayCodeone = charge.getResult().getPayWayCode();
                    payWayCodes = new String[payWayCodeone.length + payWayCode.length];
                    payWayCode = payWayCodeone;

                    String[] amountone = charge.getResult().getAmount();
                    amounts = new String[amountone.length + amount.length];
                    amount = amountone;

                    String[] createTimeone = charge.getResult().getCreateTime();
                    createTimes = new String[createTimeone.length + createTime.length];
                    createTime = createTimeone;

                    String[] trxNoone = charge.getResult().getTrxNo();
                    trxNos = new String[trxNoone.length + trxNo.length];
                    trxNo = trxNoone;

                    String[] statusone = charge.getResult().getStatus();
                    statuss = new String[statusone.length + status.length];
                    status = statusone;

                }
                swipeLayout.setRefreshing(false);

                list = getData();
                listView.setAdapter(new LookAdapter(getActivity(), list));
            }else if (msg.what == 11){

                pagenumber = pagenumber-1;

                JudgeBean judgeBean = (JudgeBean) msg.obj;

                String message = judgeBean.getMsg();

                new AlertDialog.Builder(getActivity()).setTitle("系统提示")
                        .setMessage(message).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }else if (msg.what == 2){
                OrderNumBean orderNumber = (OrderNumBean) msg.obj;

                String payAmounts = orderNumber.getResult().getPayAmount();
                String payNums = orderNumber.getResult().getPayNum();
                String refundAmounts = orderNumber.getResult().getRefundAmount();
                String refundNums = orderNumber.getResult().getRefundNum();

                payAmount.setText(payAmounts);
                payNum.setText(payNums);
                refundAmount.setText(refundAmounts);
                refundNum.setText(refundNums);
            }else if (msg.what == 21){

//                JudgeBean judgeBean = (JudgeBean) msg.obj;
//
//                String message = judgeBean.getMessage();
//
//                new AlertDialog.Builder(getActivity()).setTitle("系统提示")
//                        .setMessage(message).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).show();
            }
        }
    };

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragement_look,container,false);
//        view = inflater.inflate(R.layout.fragement_look, null);

        listView = (ListView) view.findViewById(R.id.list);


        SharedPreferences share = getActivity().getSharedPreferences(fileName,
                MODE_PRIVATE);

        token = share.getString("token", null);
        merchantNo = share.getString("merchantNo", null);

        Log.i("charge token", token);


        networkData(orderstatus, "1", payType);

        payAmount = (TextView) view.findViewById(R.id.number1);
        payNum = (TextView) view.findViewById(R.id.number2);
        refundAmount = (TextView) view.findViewById(R.id.number3);
        refundNum = (TextView) view.findViewById(R.id.number4);

        typebtn = (Button) view.findViewById(R.id.button1);
        orderbtn = (Button) view.findViewById(R.id.button2);
        timebtn = (Button) view.findViewById(R.id.button3);

        typeimg = (ImageView) view.findViewById(R.id.image5);
        orderimg = (ImageView) view.findViewById(R.id.image6);
        timeimg = (ImageView) view.findViewById(R.id.image7);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.secondactivity_list);

//设置刷新时动画的颜色，可以设置4个
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);

        swipeLayout.setOnRefreshListener(this);//设置刷新监听器


        //    上拉加载
        /**
         *  上拉更多
         */
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:

                        String no = String.valueOf(pagenumber + 1);

                        Log.i("charge down", "上拉走了");
                        Log.i("charge down", no);

                        networkData(orderstatus, no, payType);

//                        if (listView.getLastVisiblePosition() == mViewModel.getList().size() - 1) {
//                            if (!mViewModel.isComplete()) {
//                                fetchNewData(FIRST);
//                            }
//                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });




//        networkData(orderstatus, "1", payType);

//        订单数量，订单价格
//        Runnable orderNumRunnable = new Runnable() {
//            @Override
//            public void run() {
//                String data = null;
//
//                try {
//                    data = orderURL(status, "8", no, paytype, token);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                Log.i("charge orderNum", data);
//                JudgeBean judgeBean = new Gson().fromJson(data, JudgeBean.class);
//                String respCode = judgeBean.getCode();
//                if (respCode.equals("0")){
//
//                    OrderNumBean charge = new Gson().fromJson(data, OrderNumBean.class);
//
//                    Message msg = new Message();
//                    msg.what = 2;
//                    msg.obj = charge;
//                    mHandler.sendMessage(msg);
//
//                }else {
//
//                    Message msg = new Message();
//                    msg.what = 21;
//                    msg.obj = judgeBean;
//                    mHandler.sendMessage(msg);
//
//                }
//
//
//            }
//        };
//        // 必须异步调用
//        Thread orderNumThread = new Thread(orderNumRunnable);
//        orderNumThread.start();
        return view;
    }

    public void onRefresh() {

        networkData(orderstatus, "1", payType);

        Log.i("charge onRefresh", "onRefresh走了");

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 3000);
    }



    private List<Map<String, Object>> getData() {

        list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < productNames.length; i++) {

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", productNames[i]);

            if (payWayCodes[i].equals("2002")){
                map.put("type", "支付宝");
            }else if (payWayCodes[i].equals("2001")){
                map.put("type", "微信");
            }else if (payWayCodes[i].equals("0002")){
                map.put("type", "银联");
            }
            map.put("reminder", statuss[i]);
            map.put("money", amounts[i]);
            map.put("content", createTimes[i]);

            list.add(map);
        }

        initTypePopupWindow();
        initOrderPopupWindow();
        initTimePopupWindow();

        return list;
    }

    //    status 状态  1：支付成功   8:退款成功 空：全部
    //    size 当前页面多少数据
    //    no   当前在第几页
    //    payType  支付方式  2002 支付宝 2001 微信 0002银联

    private void networkData(final String status, final String no, final String paytype){

        Runnable orderRunnable = new Runnable() {
            @Override
            public void run() {
                String data = null;
                try {
                    data = orderURL(status, "8", no, paytype, token);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.i("charge response", data);

                JudgeBean judgeBean = new Gson().fromJson(data, JudgeBean.class);

                String respCode = judgeBean.getCode();
                if (respCode.equals("0")){

                    OrderListBean charge = new Gson().fromJson(data, OrderListBean.class);
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = charge;
                    mHandler.sendMessage(msg);

                }else {

                    Message msg = new Message();
                    msg.what = 11;
                    msg.obj = judgeBean;
                    mHandler.sendMessage(msg);

                }
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(orderRunnable);
        payThread.start();

    }


    //渠道类型
    private void initTypePopupWindow() {
        View v = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_second_popupwindow, null);

        wxButton = (Button) v.findViewById(R.id.wxbutton);
        alipayButton = (Button) v.findViewById(R.id.alipaybutton);
        uppayButton = (Button) v.findViewById(R.id.uppaybutton);
        allButton = (Button) v.findViewById(R.id.allbutton);

        wxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = "2001";
                networkData(orderstatus, "1", payType);

            }
        });

        alipayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = "2002";
                networkData(orderstatus, "1", payType);
            }
        });

        uppayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = "0002";
                networkData(orderstatus, "1", payType);
            }
        });

        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = "";
                networkData(orderstatus, "1", payType);
            }
        });

        typepopup = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);


        typepopup.setFocusable(true);
        //该属性设置为true则你在点击屏幕的空白位置也会退出
        typepopup.setTouchable(true);
        typepopup.setOutsideTouchable(true);
        typepopup.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));

        typepopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onDismiss() {
                Log.i("charge dismiss", "消失了");
//                typeimg.setRotation(360);
                typeimg.setRotation(360);
            }
        });

    }


    //订单状态
    private void initOrderPopupWindow() {
        View v = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_second_orderpopup, null);

        orderpayBtn = (Button) v.findViewById(R.id.paybutton);
        ordernopayBtn = (Button) v.findViewById(R.id.refundbutton);
        orderallBtn = (Button) v.findViewById(R.id.allPaybutton);

        orderpayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderstatus = "1";
                networkData(orderstatus, "1", payType);
            }
        });

        ordernopayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderstatus = "8";
                networkData(orderstatus, "1", payType);
            }
        });

        orderallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderstatus = "";
                networkData(orderstatus, "1", payType);
            }
        });

        orderpopup = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);


        orderpopup.setFocusable(true);
        //该属性设置为true则你在点击屏幕的空白位置也会退出
        orderpopup.setTouchable(true);
        orderpopup.setOutsideTouchable(true);
        orderpopup.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));

        orderpopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onDismiss() {
                Log.i("charge dismiss", "消失了");
                orderimg.setRotation(360);
            }
        });

    }
    //下单时间
    private void initTimePopupWindow() {
        View v = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_second_timepopup, null);
        timepopup = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);


        timepopup.setFocusable(true);
        //该属性设置为true则你在点击屏幕的空白位置也会退出
        timepopup.setTouchable(true);
        timepopup.setOutsideTouchable(true);

        timepopup.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));

        timepopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onDismiss() {
                Log.i("charge dismiss", "消失了");
                timeimg.setRotation(360);
            }
        });

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("charge position", String.valueOf(position));
                Intent intent = new Intent(getActivity(), OrderDetailActicity.class);
                intent.putExtra("trxNo", trxNos[position]);
                startActivity(intent);

            }
        });

        typebtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                Log.i("charge type", "渠道类型");
                typepopup.showAsDropDown(v);
                typeimg.setRotation(180);
            }
        });

        typeimg.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                typepopup.showAsDropDown(v);
                typeimg.setRotation(180);
            }
        });

        orderbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                Log.i("charge order", "订单状态");
                orderpopup.showAsDropDown(v);
                orderimg.setRotation(180);
            }
        });

        orderimg.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                orderpopup.showAsDropDown(v);
                orderimg.setRotation(180);
            }
        });

        timebtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                Log.i("charge order", "下单时间");
                timepopup.showAsDropDown(v);
                timeimg.setRotation(180);
            }
        });

        timeimg.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                timepopup.showAsDropDown(v);
                timeimg.setRotation(180);
            }
        });




    }

}
