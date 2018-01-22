package demopay.gusheng.merchantpay_android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import demopay.gusheng.merchantpay_android.R;

/**
 * Created by fby on 2017/9/8.
 */

public class RegisterActivity extends Activity {

//    public WebView myWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Log.i("charge main", "zoule");

        ImageButton backButton = (ImageButton) findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


//        WebView myWebView = (WebView) findViewById(R.id.registerWebview);
//
//        myWebView.loadUrl("https://www.wenjuan.in/s/BfMNbe/");
//        WebSettings webSettings = myWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//
////        myWebView.loadUrl("https://www.baidu.com/");
//        myWebView.setWebViewClient(new WebViewClient() {
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//
//                Log.i("charge title", "zoule");
//                view.loadUrl(url);
//                return true;
//            }
//
//        });
//
//        myWebView.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                super.onReceivedTitle(view, title);
//                Log.v("charge title", "title:"+title);
//            }
//        });


        final TextView txtTitle = (TextView) findViewById(R.id.txt_title);
        final WebView webView = (WebView)findViewById(R.id.registerWebview);

        webView.loadUrl("https://www.wenjuan.in/s/BfMNbe/");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.d("ANDROID_LAB", "TITLE=" + title);
                txtTitle.setText(title);
            }

        };
        // 设置setWebChromeClient对象
        webView.setWebChromeClient(wvcc);

        // 创建WebViewClient对象
        WebViewClient wvc = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
                webView.loadUrl(url);
                // 消耗掉这个事件。Android中返回True的即到此为止吧,事件就会不会冒泡传递了，我们称之为消耗掉
                return true;
            }
        };
        webView.setWebViewClient(wvc);


    }

}
