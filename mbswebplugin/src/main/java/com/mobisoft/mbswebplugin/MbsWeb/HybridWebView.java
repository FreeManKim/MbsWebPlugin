package com.mobisoft.mbswebplugin.MbsWeb;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mobisoft.mbswebplugin.Cmd.CMD;
import com.mobisoft.mbswebplugin.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import static com.mobisoft.mbswebplugin.utils.UrlUtil.parseUrl;

/**
 * Created by jiangzhou on 16/4/13.
 */
public class HybridWebView extends WebView {

    /**
     *
     */
    private HybridWebviewListener listener;

    /**
     * url_ROOT 页面load的url
     */
    private String url_ROOT;
    /**
     * shouldInterceptRequest 拦截的url
     */
    private String url_SIR = "";
    /**
     * shouldOverrideUrlLoading 拦截的url
     */
    private String url_SOUL = "";

    /**
     * 用于记录第一次进来的url
     */
    private boolean firstComeIn = true;

    /**
     * log标签
     */
    public static final String TAG = "HybridWebView"; //log
    public AlertDialog dialog;
    /**
     * 设置 是否 在当前页面加载url
     * 是否需要goBack
     * false：不在当前页面加载url
     * true：在当前页面加载URL
     */
    public boolean islocaPage;

    /**
     * 初始化
     *
     * @param context
     */
    public HybridWebView(Context context) {
        super(context);
        init();
    }

    public HybridWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 设置自定的HybridWebviewListener监听事件
     *
     * @param listener
     */
    public void setListener(HybridWebviewListener listener) {
        this.listener = listener;
    }

    private void init() {
        // ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);// 排版适应屏幕
        this.isHardwareAccelerated();
        this.getSettings().setUseWideViewPort(true);// 可任意比例缩放
        this.getSettings().setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        this.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); // 支持通过JS打开新窗口
        this.getSettings().setSavePassword(true);
        this.getSettings().setSaveFormData(true);// 保存表单数据
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setGeolocationEnabled(true);// 启用地理定位
        this.getSettings().setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// 设置定位的数据库路径
        this.getSettings().setDomStorageEnabled(true);
        this.getSettings().setSupportMultipleWindows(true);// 新加
        this.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式

        this.setWebViewClient(new MyWebViewClient());
        this.setWebChromeClient(new MyChromeClient());

//        /**
//         * 屏幕适配:这里需要根据不同的屏幕像素密度来设置默认变焦密度，此设置常用于平板
//         */
//        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2){ // 小于4.3 api 18
//            DisplayMetrics metrics = new DisplayMetrics();
//            ((Activity)this.getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
//            int mDensity = metrics.densityDpi;
//            int width = metrics.widthPixels;
//            Log.d(TAG, "densityDpi = " + mDensity + "  width = " + width + " width" + metrics.widthPixels );
//            if (mDensity == DisplayMetrics.DENSITY_HIGH) { // 240dpi
//                this.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
//            } else if (mDensity == DisplayMetrics.DENSITY_MEDIUM) { // 160dpi
//                this.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
//            } else if(mDensity == DisplayMetrics.DENSITY_LOW) { // 120dpi
//                this.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
//            }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){ // 320dpi 5.25英寸720P (HD)
//                this.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR); // 枚举指定WebView所需的密度。FAR使看起来像100%在240dpi介质里
//                this.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS); // 排版适应内容大小
//                if (width >= 720) this.getSettings().setDefaultFontSize(24); // 设置默认字体大小：可以取1-72之间的任意值，默认16
//                else this.getSettings().setDefaultFontSize(18);
//            }else if (mDensity == DisplayMetrics.DENSITY_TV){ // 213dpi 常见平板
//                this.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
//                this.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//                this.getSettings().setDefaultFontSize(24);
//            }else{
//                this.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
//            }
//        }
    }

    /**
     * 加载url地址
     *
     * @param url
     */
    @Override
    public void loadUrl(String url) {
        if (firstComeIn) {
            this.url_ROOT = url;
            firstComeIn = false;
        }
        Log.i(TAG, "url_ROOT:" + url_ROOT + "  loadUrl:" + url);
        super.loadUrl(url);
    }


    /***
     * webview与swiperefreshlayout滑动冲突 重写webview每次按下的时候，
     * 如果在0,0坐标，让它滚动到0,1，这样就会告诉SwipeRefreshLayout他还在滑动，
     * 就不会触发刷新事件了
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("weizhi", "===" + event.getY() + ",getScrollY==" + getScrollY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (this.getScrollY() <= 0)
                    this.scrollTo(0, 1);
                break;
            case MotionEvent.ACTION_UP:
//                if(this.getScrollY() > 1)
//                this.scrollTo(0,-1);
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.i("weizhi", "===" + event.getY() + ",getScrollY==" + getScrollY());
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i("weizhi", "===" + event.getY() + ",getScrollY==" + getScrollY());

        return super.dispatchTouchEvent(event);
    }

    /**
     * WebViewClient重写shouldOverrideUrlLoading，
     * shouldInterceptRequest方法，来配合拦截action 和cmd命令
     */
    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 在点击请求的是链接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边。
            // 这个函数我们可以做很多操作，比如我们读取到某些特殊的URL，于是就可以不打开地址，取消这个操作，进行预先定义的其他操作，
            // 这对一个程序是非常必要的。
            Log.i(TAG, "shouldOverrideUrlLoading==url_SOUL:" + url);
