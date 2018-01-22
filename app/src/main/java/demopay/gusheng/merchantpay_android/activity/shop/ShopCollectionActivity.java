package demopay.gusheng.merchantpay_android.activity.shop;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import demopay.gusheng.merchantpay_android.Fragement.LoginFragement;
import demopay.gusheng.merchantpay_android.R;
import demopay.gusheng.merchantpay_android.activity.MainActivity;
import demopay.gusheng.merchantpay_android.bean.ShopCollectionBean;
import demopay.gusheng.merchantpay_android.bean.ShopCollectionDataBean;
import demopay.gusheng.merchantpay_android.util.ImgUtils;
import demopay.gusheng.merchantpay_android.zxing.encoding.EncodingHandler;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static demopay.gusheng.merchantpay_android.util.OrderRequest.ShopCollectionJson;

/**
 * Created by fby on 2017/10/10.
 */

public class ShopCollectionActivity extends FragmentActivity implements EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_CODE_SAVE_IMG = 10;
    private static final String TAG = "MainActivity";
    private Context mContext;

    private LoginFragement f3;

    BottomNavigationView bottomNavigationView;

//    private static final String ShopCollection_URL = "http://139.198.3.34:8082/api/generate/page/queryQrcodeCollections";
    private static final String ShopCollection_URL = "http://139.198.3.34:8082/api/generate/getQrcodeApp";

    private static final String fileName = "logintext";//定义保存的文件的名称

    private  String token;
    private  String merchantNo;

    private ImageView ShopQrCodes;

    private Bitmap downloadBmp = null;

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            if (msg.what == 1) {

                ShopCollectionBean ShopCollectionBean = (ShopCollectionBean) msg.obj;
                String qrcodeContent = ShopCollectionBean.getData();

                ShopQrCodes = (ImageView) findViewById(R.id.ShopQrCodes);

                //将字符串转换成Bitmap类型
                Bitmap bitmap=null;
                try {
                    byte[]bitmapArray;
                    bitmapArray= Base64.decode(qrcodeContent, Base64.DEFAULT);
                    bitmap=BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
                    downloadBmp = bitmap;
                    ShopQrCodes.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                try {
//                    //获取输入的文本信息
//                    String str = qrcodeContent;
//                    if(str != null && !"".equals(str.trim())){
//                        //根据输入的文本生成对应的二维码并且显示出来
//                        Bitmap mBitmap = EncodingHandler.createQRCode(str.toString(), 500);
//                        if(mBitmap != null){
////                            Toast.makeText(this,"二维码生成成功！",Toast.LENGTH_SHORT).show();
//                            ShopQrCodes.setImageBitmap(mBitmap);
//                        }
//                    }else{
////                        Toast.makeText(this,"文本信息不能为空！",Toast.LENGTH_SHORT).show();
//                    }
//                } catch (WriterException e) {
//                    e.printStackTrace();
//                }

            }else if (msg.what == 2) {

                ShopCollectionBean ShopCollectionBean = (ShopCollectionBean) msg.obj;
                String message = ShopCollectionBean.getMsg();

//                Toast.makeText(ShopCollectionActivity.this, message, Toast.LENGTH_SHORT).show();

                new AlertDialog.Builder(ShopCollectionActivity.this)
                        .setTitle("提示")
                        .setMessage(message)
                        .setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                SharedPreferences share = getSharedPreferences(fileName, MODE_PRIVATE); //实例化
                                SharedPreferences.Editor editor = share.edit(); //使处于可编辑状态
                                editor.putInt("loginid", 1); //设置保存的数据
                                editor.commit(); //提交数据保存

                                initFragment3();

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();


            }
        }
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shopcollection);

        ImageButton backButton = (ImageButton) findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //第一次初始化首页默认显示第一个fragment
        SharedPreferences share = super.getSharedPreferences(fileName,
                MODE_PRIVATE);
        token = share.getString("token", null);
        merchantNo = share.getString("merchantNo", null);

        Log.i("charge token", token);

        Runnable orderRunnable = new Runnable() {
            @Override
            public void run() {
                String data = null;

                try {
                    data = ShopCollectionJson(ShopCollection_URL, token,merchantNo);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.i("charge response", data);

                ShopCollectionBean ShopCollectionBean = new Gson().fromJson(data, ShopCollectionBean.class);

                String code = ShopCollectionBean.getCode();

                Log.i("charge code", code);
//
                if (code.equals("0")){

                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = ShopCollectionBean;
                    mHandler.sendMessage(msg);

                }else{

                    Log.i("charge code", code);
                    Log.i("charge code", "网络请求失败");
                    Message msg = new Message();
                    msg.what = 2;
                    msg.obj = ShopCollectionBean;
                    mHandler.sendMessage(msg);

                }
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(orderRunnable);
        payThread.start();


        mContext = this;
        Button saveButton = (Button) findViewById(R.id.save_btn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (downloadBmp == null){

                    Toast.makeText(ShopCollectionActivity.this, "暂无图片，请重新获取", Toast.LENGTH_SHORT).show();

                }else {

                    requestPermission();
                }


            }
        });


    }

    /**
     * 请求读取sd卡的权限
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //读取sd卡的权限
            String[] mPermissionList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (EasyPermissions.hasPermissions(mContext, mPermissionList)) {
                //已经同意过
                saveImage();
            } else {
                //未同意过,或者说是拒绝了，再次申请权限
                EasyPermissions.requestPermissions(
                        this,  //上下文
                        "保存图片需要读取sd卡的权限", //提示文言
                        REQUEST_CODE_SAVE_IMG, //请求码
                        mPermissionList //权限列表
                );
            }
        } else {
            saveImage();
        }
    }


    //保存图片
    private void saveImage() {


//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.fj);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), );


        Bitmap bitmap = ((BitmapDrawable) ShopQrCodes.getDrawable()).getBitmap();

//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ShopQrCodes.getId());

//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.fj);
        boolean isSaveSuccess = ImgUtils.saveImageToGallery(mContext, bitmap);
        if (isSaveSuccess) {
            Toast.makeText(mContext, "保存图片成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "保存图片失败，请稍后重试", Toast.LENGTH_SHORT).show();
        }
    }

    //授权结果，分发下去
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        //跳转到onPermissionsGranted或者onPermissionsDenied去回调授权结果
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    //同意授权
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        Log.i(TAG, "onPermissionsGranted:" + requestCode + ":" + list.size());
        saveImage();
    }

    //拒绝授权
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.i(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            //打开系统设置，手动授权
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            //拒绝授权后，从系统设置了授权后，返回APP进行相应的操作
            Log.i(TAG, "onPermissionsDenied:------>自定义设置授权后返回APP");
            saveImage();
        }
    }


    //显示第二个fragment
    private void initFragment3(){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if(f3 == null){
            f3 = new LoginFragement();
        }
        transaction.replace(R.id.main_frame_layout,f3);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        bottomNavigationView.getMenu().getItem(1).setChecked(false);
        bottomNavigationView.setVisibility(View.GONE);

        transaction.commit();
    }


}
