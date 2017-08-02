package com.mobisoft;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mobisoft.MbsDemo.R;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        WebView we_test = (WebView) findViewById(R.id.we_test);
        we_test.setWebChromeClient(new WebChromeClient());
        we_test.setWebViewClient(new WebViewClient());
        WebSettings settings = we_test.getSettings();
//        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(true);// 可任意比例缩放
//        settings.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
//        settings.setJavaScriptCanOpenWindowsAutomatically(true); // 支持通过JS打开新窗口
//        settings.setSaveFormData(true);// 保存表单数据
        settings.setJavaScriptEnabled(true);
//        settings.setGeolocationEnabled(true);// 启用地理定位
//        settings.setGeolocationDatabasePath("/data/data/org.itri.html5webview/
// databases/");// 设置定位的数据库路径
//        settings.setDomStorageEnabled(true);
//        settings.setSupportMultipleWindows(true);// 新加
//        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式
//        we_test.isHardwareAccelerated();
//        // 设置可以使用localStorage
//        settings.setDomStorageEnabled(true);
//        // 应用可以有缓存
//        settings.setAppCacheEnabled(true);
//        settings.setAllowFileAccess(true);
//        settings.setAppCachePath(we_test.getContext().getCacheDir().getAbsolutePath());
//        // 缓存模式
//        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

//        we_test.setVerticalScrollbarOverlay(true); //指定的垂直滚动条有叠加样式
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
//        settings.setUseWideViewPort(true);//扩大比例的缩放 //为图片添加放大缩小功能
//        settings.setLoadWithOverviewMode(true);
//        settings.setDisplayZoomControls(false);
//        settings.setSupportZoom(true); // 支持缩放
//        settings.setBuiltInZoomControls(true);// 设置出现缩放工具
//        settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);// 默认缩放模式

        we_test.loadUrl("http://cathy.mobisoft.com.cn/cathay/agent/test.html?action=nextPage");
    }
}