//            boolean b = Looper.myLooper() == Looper.getMainLooper();
//            Log.e(TAG, "测试："+(b?b+"当前是主线程":b+"子线程"));

            url_SOUL = url;
            // 解决多次加载，死循环打开nextpage 的问题
            if (url_SOUL.equals(url_ROOT) || url_SOUL.equals(url_SIR)) return true;

            // 页面的跳转
            if (url.startsWith("http")) {
                Map<String, String> param = parseUrl(url);
                if (param.containsKey("action")) {
                    String value = param.get("action");
                    if (value != null) {
                        if (listener != null) {
                            if (value.equals(CMD.action_closePage)) {
                                return listener.onClosePage(url, value);
                            } else if (value.equals(CMD.action_closePageAndRefresh)) {//禁用返回自动下拉刷新，解决卡死问题
                                // return listener.onClosePage(url,value);
                                return listener.onClosePageReturnMain(url, value);
                            } else if (TextUtils.equals(value, CMD.action_localPage)) {// 本页面新加载html
                                setIslocaPage(false);
                                loadUrl(TextUtils.substring(url, 0, url.indexOf("?action")));
                                return false;
                            } else if (TextUtils.equals(value, CMD.action_hideNavigation)) {// 加载轻量级webView
                                listener.onLightweightPage(url, value);
                                return true;
                            } else {
                                return listener.onNextPage(url, value);
                            }
                        }
                        return false;
                    }
                }
            }
            // 页面的功能函数
            if (url.startsWith("kitapps")) {
                listener.onCommand(view,url);
                return true;
//                Map<String, String> param = parseUrl(url);
//                String parameter = param.get("para");
//                String function = param.get("callback");
//                Pattern p = Pattern.compile("\\//(.*?)\\?");//正则表达式，取=和|之间的字符串，不包括=和|
//                Matcher m = p.matcher(url);
//                String cmd = null;
//                while (m.find()) {
//                    cmd = m.group();
//                    break; // 菜单里的url带"?"的话会导致cmd取值不对，所以只拿第一次的cmd
//                }
//                if (cmd != null) {
//                    if (listener != null) {
//                        cmd = cmd.substring(2, cmd.length() - 1);
//                        listener.onCommand(cmd, parameter, function);
//                    }
//                }
//                return true;
            }
            return false;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, final String url) {
            // 部分系统(android 4.4 以下)，手机或者html不走shouldOverrideUrlLoading方法
            Log.i(TAG, "shouldInterceptRequest==url_SIR:" + url);
            url_SIR = url;

            // 解决多次加载，死循环打开nextpage 的问题
            if (url_SIR.equals(url_ROOT) || url_SIR.equals(url_SOUL))
                return super.shouldInterceptRequest(view, url);

            // 页面的跳转
            if (url.startsWith("http")) {
                Map<String, String> param = parseUrl(url);
                if (param.containsKey("action")) {
                    String value = param.get("action");
                    if (value != null) {
                        if (listener != null) {
                            if (value.equals(CMD.action_closePage)) {
                                listener.onClosePage(url, value);
                            } else if (value.equals(CMD.action_closePageAndRefresh)) {
                                //  listener.onClosePage(url, value);
                                listener.onClosePageReturnMain(url, value);   //禁用返回自动下拉刷新，解决卡死问题
                            } else if (TextUtils.equals(value, CMD.action_localPage)) {// 本页面跳转
                                setIslocaPage(false);
                                HybridWebView.this.post(new Runnable() {// 同一个线程
                                    @Override
                                    public void run() {
                                        loadUrl(TextUtils.substring(url, 0, url.indexOf("?action")));
                                    }
                                });
                                return null;
                            } else if (TextUtils.equals(value, CMD.action_hideNavigation)) {// 加载轻量级webView
                                listener.onLightweightPage(url, value);
                                WebResourceResponse res = null;
                                try {
                                    PipedOutputStream out = new PipedOutputStream();
                                    PipedInputStream in = new PipedInputStream(out);
                                    // 返回一个错误的响应，让连接失败，webview内就不跳转了(解决本activity的webview跳转问题)
                                    res = new WebResourceResponse("html", "utr-8", in);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    return res;
                                }
                                return res;
                            } else {
                                return listener.onSIRNextPage(url, value);
                            }
                            return null;
                        }
                    }
                } else if (param.containsKey("action")) {

                }
            }
            // 页面的功能函数
            if (url.startsWith("kitapps")) {
                listener.onCommand(view,url);
                return null;
//                Map<String, String> param = parseUrl(url);
//                String parameter = param.get("para");
//                String function = param.get("callback");
//                Pattern p = Pattern.compile("\\//(.*?)\\?");//正则表达式，取=和|之间的字符串，不包括=和|
//                Matcher m = p.matcher(url);
//                String cmd = null;
//                while (m.find()) {
//                    cmd = m.group();
//                }
//                if (cmd != null) {
//                    if (listener != null) {
//                        cmd = cmd.substring(2, cmd.length() - 1);
//                        listener.onCommand(cmd, parameter, function);
//                        return null;
//                    }
//                }
            }

            return super.shouldInterceptRequest(view, url);

        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            //(报告错误信息)
            Log.e(TAG, "onReceivedSslError==Error:" + error.toString());
            handler.proceed();
        }


        @Override
        public void onLoadResource(WebView view, String url) {
            //在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次。
            Log.i(TAG, "onLoadResource==url:" + url);
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //这个事件就是开始载入页面调用的，通常我们可以在这设定一个loading的页面，告诉用户程序在等待网络响应。
            Log.i(TAG, "onPageStarted==url:" + url);
            if (firstComeIn) {
                firstComeIn = false;
            }
            super.onPageStarted(view, url, favicon);
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            //在页面加载结束时调用。同样道理，我们知道一个页面载入完成，于是我们可以关闭loading 条，切换程序动作。
            Log.i(TAG, "onPageFinished==url:" + url);
            listener.onWebPageFinished();

        }

    }
    /**
     * WebChromeClient
     * 重写 onReceivedTitle，onJsAlert方法
     * 来获取标题，修改alert弹窗
     */
    class MyChromeClient extends WebChromeClient {

        // 自动获取网页标题的方法
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (listener != null) {
                listener.onTitle(0, title);
            }
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(view
                    .getContext());
            builder.setTitle("提示").setMessage(message)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            result.confirm();
                        }
                    });

            //禁止响应按back键的事件
            builder.setCancelable(false);
            dialog = builder.create();
            dialog.show();
            result.cancel();
            return true;
        }

        //         // 扩充缓存的容量(网络)
