package demopay.gusheng.merchantpay_android.Fragement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wizarpos.paymentrouter.aidl.IWizarPayment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import demopay.gusheng.merchantpay_android.R;
import demopay.gusheng.merchantpay_android.activity.LookActivity;
import demopay.gusheng.merchantpay_android.activity.cashier.CashierActivity;
import demopay.gusheng.merchantpay_android.activity.shop.ShopCollectionActivity;
import demopay.gusheng.merchantpay_android.adapter.MyViewPagerAdapter;
import demopay.gusheng.merchantpay_android.adapter.SecondAdapter;
import demopay.gusheng.merchantpay_android.adapter.ServiceObject;
import demopay.gusheng.merchantpay_android.util.ToastUtil;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by fby on 2017/9/1.
 */

public class SecondFragement extends Fragment {

    private MyViewPagerAdapter pagerAdapter;// ViewPager适配器
    private ViewPager pager;
    private List<View> list;// 保存ImageView的List
    private ListView listView = null;

    private TextView merchantName;
    private TextView merchantCode;

    private static final String fileName = "logintext";//定义保存的文件的名称
    private LoginFragement f3;
    private FirstFragement f1;
    private SecondFragement f2;

    private String username;
    private String shortName;
    private String userStatus = "1";

    BottomNavigationView bottomNavigationView;

    int[] TitleImg = new int[]{R.drawable.fourmessage2,R.drawable.fourmessage2,R.drawable.foursweep1,R.drawable.fourcashier, R.drawable.fourcard3,R.drawable.fourserve4,R.drawable.fourrecommend5,R.drawable.fourservice6,R.drawable.fourset7};
    String[] Content = new String[]{"店铺签约信息","签到","店铺收款码","收银员管理","我的银行卡","我的服务","推荐给好友","联系客服","退出登录"};

    int[] TitleImgCashier = new int[]{R.drawable.fourmessage2,R.drawable.foursweep1,R.drawable.fourset7};
    String[] ContentCashier = new String[]{"签到","店铺收款码","退出登录"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_second,container,false);
//        View view = inflater.inflate(R.layout.fragment_second, null);

        listView = (ListView) view.findViewById(R.id.firstlist);

//        // ListView头部View
//        View viewView = LayoutInflater.from(getContext()).inflate(R.layout.first_header, null);
//        pager = (ViewPager) view.findViewById(R.id.viewpager);
//        pagerAdapter = new MyViewPagerAdapter(list);
//        pager.setAdapter(pagerAdapter);
//
//        // addHeaderView一定要在setAdapter前调用
//        listView.addHeaderView(viewView);

        List<Map<String, Object>> list=getData();
        listView.setAdapter(new SecondAdapter(getActivity(), list));


        //第一次初始化首页默认显示第一个fragment
        SharedPreferences share = getActivity().getSharedPreferences(fileName,
                MODE_PRIVATE);
        username = share.getString("username", null);
        shortName = share.getString("shortName", null);
        userStatus = share.getString("userStatus", "1");

        merchantName = (TextView) view.findViewById(R.id.merchantName);
        merchantName.setText(username);

        merchantCode = (TextView) view.findViewById(R.id.merchantCode);
        merchantCode.setText(shortName);




        return view;

    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        if (userStatus.equals("2")) {

            for (int i = 0; i < TitleImgCashier.length; i++) {

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("imageview", TitleImgCashier[i]);
                map.put("nameTitle", ContentCashier[i]);

                list.add(map);
            }
        }else {

            for (int i = 0; i < TitleImg.length; i++) {

                Map<String, Object> map = new HashMap<String, Object>();
//                Log.i("charge btn", String.valueOf(TitleImg[i]));
                map.put("imageview", TitleImg[i]);
                map.put("nameTitle", Content[i]);

                list.add(map);
            }
        }


        return list;
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Log.i("charge position", String.valueOf(position));

                if (userStatus.equals("2")) {

                    if (position == 2) {

                        loginOut();

                    }else if (position == 1) {

                        startActivity(new Intent(getActivity(), ShopCollectionActivity.class));

                    }else if (position == 0) {

                        signIn();

                    }

                }else {

                    if (position == 8) {

                        loginOut();

                    }else if (position == 3) {

                        startActivity(new Intent(getActivity(), CashierActivity.class));

                    }else if (position == 2) {

                        startActivity(new Intent(getActivity(), ShopCollectionActivity.class));

                    }else if (position == 1) {

                        signIn();

                    }

                }



            }
        });

    }

//    退出登录
    private void loginOut() {

        SharedPreferences share = getActivity().getSharedPreferences(fileName, MODE_PRIVATE); //实例化
        SharedPreferences.Editor editor = share.edit(); //使处于可编辑状态
        editor.putInt("loginid", 1); //设置保存的数据
        editor.commit(); //提交数据保存

        initFragment3();

//        Log.i("charge loginid", "3333");

    }

//  签到
    private void signIn() {

        JSONObject param = new JSONObject();
        String ret = "ok";
        try
        {

            Log.i("charge data", "判断走了");

//                        IWizarPayment wizarPayment = ServiceObject.getInstance().IWizarPayment;

            if (ServiceObject.getInstance().IWizarPayment == null) {
                Log.i("charge data login", "服务未连接");

            }else {

                param.put("AppID", "");
                param.put("AppName", "");

                param.put("OptCode", "01");
                param.put("OptPass", "0000");
                ret = ServiceObject.getInstance().IWizarPayment.login(param.toString());

                Log.i("charge data login", "result:"+ret);
                ToastUtil.showShort(getActivity(), "签到成功");
            }

        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }

    }

    //显示第二个fragment
    private void initFragment3(){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        if(f3 == null){
            f3 = new LoginFragement();
        }
        transaction.replace(R.id.main_frame_layout,f3);

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);

        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        bottomNavigationView.getMenu().getItem(1).setChecked(false);
        bottomNavigationView.setVisibility(View.GONE);

        transaction.commit();
    }

}