//        @Override
//        public void onReachedMaxAppCacheSize(long spaceNeeded,long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater){
//            quotaUpdater.updateQuota(spaceNeeded * 2);
//        }
//
//        @Override
//        public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota, long estimatedDatabaseSize, long totalQuota, WebStorage.QuotaUpdater quotaUpdater) {
////            super.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);
//            quotaUpdater.updateQuota(estimatedDatabaseSize * 2);
//        }
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            String message = consoleMessage.message();
            Log.i(TAG, "consoleMessage:" + message);
            if (message.contains("ReferenceError")) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(HybridWebView.this.getContext());
                builder.setTitle("ReferenceError").setMessage(message)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                //禁止响应按back键的事件
                builder.setCancelable(false);
                dialog = builder.create();
                dialog.show();
            }
            return super.onConsoleMessage(consoleMessage);
        }

    }

    /**
     * 回调方法
     *
     * @param function    功能函数
     * @param inParameter 入参
     * @param data        数据
     */
    public void excuteJSFunction(String function, String inParameter, String data) {
//        String josn = String.format("javascript:" + function + "(%s)", parameter);
        JSONObject myJsonObject = null;
        try {
            //将字符串转换成jsonObject对象
            myJsonObject = new JSONObject();
            myJsonObject.put(inParameter, data);
        } catch (JSONException e) {

        }
        function = function.replace("(val)", "(" + myJsonObject.toString() + ")");

//        String josn = String.format("javascript:" + function);
//        String josn = String.format("javascript:" + function + "%s)", myJsonObject);

//        Log.i(TAG, "excuteJSFunction()==function:" + function + "   parameter:" + inParameter + "   data:" + data + "   url_josn:" + josn);
        loadUrl(Utils.functionFormat(function, myJsonObject));
    }

    /**
     * 相册回调方法
     *
     * @param function    功能函数
     * @param inParameter 入参
     * @param data        数据
     */
    public void excuteJSFunction(String function, String inParameter, List<String> data) {
//        String josn = String.format("javascript:" + function + "(%s)", parameter);
        JSONObject myJsonObject = null;
        try {
            myJsonObject = new JSONObject();
            //将字符串转换成jsonObject对象
            JSONArray imageJSONArray = new JSONArray();
            for (String url : data) {
                JSONObject image = new JSONObject();
                image.put("url", url);
                imageJSONArray.put(image);
            }
            myJsonObject.put(inParameter, imageJSONArray);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
        String josn = String.format("javascript:" + function + "('%s')", myJsonObject);
        Log.i(TAG, "excuteJSFunction()==function:" + function + "   parameter:" + inParameter + "   data:" + data + "   url_josn:" + josn);
        loadUrl(josn);
    }

    /**
     * cheng 城市回掉方法
     *
     * @param function    回掉方法
     * @param inParameter 地区和位置的区分 code
     * @param str0        省 provJson
     * @param str1        市 cityJson
     * @param str2        区 areaJson
     */
    public void excuteJSFunction(String function, String inParameter, String str0, String str1, String str2) {
//        String josn = String.format("javascript:" + function + "(%s)", parameter);
        JSONObject myJsonObject = null;
        try {
            //将字符串转换成jsonObject对象
            myJsonObject = new JSONObject();
            if (inParameter.equals(Utils.IN_PARAMETER_FOR_CITY)) {
                JSONObject provJsonObject = new JSONObject();
                String[] provarr = str0.split(",");
                provJsonObject.put(Utils.IN_PARAMETER_FOR_CODE, provarr[0]);
                provJsonObject.put(Utils.IN_PARAMETER_FOR_NAME, provarr[1]);

                JSONObject cityJsonObject = new JSONObject();
                String[] cityarr = str1.split(",");
                cityJsonObject.put(Utils.IN_PARAMETER_FOR_CODE, cityarr[0]);
                cityJsonObject.put(Utils.IN_PARAMETER_FOR_NAME, cityarr[1]);

                JSONObject areaJsonObject = new JSONObject();
                String[] areaarr = str2.split(",");
                areaJsonObject.put(Utils.IN_PARAMETER_FOR_CODE, areaarr[0]);
                areaJsonObject.put(Utils.IN_PARAMETER_FOR_NAME, areaarr[1]);

                myJsonObject.put(Utils.IN_PARAMETER_FOR_PROV, provJsonObject);
                myJsonObject.put(Utils.IN_PARAMETER_FOR_CITY, cityJsonObject);
                myJsonObject.put(Utils.IN_PARAMETER_FOR_AREA, areaJsonObject);
            } else {
                myJsonObject.put(Utils.IN_PARAMETER_FOR_ADDR, str0);
                myJsonObject.put(Utils.IN_PARAMETER_FOR_LAT, str1);
                myJsonObject.put(Utils.IN_PARAMETER_FOR_LON, str2);
            }


        } catch (JSONException e) {

        }
        function = function.replace("(val)", "(" + myJsonObject.toString() + ")");

//        String josn = String.format("javascript:" + function);
        String josn = String.format("javascript:" + function + "%s)", myJsonObject);

        Log.i(TAG, "excuteJSFunction()===function:" + function + "   parameter:" + inParameter + "   url_josn:" + josn);
        loadUrl(josn);
    }

    /**
     * 获得用户的account
     *
     * @return 用户的account
     */
    public String getAccount(String url) {
        String param = url.substring(url.indexOf("?") + 1);
        param = param.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
        try {
            param = URLDecoder.decode(param, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String str = "";
        String[] list2 = param.split("account");
        if (list2.length >= 2) {
            String[] list3 = list2[1].split("\"");
            if (list3.length >= 2) {
                str = list3[1];
            }
        }
        Log.i(TAG, "getAccount()===用户account：" + str);
        return str;
    }

    /**
     * 设置 是否 当前页面加载
     *
     * @param islocaPage
     */
    public void setIslocaPage(boolean islocaPage) {
        Log.e("oye", islocaPage + "  setIslocaPage");

        this.islocaPage = islocaPage;
    }

    /**
     * 获取 是否需要goBack
     */
    public Boolean needGoback() {
        return islocaPage;
    }



}
