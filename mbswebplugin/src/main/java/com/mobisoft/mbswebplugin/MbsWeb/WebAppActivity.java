package com.mobisoft.mbswebplugin.MbsWeb;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mobisoft.mbswebplugin.ClickEvent.DisDialog;
import com.mobisoft.mbswebplugin.Cmd.CMD;
import com.mobisoft.mbswebplugin.Cmd.CmdrBuilder;
import com.mobisoft.mbswebplugin.Cmd.ResultListener;
import com.mobisoft.mbswebplugin.Entity.BottomItem;
import com.mobisoft.mbswebplugin.Entity.MeunItem;
import com.mobisoft.mbswebplugin.Entity.Task;
import com.mobisoft.mbswebplugin.Entity.TopMenu;
import com.mobisoft.mbswebplugin.IProxyCallback;
import com.mobisoft.mbswebplugin.IProxyPortListener;
import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.Voide.EduMediaPlayer;
import com.mobisoft.mbswebplugin.adapter.TaskAdapter;
import com.mobisoft.mbswebplugin.base.AppConfing;
import com.mobisoft.mbswebplugin.base.BaseWebActivity;
import com.mobisoft.mbswebplugin.helper.FunctionConfig;
import com.mobisoft.mbswebplugin.proxy.server.ProxyService;
import com.mobisoft.mbswebplugin.proxy.server.SettingProxy;
import com.mobisoft.mbswebplugin.refresh.SwipeRefreshLayout;
import com.mobisoft.mbswebplugin.utils.ActivityCollector;
import com.mobisoft.mbswebplugin.utils.DisplayUtil;
import com.mobisoft.mbswebplugin.utils.NetworkUtils;
import com.mobisoft.mbswebplugin.utils.SDCardUtils;
import com.mobisoft.mbswebplugin.utils.SharedPreferUtil;
import com.mobisoft.mbswebplugin.utils.ToastUtil;
import com.mobisoft.mbswebplugin.utils.UUIDTools;
import com.mobisoft.mbswebplugin.utils.UrlUtil;
import com.mobisoft.mbswebplugin.utils.Utils;
import com.mobisoft.mbswebplugin.view.ActionSheetDialog;
import com.mobisoft.mbswebplugin.view.AlertDialog;
import com.mobisoft.mbswebplugin.view.ImageBrowserActivty.ImageBrowserActivty;
import com.mobisoft.mbswebplugin.view.LockPattern.LockPatternUtils;
import com.mobisoft.mbswebplugin.view.SingleSeletPopupWindow;
import com.mobisoft.mbswebplugin.view.TitleMenuPopupWindow;
import com.mobisoft.mbswebplugin.view.TopMenuPopupWindowActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mobisoft.mbswebplugin.utils.UrlUtil.parseUrl;
import static com.mobisoft.mbswebplugin.utils.Utils.IMAGE_FROM_CAMERA;
import static com.mobisoft.mbswebplugin.view.ImageBrowserActivty.ImageBrowserActivty.IMAGE_RIGHTMEN;
import static com.mobisoft.mbswebplugin.view.ImageBrowserActivty.ImageBrowserActivty.IMAGE_URL;
import static java.lang.System.in;

//import org.xutils.x;

/**
 * native html 混合式开发库
 */
public class WebAppActivity extends BaseWebActivity implements View.OnClickListener, WebAppInterface,
        HybridWebviewListener, SwipeRefreshLayout.OnRefreshListener {


    /**
     * 耐克 申请权限
     */
    public static final int NICK_REQUEST_CAMERA_CODE = 105;
    /**
     * 调用相册
     */
    int SELECT_PIC = 1;

    /**
     * 启动相机 耐克
     */
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 300;
    /**
     * 启动相册 耐克
     */
    public static final int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 200;

    /**
     * 启动通讯的请求码
     */
    public static final int CONTACTS_CODE = 10008;
    /**
     * 启动扫描二维码传递回掉方法 Key
     **/
    public static final String FUNCTION = "function";
    /**
     * 启动扫描二维码传递参数  Key
     **/
    public static final String PARAMTER = "paramter";
    /***
     * 下拉刷新 延时取消刷新（等待initpage方法执行结束）
     */
    public static final int DELAY_MILLIS = 400;
    /**
     * TYPE_WEB 按返回键的类型 调用web View.goback()方法
     */
    public static final String TYPE_WEB = "pageWeb";
    /**
     * TYPE_ACTIVITY 按返回键的类型调用 activity.finish()方法
     */
    public static final String TYPE_ACTIVITY = "activity";
//    public static final int REQUEST_CODE_Album = 102;
    /**
     * 返回上一页布局
     */
    protected LinearLayout mLl_back;
    /**
     * title布局
     */
    public LinearLayout ll_head_title;
    /**
     * 标题
     */
    public TextView mTv_head_title;
    /**
     * 右标题
     */
    protected TextView tv_head_right;
    /**
     * 左标题
     */
    protected TextView tv_head_left;
    /**
     * 刷新控件
     */
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    /**
     * 自定义hybridwebview
     */
    protected HybridWebView mWebViewExten;
    /**
     * url
     */
    protected String urlStr;
    /**
     * 工号
     */
    protected String accountStr;

    /**
     * 单选标志
     */
    protected int lv_single_Item = -1;
    /**
     * 右标题
     */
    protected LinearLayout ll_right;

    /**
     * 头布局
     */
    public LinearLayout ll_head;

    /***
     * 单选菜单
     */
    protected SingleSeletPopupWindow mSingleSeletPopupWindow;

    /***
     * 右上角菜单
     */
    protected TopMenuPopupWindowActivity mTopMenuPopWin;
    /***
     * title菜单
     */
    protected TitleMenuPopupWindow mTitleMenuPopWin; //

    /**
     * 请求打开相机
     */
    protected final int REQUEST_CODE_CAMERA = 1000;
    /**
     * 请求打开相册
     */
    protected final int REQUEST_CODE_GALLERY = 1001;
    /**
     * 请求打开相机 返回图片转 base64
     */
    protected final int REQUEST_CODE_CAMERA_B = 1002;
    /**
     * 请求打开相册 返回 图片转 base64
     */
    protected final int REQUEST_CODE_GALLERY_B = 1003;
    /**
     * openNextWebActivity  resultCode
     **/
    public static final int INTENT_REQUEST_CODE = 3;
    /**
     * 启动地图页面的 resultCode
     */
    public static final int REQUEST_CODE_MAP = 4;
    /**
     * 启动扫描二维码页面的 resultCode
     */
    public static final int INTENT_REQUEST_QRCODE = 6;
    /**
     * 创建手势解锁 resultCode
     */
    public static final int INTENT_REQUEST_GESTURE = 7;

    /**
     * 是否刷新！
     */
    public static final String REFRESH = "Refresh"; //
    /***
     * 刷新的url！
     */
    public static final String REFRESH_URL = "RefreshUrl"; //
    /**
     * log日志标签
     */
    protected static final String TAG = "WebAppActivity"; //
    /**
     * 初次进来webview 的 url （必须要）
     */
    public static final String URL = "url";
    /**
     * 初始化webview时用户account （必须要）
     */
    public static final String ACCOUNT = "account";
    /**
     * title 颜色 （必须要）
     */
    public static final String TITLECOLOR = "titleColor";
    /**
     * 初始化webview时avtivity进入动画 （非必须，有默认值）
     */
    public static final String ANIMRES = "AnimRes";
    /**
     * title左右两边菜单模式 （非必须，有默认值）
     */
    public static final String SHOWMOUDLE = "showmodel";
    /**
     * 搜索页面没有title模式 （非必须，有默认值）
     */
    public static final String SHOWMOUDLESEARCHPAGE = "showModelSearchPage";
    /**
     * 沉浸式菜单栏颜色 （非必须，有默认值）
     */
    public static final String SYSTEM_BAR_COLOR = "SystemBarColor";
    /**
     * 标题中间文字颜色颜色 （非必须，有默认值）
     */
    public static final String TITLE_CENTER_TEXT_COLOR = "TitleCenterTextColor";
    /**
     * 标题左边文字颜色颜色 （非必须，有默认值）
     */
    public static final String TITLE_LEFT_TEXT_COLOR = "TitleLeftTextColor";
    /**
     * 标题右边文字颜色颜色 （非必须，有默认值）
     */
    public static final String TITLE_RIGHT_TEXT_COLOR = "TitleRightTextColor";
    /**
     * 标题返回图片 （非必须，有默认值）
     */
    public static final String ICON_BACK = "IconBack";
    /**
     * 标题中间图片 （非必须，有默认值）
     */
    public static final String ICON_TITLE_CENTER = "IconTitleCenter";
    /**
     * 标题右边图片 （非必须，有默认值）
     */
    public static final String ICON_TITLE_RIGHT = "IconTitleRight";
    /**
     * 是否显示左边“返回”文字 （非必须，有默认值）
     */
    public static final String IS_LEFT_TEXT_SHOW = "IsLeftTextShow";
    /**
     * 是否显示左边“返回”图片 （非必须，有默认值）
     */
    public static final String IS_LEFT_ICON_SHOW = "IsLeftIconShow";
    /**
     * 是否显示沉浸式菜单栏 （非必须，有默认值）
     */
    public static final String IS_SYSTEM_BAR_SHOW = "IsSystemBarShow";
    /**
     * 是否支持刷新 （非必须，有默认值）
     */
    public static final String IS_REFRESH_ENABLE = "IsRefreshEnable";
    /**
     * activity是否支持打开方式 （非必须，有默认值）
     */
    public static final String IS_TRANSITION_MODE_ENABLE = "isTransitionModeEnable";
    /**
     * activity打开方式 （非必须，有默认值）
     */
    public static final String IS_TRANSITION_MODE = "isTransitionMode";

    /**
     * 是否开始隐藏导航栏 （非必须，有默认值）false 为 不隐藏，true 为隐藏导航栏
     */
    public static final String IS_HIDENAVIGATION = "isHideNavigation";

    /**
     * 进度条
     */
    public ProgressDialog mProgressDialog;
    /**
     * 右上角菜单标志位  默认true  移除第一个选项 将其显示再右边标题处
     */
    protected boolean farstMune = true; //
    /**
     * title菜单标志位  移除第一个选项 将其显示再标题处
     */
    protected boolean farstTitleMune = true; // title菜单标志位
    /**
     * 左右菜单 false 不显示 菜单
     */
    protected boolean showModel = false; // 左右菜单
    /***
     * 没有title 的搜索
     */
    protected boolean showModelSearchPage = false; // 没有title 的搜索
    /**
     * title 颜色
     */
    protected int titleColor = 0; // title 颜色
    /**
     * avtivity进入动画
     */
    protected int animRes = 0; // avtivity进入动画
    /***
     * 沉浸式菜单栏颜色
     */
    protected int systemBarColor = 0; // 沉浸式菜单栏颜色
    /**
     * 标题中间文字颜色颜色
     */
    protected int titleCenterTextColor = 0; // 标题中间文字颜色颜色
    /***
     * 标题左边文字颜色颜色
     */
    protected int titleLeftTextColor = 0; // 标题左边文字颜色颜色
    /**************************
     * 标题右边文字颜色颜色
     ***************************/
    protected int titleRightTextColor = 0; // 标题右边文字颜色颜色
    /**
     * 标题返回图片
     */
    protected int iconBack = 0; // 标题返回图片
    /**************************
     * 标题中间图片
     ***************************/
    protected int iconTitleCenter = 0; // 标题中间图片
    /***
     * 标题右边图片
     */
    protected int iconTitleRight = 0; // 标题右边图片
    /**
     * 是否显示左边“返回”文字
     */
    protected boolean isLeftTextShow = false; // 是否显示左边“返回”文字
    /**
     * 是否显示左边“返回”图片
     */
    protected boolean isLeftIconShow = false; // 是否显示左边“返回”图片
    /**
     * 是否显示沉浸式菜单栏
     */
    protected boolean isSystemBarShow = true; // 是否显示沉浸式菜单栏
    /**
     * 是否支持刷新
     */
    protected boolean isRefreshEnable = false; // 是否支持刷新
    /***
     * activity是否支持打开方式
     */
    protected boolean isTransitionModeEnable = true; // activity是否支持打开方式
    /***
     * activity打开方式
     */
    protected String isTransitionMode = "RIGHT"; // activity打开方式

    /**
     * 位置经度
     */
    protected double latitude;
    /**
     * 位置维度
     */
    protected double longitude;
    /**
     * userLocationAddress 当前位置  格式：江苏省，南京市
     */
    protected String userLocationAddress;
    /**
     * 定位之后的  回掉方法
     */
    protected String areaFunction;
    /**
     * 获取相片的 回掉方法
     */
    protected String picFunction;
    /**
     * 改变登录方式 比如手势解锁 的回掉方法
     */
    protected String functionNameStr;
    /**
     * 左边返回图片按钮
     */
    protected ImageView iv_head_left;
    /**
     * 菜单图标
     */
    protected ImageView img_right;
    /**
     * title菜单图标
     */
    protected ImageView iv_head_title_menu;

    /***
     * 右边菜单 选项
     */
    protected List<MeunItem> listMenuItem = new ArrayList<>();
    /**
     * 中间菜单选项
     */
    protected List<MeunItem> listTitleMenuItem = new ArrayList<>();

    /**
     * 用于记录第一次onPageFinished进来的
     */
    private boolean firstComeIn = true;
    /**
     * 是否 清除过任务栈 false： 没有
     ***/
    public boolean isClearTask = false;
    /**
     * h5携带参数
     */
    private String mParamter;
    /**
     * 消息的广播接受者
     */
    protected ReceiveBroadCast receiveBroadCast;
    /***
     * 是否隐藏 导航栏 true :隐藏  false：显示
     */
    protected boolean is_hidenavigation;

    /**
     * 是否设置过 h5命令中标题 false:未设置命令标题
     */
    private boolean issetTitle = false;

    /**
     * 获取网页标题
     */
    private String urlTitle;


    /**
     * url 返回的可选总数
     */
    private int limtCount = 0;

    /**
     * 缓存路径
     */
    private static final String APP_CACAHE_DIRNAME = "/webcache";
    /**
     * url 返回的选择张数
     */
    private int selectCount = 0;
    /**
     * 代理端口号
     */
    private int PORT = 8182;
    /**
     * 搜索头布局
     */
    private LinearLayout ll_search;
    /**
     * 搜索头布局 相关
     */
    private TextView search_back, search_cancel;
    /**
     * 搜索头布局 相关
     */
    private ImageView search_sousuo;
    /**
     * 搜索头布局 相关
     */
    private EditText search_editext;
    /**
     * 搜索头布局 相关
     */
    private LinearLayout search_content, search_view;
    /**
     * seeting头布局
     */
    private LinearLayout setting_ll;
    /**
     * seeting头布局 相关
     */
    private TextView setting_back;
    /**
     * seeting头布局 相关
     */
    private RadioGroup rg_all;
    private LinearLayout ll_center_normal;
    /**
     * 上传文件的类型  ！
     */
    private String uploadType = "";
    /**
     * 拍照路径
     */
    private String picFileFullName;

    /**
     * onActivityResult 监听事件
     */
    private ResultListener resultListener;

    /**
     * 设置监听事件
     *
     * @param result
     */
    public void setResult(ResultListener result) {
        this.resultListener = result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_app);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            if (!hasPerm)
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 99901);
        }
        ActivityCollector.addActivity(this);
        initViews();
        initData();
        initEvents();
        if (isTransitionModeEnable) {
            if (FunctionConfig.TransitionMode.LEFT.name().equals(isTransitionMode)) {
                overridePendingTransition(R.anim.left_in, R.anim.out_to_right);
            } else if (FunctionConfig.TransitionMode.RIGHT.name().equals(isTransitionMode)) {
                overridePendingTransition(R.anim.right_in, R.anim.out_to_left);
            } else if (FunctionConfig.TransitionMode.TOP.name().equals(isTransitionMode)) {
                overridePendingTransition(R.anim.top_in, R.anim.top_out);
            } else if (FunctionConfig.TransitionMode.BOTTOM.name().equals(isTransitionMode)) {
                overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
            } else if (FunctionConfig.TransitionMode.SCALE.name().equals(isTransitionMode)) {
                overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
            } else if (FunctionConfig.TransitionMode.FADE.name().equals(isTransitionMode)) {
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }
    }

    /***
     * 初始化view
     */
    protected void initViews() {
        mLl_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_head_title = (LinearLayout) findViewById(R.id.ll_head_title);
        mTv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_head_left = (TextView) findViewById(R.id.tv_head_left);
        tv_head_right = (TextView) findViewById(R.id.tv_head_right);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mWebViewExten = (HybridWebView) findViewById(R.id.webViewExten);
        ll_right = (LinearLayout) findViewById(R.id.ll_right);
        ll_head = (LinearLayout) findViewById(R.id.ll_head);
        iv_head_left = (ImageView) findViewById(R.id.iv_head_left);
        img_right = (ImageView) findViewById(R.id.img_right);
        iv_head_title_menu = (ImageView) findViewById(R.id.iv_head_title_menu);

        //nike
        ll_search = (LinearLayout) findViewById(R.id.search_ll);
        search_back = (TextView) findViewById(R.id.search_back);
        search_cancel = (TextView) findViewById(R.id.search_cancel);
        search_sousuo = (ImageView) findViewById(R.id.search_sousuo);
        search_editext = (EditText) findViewById(R.id.search_editext);
        search_content = (LinearLayout) findViewById(R.id.search_content);
        search_view = (LinearLayout) findViewById(R.id.search_view);

        setting_ll = (LinearLayout) findViewById(R.id.setting_ll);
        setting_back = (TextView) findViewById(R.id.setting_back);

        rg_all = (RadioGroup) findViewById(R.id.rg_all);
        ll_center_normal = (LinearLayout) findViewById(R.id.ll_center_normal);

        enableHTML5AppCache();
    }

    /**
     * 初始化数据
     */
    protected void initData() {
        urlStr = getIntent().getStringExtra(URL);
        accountStr = getIntent().getStringExtra(ACCOUNT);
        AppConfing.ACCOUNT = accountStr;
        titleColor = getIntent().getIntExtra(TITLECOLOR, 0);
        showModel = getIntent().getBooleanExtra(SHOWMOUDLE, false);
        showModelSearchPage = getIntent().getBooleanExtra(SHOWMOUDLESEARCHPAGE, false);
        animRes = getIntent().getIntExtra(ANIMRES, R.anim.in_from_right);
        systemBarColor = getIntent().getIntExtra(SYSTEM_BAR_COLOR, Color.parseColor(getString(R.string.man_system_bar_color)));
        titleCenterTextColor = getIntent().getIntExtra(TITLE_CENTER_TEXT_COLOR, Color.WHITE);
        titleLeftTextColor = getIntent().getIntExtra(TITLE_LEFT_TEXT_COLOR, Color.WHITE);
        titleRightTextColor = getIntent().getIntExtra(TITLE_RIGHT_TEXT_COLOR, Color.WHITE);
        iconBack = getIntent().getIntExtra(ICON_BACK, R.drawable.back);
        iconTitleCenter = getIntent().getIntExtra(ICON_TITLE_CENTER, R.drawable.ic_gf_triangle_arrow);
        iconTitleRight = getIntent().getIntExtra(ICON_TITLE_RIGHT, R.drawable.ic_add_black_48dp);
        isLeftTextShow = getIntent().getBooleanExtra(IS_LEFT_TEXT_SHOW, false);
        isLeftIconShow = getIntent().getBooleanExtra(IS_LEFT_ICON_SHOW, false);
        isSystemBarShow = getIntent().getBooleanExtra(IS_SYSTEM_BAR_SHOW, true);
        isRefreshEnable = getIntent().getBooleanExtra(IS_REFRESH_ENABLE, false);
        isTransitionModeEnable = getIntent().getBooleanExtra(IS_TRANSITION_MODE_ENABLE, true);
        isTransitionMode = getIntent().getStringExtra(IS_TRANSITION_MODE);
        is_hidenavigation = getIntent().getBooleanExtra(IS_HIDENAVIGATION, false);
//        Log.e(TAG, "==isTransitionMode:" + isTransitionMode + " enmu:" + FunctionConfig.TransitionMode.LEFT.name());
//        Log.e(TAG, "is_hidenavigation:" + is_hidenavigation);
        if (is_hidenavigation || showModel || showModelSearchPage || urlStr.equals(AppConfing.CTTQ_BASE_URL + "/tianxin/aaaao20041-test/html/index.html"))
            hideTitle();
        if (TextUtils.isEmpty(accountStr)) accountStr = "error";
        if (titleColor != 0) { // 设置title颜色 和沉浸式菜单栏
            ll_head.setBackgroundColor(titleColor);
//            if (isSystemBarShow)
//                initSystemBar(WebAppActivity.this, systemBarColor);
        }
        if (isLeftTextShow) tv_head_left.setVisibility(View.GONE);
        iv_head_left.setImageResource(iconBack);
        if (isLeftIconShow) iv_head_left.setVisibility(View.GONE);

        // 页面不可刷新
        if (isRefreshEnable || urlStr.equals(AppConfing.CTTQ_BASE_URL + "/tianxin/aaaao20041-test/html/index.html"))
            mSwipeRefreshLayout.setEnabled(false);

        // 如果url为图片链接，设置可以支持缩放
        if (urlStr.endsWith(".jpg") || urlStr.endsWith(".png") || urlStr.endsWith(".gif") || urlStr.endsWith(".bmp") || urlStr.endsWith(".psd")) {
            mWebViewExten.setVerticalScrollbarOverlay(true); //指定的垂直滚动条有叠加样式
            mWebViewExten.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
            mWebViewExten.getSettings().setUseWideViewPort(true);//扩大比例的缩放 //为图片添加放大缩小功能
            mWebViewExten.getSettings().setLoadWithOverviewMode(true);
            mWebViewExten.getSettings().setDisplayZoomControls(false);
            mWebViewExten.getSettings().setSupportZoom(true); // 支持缩放
            mWebViewExten.getSettings().setBuiltInZoomControls(true);// 设置出现缩放工具
            mWebViewExten.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);// 默认缩放模式
            mWebViewExten.getSettings().setBuiltInZoomControls(true); // 支持手势缩放
        }
        mWebViewExten.setVerticalScrollbarOverlay(true); //指定的垂直滚动条有叠加样式
        mWebViewExten.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        mWebViewExten.getSettings().setUseWideViewPort(true);//扩大比例的缩放 //为图片添加放大缩小功能
        mWebViewExten.getSettings().setLoadWithOverviewMode(true);
        mWebViewExten.getSettings().setDisplayZoomControls(false);
        mWebViewExten.getSettings().setSupportZoom(true); // 支持缩放
        mWebViewExten.getSettings().setBuiltInZoomControls(true);// 设置出现缩放工具
        mWebViewExten.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);// 默认缩放模式
        mWebViewExten.getSettings().setBuiltInZoomControls(true); // 支持手势缩放

        mWebViewExten.addJavascriptInterface(new JsControl(), "webview");


        Log.i(TAG, "url:" + urlStr);
        this.setMainUrl(urlStr);
        mProgressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("正在加载...");

        mProgressDialog.show(); // 本地不show，拦截url响应事件
    }

    /**
     * 初始化事件
     */
    protected void initEvents() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mWebViewExten.setListener(this);
//        mWebViewExten.setOnTouchListener(this);
        iv_head_left.setOnClickListener(this);
        tv_head_left.setOnClickListener(this);
        tv_head_left.setClickable(false);
        ll_head_title.setOnClickListener(this);
        ll_right.setOnClickListener(this);
        ll_head_title.setClickable(false);
        ll_right.setClickable(false);

    }


    /**
     * 设置webview缓存
     */
    private void enableHTML5AppCache() {
        //zhu.gf
        mWebViewExten.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        // 开启 DOM storage API 功能
        mWebViewExten.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        mWebViewExten.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
        Log.i(TAG, "cacheDirPath=" + cacheDirPath);
        //设置数据库缓存路径
        mWebViewExten.getSettings().setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        mWebViewExten.getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        mWebViewExten.getSettings().setAppCacheEnabled(true);
        Intent intent = new Intent(this, ProxyService.class);
        startService(intent);
        SettingProxy.setProxy(mWebViewExten, "127.0.0.1", PORT, getApplication().getClass().getName());
        mWebViewExten.setWebContentsDebuggingEnabled(true);


    }


    /**
     * 返回键 按下事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //设置回退
        //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (mWebViewExten.canGoBack() && mWebViewExten.needGoback()) {
                onFinish(TYPE_WEB);
                return true;
            } else {
                onFinish(TYPE_ACTIVITY);
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }


    /**
     * 设置应用的主页链接
     *
     * @param url 主页地址
     */
    @Override
    public void setMainUrl(String url) {
        this.urlStr = url;
        if (TextUtils.indexOf(urlStr, "http") < 0) {
            this.mWebViewExten.loadData(urlStr, "text/html", "UTF-8");
            return;
        }
        loadUrl(this.urlStr);
    }

    /**
     * 获取应用的主页链接地址
     */
    @Override
    public String getMainUrl() {
        return urlStr;
    }

    /**
     * 重新加载页面
     */
    @Override
    public void reloadApp() {
        this.mWebViewExten.reload();
    }

    /**
     * 设置标题
     *
     * @param title 标题
     * @param type  设置类型
     */
    @Override
    public void onTitle(int type, String title) {
        switch (type) {
            case CMD.type_h5Title:// 获取h5中的title
                if (!issetTitle)
                    this.mTv_head_title.setText(title);
                break;
            case CMD.type_kitappsTitle:// 获取h5 命令中的title
                issetTitle = true;
                this.mTv_head_title.setText(title);
                break;
            default:// 默认设置 h5中自带的title
                if (!issetTitle)
                    this.mTv_head_title.setText(urlTitle);
                break;
        }

    }

    /**
     * webView加载结束
     */
    @Override
    public void onWebPageFinished() {
        mWebViewExten.setEnabled(true);
        // 解决 android 5.0 以下多次调用onPageFinished的方法，多次调用initpage() 导致页面重复加载的问题
        if (firstComeIn) firstComeIn = false;
        else return;

        /** 初始化工号*/
        String json = String.format("javascript:initaccount(" + "'%s')", accountStr);
        loadUrl(json);
        /**初始化页面，调js函数必须调用*/
        String json2 = String.format("javascript:initPage(" + "'%s')", "");
        Log.e(TAG, json2);
        loadUrl(json2);

        /**初始化页面，调js函数initlocation('省份','城市')*/
        String json3 = String.format("javascript:initlocation(" + "'%s','%s')", AppConfing.LOCATION_Province, AppConfing.LOCATION_City);
        loadUrl(json3);
        /***关闭下拉刷新*/
        handler.sendEmptyMessageDelayed(2, DELAY_MILLIS);

        colseProgressDialog();  // TODO: 2017/1/3  加载框提前消失，是因为初始化的时候，关闭加载框覆盖了指令关闭缓冲框
        /**
         * TODO 解决方案
         * 自动以一个加载框，获取里面的内容，通过内容判断是否执行关闭操作！
         *
         */
    }


    @Override
    public void onCommand(WebView view, String url) {
        Message msg = new Message();
        msg.what = 5;
        msg.obj = url;
        handler.sendMessage(msg);
    }

    /**
     * 命令回调
     *
     * @param command   命令
     * @param parameter 参数
     * @param function  回写函数
     */
    @Override
    public void onCommand(String command, String parameter, String function) {
        mParamter = parameter;

        Log.e(TAG, "onCommand()==命令command:" + command + "  参数paramter:" + parameter + "  功能function:" + function);
        switch (command) {
            case CMD.cmd_cellphone:// 打电话
                Utils.getPhone(WebAppActivity.this, parameter);
                break;
            case CMD.cmd_getImage:// 获取图片
                limtCount = Utils.getlimitCount(WebAppActivity.this, parameter);
                selectCount = Utils.getSelectCount(WebAppActivity.this, parameter);
//                getPicDialog(WebAppActivity.this, Utils.IMAGE_SELECT_CAMERA_AND_PHOTOS, function
//                        , REQUEST_CODE_CAMERA, REQUEST_CODE_GALLERY, limtCount, selectCount);
                break;
            case CMD.cmd_getCamera:// 打开相机
                limtCount = Utils.getlimitCount(WebAppActivity.this, parameter);
                selectCount = Utils.getSelectCount(WebAppActivity.this, parameter);
//                getPicDialog(WebAppActivity.this, Utils.IMAGE_SELECT_CAMERA, function, REQUEST_CODE_CAMERA, REQUEST_CODE_GALLERY, limtCount, selectCount);
                break;
            case CMD.cmd_getPicture:// 打开相册
                limtCount = Utils.getlimitCount(WebAppActivity.this, parameter);
                selectCount = Utils.getSelectCount(WebAppActivity.this, parameter);
//                getPicDialog(WebAppActivity.this, Utils.IMAGE_SELECT_PHOTOS, function, REQUEST_CODE_CAMERA, REQUEST_CODE_GALLERY, limtCount, selectCount);
                break;
            case CMD.cmd_setDate://  设置日期
                Utils.getTimePickerDialog(mWebViewExten, WebAppActivity.this, Utils.DATA_SELECT_DATA, function, parameter);
                break;
            case CMD.cmd_setTime:// 设置时间
                Utils.getTimePickerDialog(mWebViewExten, WebAppActivity.this, Utils.DATA_SELECT_TIME, function, parameter);
                break;
            case CMD.cmd_setCity:// 设置城市
                Utils.showAreaWindow(mWebViewExten, mLl_back, WebAppActivity.this, parameter, function);
                break;
            case CMD.cmd_setAddress:// 设置地址
                gotoAmapLocationAcitvity(parameter, function);// 设置地址
                break;
            case CMD.cmd_showSelect:// 设置单选 （未实现）
                setSingleSelection();// 设置单选 （未实现）
                break;
            case CMD.cmd_setRightMenu:// 设置右上角菜单
                showModel = false;
                setTopAndRightMenu(parameter);// 设置右上角菜单
                break;
            case CMD.cmd_setLeftRightMenu:// 设置左上角菜单
                showModel = true;
                setTopAndRightMenu(parameter);// 设置左上角菜单
                break;
            case CMD.cmd_setCenterMenu:// 设置中间菜单
                setCenterMenu(parameter);// 设置中间菜单
                break;
            case CMD.cmd_setBottomMenu:// 设置底部菜单
                setBottomMenu(WebAppActivity.this, parameter);// 设置底部菜单
                break;
            case CMD.cmd_showMessage:// 弹出消息 toast
                ToastUtil.showLongToast(WebAppActivity.this, "我吐出来了paramter！");
                break;
            case CMD.cmd_pagetips:// 设置 弹窗提示
                setShowConfirmMessage(WebAppActivity.this, parameter);// 设置 弹窗提示
                break;
            case CMD.cmd_showHud:// 设置进度条 ProgressDialog
                setDialogForWait(parameter);
                break;
            case CMD.action_showModelPage: //  设置全屏隐藏导航栏
                setShowModelPage();//  设置全屏隐藏导航栏
                break;
            case CMD.action_showModelSearchPage:// 搜索界面
                gotoSearchPageAcitvity(parameter, function);// 搜索界面
                break;
            case CMD.action_closeModelPage: //  关闭全屏界面
                setCloseModelPage(); //  关闭全屏界面
                break;
            case CMD.cmd_scanQrcode: //扫描二维码
                startQrcode(parameter, function);// 扫描二维码
                break;
            case CMD.cmd_salary_dec:
                salaryDecode(parameter, function);// 薪资解密
                break;
            case CMD.cmd_forbidRefresh:// 禁止刷新
            case CMD.cmd_disableRefresh://  （耐克项目）  不能刷新
                forbidRefresh();// 不能刷新
                break;
            case CMD.cmd_openRefresh:// 开启刷新
                openRefresh();// 开启刷新
                break;
            case CMD.cmd_clearTask:// 任务栈清理
                clearTask(parameter, function);// 任务栈清理
                break;
//            case CMD.cmd_uploadFile:// 上传图片
//                limtCount = Utils.getlimitCount(WebAppActivity.this, parameter);
//                selectCount = Utils.getSelectCount(WebAppActivity.this, parameter);
//                getPicDialog(WebAppActivity.this, Utils.IMAGE_SELECT_CAMERA_AND_PHOTOS, function, REQUEST_CODE_CAMERA, REQUEST_CODE_GALLERY, limtCount, selectCount);// 上传图片
//                break;
            case CMD.cmd_GestureDrawing:
                sendMyBroadcast(parameter, function);// 发送特定广播 打开手势
                break;
            case CMD.cmd_initGesture:
                setLoginWay(function);// 初始化登录状态
                break;
            case CMD.cmd_imgBrowse:
                showImgBrowse(parameter, function);// 显示大图
                break;
            case CMD.cmd_checkClose:
                checkClose(parameter, function);// 检查 关闭
                break;
            case CMD.cmd_wechatShare:
                wechatShare(parameter, function);// 微信分享
                break;
            case CMD.cmd_setAlias:
                setAlias(parameter, function);// 极光推送 设置别名
                break;
            case CMD.cmd_setTags:
                setTags(parameter, function);// 极光推送 设置标签
                break;
            case CMD.cmd_setAliasAndTags:
                setAliasAndTags(parameter, function);// 极光推送 同时设置别名与标签
                break;
            case CMD.cmd_setBadgeNumber:
                setBadgeNumber(parameter, function);// 极光推送  设置badge值
                break;
            case CMD.cmd_getBadgeNumber:
                getBadgeNumber(parameter, function);// 极光推送 获取badge值
                break;
            case CMD.cmd_clearAllNotifications:
                clearAllNotifications(parameter, function);// 极光推送 清除所有通知
                break;
            case CMD.cmd_stopOrResumePush:
                stopOrResumePush(parameter, function);// 极光推送 清除所有通知
                break;
            case CMD.cmd_initLocation:
                setLoctioninfo(parameter, function);// 获取位置信息
                break;
            case CMD.cmd_recvMessage:
                receiveMessage(parameter, function);//   注册消息广播
                break;
            case CMD.cmd_sendMessage:
                sendMessage(parameter, function); // 发送消息 （发送广播）
                break;
            case CMD.cmd_getNetworkStatus:
                getNetworkStatus(function); // 发送消息 （发送广播）
                break;
            case CMD.cmd_setTitle:
                setNewTitle(parameter); // 设置导航栏标题
                break;
            case CMD.cmd_showToast:
                showToast(parameter); // toast弹窗
                break;
            case CMD.cmd_setNavigationBgColor:
                setTitleBarBg(parameter); // 设置导航栏背景色
                break;
            case CMD.cmd_setDatabase:// 设置数据库
                writeDB(parameter); //
                break;
            case CMD.cmd_getDatabase:// 读取数据库
                readDB(parameter, function); //
                break;
            case CMD.cmd_delKey:// 删除数据
                deleteDB(parameter); //
                break;
            case CMD.cmd_uploadFile_Base64:// 将图片转换返回64位字符串返回给js
                limtCount = Utils.getlimitCount(WebAppActivity.this, parameter);
                selectCount = Utils.getSelectCount(WebAppActivity.this, parameter);
                break;
            case CMD.cmd_setHeaderRefreshing://  （天信）页面下拉刷新
                mSwipeRefreshLayout.setRefreshing(true);// 刷新
                forbidRefresh();
                break;
            case CMD.cmd_openBrowser:// 打开浏览器
                openBrowser(parameter);
                break;
            case CMD.cmd_openContacts:// 选择联系人
                openContacts(parameter, function);
                break;
            case CMD.cmd_playVideo:// 打开视频
                playVideo(parameter, function);
                break;
            case CMD.cmd_speechRecognition:// 语音识别
                speechRecognition(parameter, function);
                break;
            case CMD.cmd_getVersion:// 获取app版本code
                getVersion(function);
                break;
            case CMD.cmd_checkForUpdate:// 检查并app版本是否需要更新
                checkForUpdate(parameter, function);
                break;
            case CMD.cmd_showTaskView://任务弹窗
                showTaskView(parameter, function);
                break;
            case CMD.cmd_downloadFile://下载
                getDown(parameter);
                break;
            case CMD.cmd_notice_native://打开缓存界面
                // TODO 缓存页面没有待开发
                break;
            case CMD.cmd_getLocalFile://获取所下载视频的本地绝对路径
                getLocalFile(parameter, function);
                break;
            case CMD.cmd_setSearchBar://搜索界面
                ll_search.setVisibility(View.VISIBLE);
                ll_head.setVisibility(View.GONE);
                setting_ll.setVisibility(View.GONE);
                break;
            case CMD.cmd_setSegment://自定义页面
                showRadioButton(parameter, function);
                break;
            case CMD.cmd_canBlankScreen://  （耐克项目）  解除禁止页面灰屏

                break;
            case CMD.cmd_disableBlankScreen://  禁止页面灰屏
                this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                break;

            case CMD.cmd_email://  （耐克项目）  邮件
                Utils.sendEmail(WebAppActivity.this, parameter);
                break;
            case CMD.cmd_pageConfirm://  （耐克项目）  两个按钮的对话框
                showConfirm(WebAppActivity.this, parameter);
                break;
//            case CMD.action_pag etips ://  （耐克项目）  一个按钮的对话框
//                showConfirm(WebAppActivity.this, parameter);
//                break;
            case CMD.cmd_uploadFile://  （耐克项目）  上传文件
                JSONObject json = null;
                try {
                    json = new JSONObject(parameter);
                    String type = json.optString("type");
                    getPic(type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case CMD.cmd_allowLandscape://强制横屏
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
                break;
            case CMD.cmd_clearCache://清除缓存
                Utils.clearWebViewCache(WebAppActivity.this, APP_CACAHE_DIRNAME);
                mWebViewExten.loadUrl(getFormatJavascript(function, "清除成功"));
                break;

            case CMD.cmd_getudid://获取UUID
                String uuid = UUIDTools.getInstance().getUuid(this);
                mWebViewExten.loadUrl(getFormatJavascript(function, uuid));
                break;
            case CMD.cmd_getCache://获取缓存大小
                try {
                    String formatSzie = Utils.getCacherSize(this.getCacheDir().getAbsolutePath(), APP_CACAHE_DIRNAME, getPackageName());
                    String josn = getFormatJavascript(function, formatSzie);
                    mWebViewExten.loadUrl(josn);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            default:// 其他
                ToastUtil.showLongToast(WebAppActivity.this, parameter);
                break;
        }
    }

    /**
     * @param type
     */
    public void getPic(String type) {
        if (type.equals("1")) {
            new ActionSheetDialog(WebAppActivity.this)
                    .builder()
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    uploadType = "picture";
                                    takePhoto();
                                }
                            })
                    .addSheetItem("相册", ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    uploadType = "picture";
                                    selPhoto();
                                }
                            }).show();
        } else if (type.equals("2")) {
            new ActionSheetDialog(WebAppActivity.this)
                    .builder()
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    uploadType = "picture";
                                    takePhoto();

                                }
                            })
                    .addSheetItem("相册", ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    uploadType = "picture";
                                    selPhoto();
                                }
                            })
                    .addSheetItem("语音", ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    uploadType = "speech";
//                                    startActivity(new Intent(WebAppActivity.this, RecordActivity2.class));
//                                    registerBroadcastReceiver();
                                }
                            }).show();
        }

    }

    /**
     * 获取相册
     */
    public void selPhoto() {
        if (Build.VERSION.SDK_INT < 23) {
            System.out.println("sdk < 23");
            openAlbum();
        } else {
            //6.0
            System.out.println("sdk 6.0");
            if (ContextCompat.checkSelfPermission(WebAppActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //该权限已经有了
                System.out.println("权限已经有了");
                openAlbum();
            } else {
                //申请该权限
                System.out.println("申请该权限");
                ActivityCompat.requestPermissions(WebAppActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x2222);
            }
        }
    }

    /**
     * 获取图片
     */
    public void takePhoto() {
        if (Build.VERSION.SDK_INT < 23) {
            System.out.println("sdk < 23");
            takePicture();
        } else {
            //6.0
            System.out.println("sdk 6.0");
            if (ContextCompat.checkSelfPermission(WebAppActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                //该权限已经有了
                takePicture();
            } else {
                //申请该权限
                System.out.println("申请该权限");
                requestPermissions(new String[]{Manifest.permission.CAMERA}, NICK_REQUEST_CAMERA_CODE);
//                ActivityCompat.requestPermissions(WebAppActivity.this, new String[]{Manifest.permission.CAMERA}, 105);
            }
        }
    }

    /**
     * 拍照
     */
    public void takePicture() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File outDir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!outDir.exists()) {
                outDir.mkdirs();
            }
            File outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
            picFileFullName = outFile.getAbsolutePath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        } else {
            Log.e("TAG", "请确认已经插入SD卡");
        }
    }

    /**
     * 打开本地相册
     */
    public void openAlbum() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        this.startActivityForResult(intent, PICK_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    /**
     * 两个按钮的提示框
     *
     * @param context
     * @param parameter 参数
     */
    private void showConfirm(WebAppActivity context, String parameter) {
        AlertDialog mAlertDialog = new AlertDialog(context).builder();
        JSONObject object = null;
        try {
            object = new JSONObject(parameter);
            final String title = object.getString("title");
            final String content = object.getString("content");
            final String confirm = object.getString("yes_action");
            final String cancel = object.getString("no_action");
            final String yes_parm = object.getString("yes_param");
            final String no_parm = object.getString("no_param");
            if (TextUtils.isEmpty(title)) {
                mAlertDialog.setTitle("温馨提示");
            } else {
                mAlertDialog.setTitle(title);
            }

            if (TextUtils.isEmpty(content)) {
                mAlertDialog.setTitle("这是一个警告！");
            } else {
                mAlertDialog.setMsg(content);
            }
            mAlertDialog.setPositiveButton("确认", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (confirm.startsWith("http")) { // URL请求
                        Map<String, String> param = UrlUtil.parseUrl(confirm);
                        if (param.containsKey("action")) {
                            String value = param.get("action");
                            if (value != null) {
                                switch (value) {
                                    case CMD.action_nextPage:
                                        onNextPage(confirm, value);
                                        break;
                                    case CMD.action_closePage:
                                        onClosePage(confirm, value);
                                        break;
                                    case CMD.action_closePageAndRefresh:
                                        onClosePageReturnMain(confirm, value);
                                        break;
                                }
                            }
                        }
                    } else if (confirm.startsWith("kitapps")) { // 页面的功能函数
                        Map<String, String> param = parseUrl(confirm);
                        String parameter = param.get("para");
                        String function = param.get("callback");
                        Pattern p = Pattern.compile("\\//(.*?)\\?");//正则表达式，取=和|之间的字符串，不包括=和|
                        Matcher m = p.matcher(confirm);
                        String cmd = null;
                        while (m.find()) {
                            cmd = m.group();
                            break; // 菜单里的url带"?"的话会导致cmd取值不对，所以只拿第一次的cmd
                        }
                        if (cmd != null) {
                            cmd = cmd.substring(2, cmd.length() - 1);
                            onCommand(cmd, parameter, function);
                        }
                    } else { // 回调js方法
                        if (!TextUtils.isEmpty(confirm)) {
                            String josn2 = getFormatJavascript(confirm, yes_parm);
                            mWebViewExten.loadUrl(josn2);
                            Log.e(TAG, "==josn:" + josn2);
                        }
                    }
                }
            });


            mAlertDialog.setNegativeButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        ToastUtil.showLongToast(WebAppActivity.this, "取消！");
                    if (!TextUtils.isEmpty(cancel)) {
                        String josn2 = String.format("javascript:" + cancel + "(" + "'%s')", no_parm);
                        mWebViewExten.loadUrl(josn2);
                    }
                }
            });

            mAlertDialog.show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 动态添加（头布局 中间）radioButton
     */
    public JSONArray array;
    public int position;
    public RadioButton radioButton;
    public String checked;

    @SuppressLint("NewApi")
    public void showRadioButton(String parameter, final String function) {
        ll_search.setVisibility(View.GONE);
        ll_center_normal.setVisibility(View.GONE);
        rg_all.setVisibility(View.VISIBLE);
        JSONObject json = null;
        try {
            json = new JSONObject(parameter);
            array = json.getJSONArray("titles");
            if (array.length() < 1) {
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONObject json2 = new JSONObject(parameter);
            array = json2.getJSONArray("titles");
            checked = json2.getString("defaultValue");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        rg_all.removeAllViews();
        int length = array.length();
        for (int i = 0; i < array.length(); i++) {
            position = i;

            radioButton = new RadioButton(getApplicationContext());
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(0, DisplayUtil.dip2px(WebAppActivity.this, 30f));
            layoutParams.weight = 1.0f;
            radioButton.setText(array.optString(i));
            radioButton.setTextColor(getResources().getColorStateList(R.color.bg_radiobutton_text));
            radioButton.setButtonDrawable(android.R.color.transparent);//隐藏单选圆形按钮
            radioButton.setGravity(Gravity.CENTER);
            if (i != 0 && i != (length - 1)) {
                radioButton.setBackground(getResources().getDrawable(R.drawable.bg_radiobuton));//设置按钮选中/未选中的背景
            }

            if (i == 0) {

                radioButton.setBackground(getResources().getDrawable(R.drawable.bg_radiobuton_left));//设置按钮选中/未选中的背景
            }
            if (i == (length - 1)) {
                radioButton.setBackground(getResources().getDrawable(R.drawable.bg_radiobuton_right));//设置按钮选中/未选中的背景
            }
            rg_all.addView(radioButton, layoutParams);
        }


        //设置默认加载第一页并加载第一页数据
//        RadioButton childAt = (RadioButton) rg_all.getChildAt(0);
//        childAt.setChecked(true);

        for (int i = 0; i < array.length(); i++) {
            try {
                if (array.get(i).toString().equals(checked)) {
                    RadioButton check = (RadioButton) rg_all.getChildAt(i);
                    check.setChecked(true);
                    String josn1 = String.format("javascript:" + function + "(" + "'%s')", array.optString(i));
                    mWebViewExten.loadUrl(josn1);
//                    Log.e("radio",array.get(i).toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        rg_all.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                String josn1 = String.format("javascript:"+function+"(" + "'%s')", array.optString((checkedId)));
//                mWebViewExten.loadUrl(josn1);
//                ToastUtil.showShortToast(WebAppActivity.this,"点击调用"+josn1);
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton rd = (RadioButton) group.getChildAt(i);
                    if (group.getCheckedRadioButtonId() == rd.getId()) {
                        String json = String.format("javascript:" + function + "(" + "'%s')", rd.getText().toString());
                        mWebViewExten.loadUrl(json);
//                        ToastUtil.showShortToast(WebAppActivity.this,"点击调用"+json);
                    }
                }
            }
        });

    }

    /**
     * 获取所下载视频的本地绝对路径
     *
     * @param parameter 参数
     * @param function  回掉方法
     */
    private void getLocalFile(String parameter, String function) {
        // TODO 获取所下载视频的本地绝对路径

    }

    /**
     * 下载文件
     *
     * @param parameter
     */
    protected void getDown(final String parameter) {
        //如果Sd卡不可用 直接return
        if (!SDCardUtils.isSDCardEnable()) {
            ToastUtil.showShortToast(WebAppActivity.this, "请检查SD卡是否可用！");
            return;
        }
        //判断网络是否连接
        if (NetworkUtils.isConnected(WebAppActivity.this)) {
            //如果是wifi直接下载
            if (NetworkUtils.isWifiConnected(WebAppActivity.this)) {
                toDownload(parameter);
                //移动网络则让用户确定下载
            } else {
                AlertDialog a = new AlertDialog(WebAppActivity.this);
                a.builder();
                a.setTitle(getString(R.string.tishi));
                a.setMsg(getString(R.string.down_msg));
                a.setNegativeButton(getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                a.setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toDownload(parameter);
                    }
                });
                a.show();

            }

        } else {
            ToastUtil.showShortToast(WebAppActivity.this, "请检查网络连接状态");
        }

    }

    /**
     * 下载文件
     *
     * @param parameter
     */
    private void toDownload(String parameter) {
        // TODO 下载文件没有待开发

    }

    /**
     * 弹出任务框
     *
     * @param parameter 参数
     * @param function  回掉方法
     */
    public void showTaskView(String parameter, String function) {
        // 数据
        List<Task> list = new ArrayList<Task>();

        // 创建弹窗
        Dialog d = new Dialog(this, R.style.FullHeightDialog);
        d.setContentView(R.layout.activity_dialog_task);
        d.setCanceledOnTouchOutside(false);
        ListView listview = (ListView) d.findViewById(R.id.dialog_listview);
        TextView cancel = (TextView) d.findViewById(R.id.cancel);
        RelativeLayout rl_top = (RelativeLayout) d.findViewById(R.id.rl_top);
        LinearLayout ll_bottom = (LinearLayout) d.findViewById(R.id.ll_bottom);
        rl_top.setOnClickListener(new DisDialog(d));
        ll_bottom.setOnClickListener(new DisDialog(d));
        cancel.setOnClickListener(new DisDialog(d));
        try {
            list.clear();
            JSONObject json = new JSONObject(parameter);
            String item = json.optString("item");
            Log.e("Dioalog", "json" + json.toString());
            Log.e("Dioalog", "item" + item.toString());
            JSONArray array = json.getJSONArray("item");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Task t = new Task();
                t.setHeader_title(obj.optString("header_title"));
                t.setImages_url(obj.optString("images_url"));
                t.setDate(obj.optString("date"));
                t.setUrl(obj.optString("todoList_no"));
                list.add(t);
            }

            listview.setAdapter(new TaskAdapter(function, list, WebAppActivity.this, mWebViewExten, d));
            Log.e("Dioalog", "list" + list.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Dioalog", "error" + e.toString());
        }

        d.show();
    }

    /***
     * 检查并app版本是否需要更新
     *
     * @param parameter 参数
     * @param function  回掉方法
     */
    protected void checkForUpdate(String parameter, String function) {

    }

    /**
     * 获取版本号
     * 获取app版本code
     *
     * @param function 回掉方法
     */
    protected void getVersion(final String function) {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            final String version = packInfo.versionName;
            String json = getFormatJavascript(function, version);
            loadUrl(json);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 格式化 javascript
     *
     * @param function 回掉方法
     * @param paramter 参数
     * @return javascript方法
     */
    protected String getFormatJavascript(String function, String paramter) {
        return String.format("javascript:" + function + "('%s')", paramter);
    }

    /**
     * 加载 执行url
     *
     * @param javascript javascript方法
     */
    protected void loadUrl(String javascript) {
        Log.i("loadUrl", javascript);
        mWebViewExten.loadUrl(javascript);
    }

    /***
     * 语音识别
     *
     * @param paramter 参数
     * @param function 回掉方法
     */
    protected void speechRecognition(String paramter, String function) {

        /*try {
            JSONObject jsonObject = new JSONObject(paramter);

        } catch (JSONException e) {
            e.printStackTrace();
        }*/


    }

    /**
     * 播放视频
     *
     * @param paramter 参数
     * @param function 回掉方法
     */
    protected void playVideo(String paramter, String function) {
//        registerBroadcastReceiver();
        try {
            Intent i = new Intent(WebAppActivity.this, EduMediaPlayer.class);
            JSONObject json = new JSONObject(paramter);
            String url = json.optString("courseSrc");
//            Boolean studyState = json.optBoolean("studyState");
//            int currentTime = json.optInt("currentTime");
//            i.putExtra("studyState", studyState);
//            i.putExtra("currentTime", currentTime);
            i.putExtra("url", url);
            startActivity(i);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /***
     * 打开通讯录
     *
     * @param paramter 参数
     * @param function 回掉方法
     */
    protected void openContacts(String paramter, String function) {

    }

    /**
     * 打开手机自带浏览器
     *
     * @param parameter 命令参数
     */
    private void openBrowser(String parameter) {
        try {
            JSONObject jsonObject = new JSONObject(parameter);
            /** 网页地址*/
            String url = jsonObject.optString("url");
//            /*网页名称*/
//            String name=jsonObject.optString("name");
            // 启动浏览器
            Uri uri = Uri.parse(url);
            Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 读取数据库
     *
     * @param paramter 命令参数
     * @param function 回掉方法
     */
    private void readDB(String paramter, String function) {
        try {
            JSONObject jsonObject = new JSONObject(paramter);
            /** 存储key*/
            String getkey = jsonObject.optString("key");
            /*工号*/
            String acount = jsonObject.optString("account");
            String valueFromDB = getValueFromDB(acount, getkey);
            //String.format("javascript:" + function + "('%s')", json);
            String json = getFormatJavascript(function, valueFromDB);
            loadUrl(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 存储数据库
     *
     * @param parameter 命令参数
     */
    private void writeDB(String parameter) {
        try {
            JSONObject jsonObject = new JSONObject(parameter);
            /** 存储key*/
            String getkey = jsonObject.optString("key");
            /*工号*/
            String acount = jsonObject.optString("account");
            setKeyToDB(acount, getkey, parameter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除数据
     *
     * @param parameter 命令参数
     */

    private void deleteDB(String parameter) {
        try {

            JSONObject jsonObject = new JSONObject(parameter);
            /** 存储key*/
            String getkey = jsonObject.optString("key");
            /*工号*/
            String acount = jsonObject.optString("account");

            deleteValueFromDB(acount, getkey);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置导航栏 背景颜色
     *
     * @param parameter js返回参数
     */
    private void setTitleBarBg(String parameter) {
        try {
            JSONObject jsonObject = new JSONObject(parameter);
            /** js返回的颜色*/
            String color = jsonObject.optString("color");
            ll_head.setBackgroundColor(Color.parseColor(TextUtils.isEmpty(color) ? "#0089F6" : color));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * toast弹窗
     *
     * @param paramter js返回参数
     */
    private void showToast(String paramter) {
        try {
            JSONObject jsonObject = new JSONObject(paramter);
            /** js返回的标题*/
            String msg = jsonObject.optString("message");
            ToastUtil.showLongToast(this, msg);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置导航栏标题
     *
     * @param paramter js返回参数
     */
    private void setNewTitle(String paramter) {
        try {
            JSONObject jsonObject = new JSONObject(paramter);
            /** js返回的标题*/
            String name = jsonObject.optString("title");
            onTitle(1, name);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * 检测 网络状态
     *
     * @param function 回掉方法
     */
    public void getNetworkStatus(String function) {
        //网络可用
        //wifi
        //网络不可用
        if (NetworkUtils.isAvailable(WebAppActivity.this)) {
            if (NetworkUtils.isWifiConnected(WebAppActivity.this)) {
                String json = getFormatJavascript(function, "WiFi");
                loadUrl(json);
                //手机网络
            } else {
                //String.format("javascript:" + function + "(" + "'%s')", "3G");
                String json = getFormatJavascript(function, "3G");
                loadUrl(json);
            }
        } else {
            String json = getFormatJavascript(function, "None");
            loadUrl(json);

        }
    }


    /**
     * 发送消息
     *
     * @param paramter js返回参数
     * @param function 回掉方法
     */
    private void sendMessage(String paramter, String function) {

        Log.e("12345send", "parameter" + paramter + "   " + "function  " + function);
        String name;
        try {
            JSONObject jsonObject = new JSONObject(paramter);
            name = jsonObject.optString("notificationName");
            String type = jsonObject.optString("type");
            if (TextUtils.isEmpty(name)) { // 广播的默认action
                name = "receiveMessage";
            } else {
                // 根据js返回 拼接 广播的action
                name = name + type;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            // 广播的默认action
            name = "receiveMessage";
        }
        Intent tent = new Intent(name);// 广播的标签，一定要和需要接受的一致。
        tent.putExtra("data", paramter);
        tent.putExtra("function", function);
        sendBroadcast(tent);// 发送广播
    }

    /**
     * 接收消息，注册消息的广播
     * registerBroadcastReceiver
     *
     * @param parameter js返回参数
     * @param function  回掉方法
     */
    protected void receiveMessage(String parameter, String function) {
        String name;
        try {
            JSONObject jsonObject = new JSONObject(parameter);

            name = jsonObject.optString("notificationName");
            String type = jsonObject.optString("type");
            if (!TextUtils.isEmpty(name)) {
                name = name + type;
            } else {
                name = "receiveMessage";
            }

        } catch (JSONException e) {
            e.printStackTrace();
            name = "receiveMessage";
        }
        Log.e("12345receive", "parameter" + parameter + "   " + "function  " + function);
        registerBoradcastReceiver(name, function);
    }

    /**
     * 注册广播
     *
     * @param actionName BoradcastReceiver 的action
     * @param function   回掉方法
     */
    public void registerBoradcastReceiver(String actionName, String function) {
        if (receiveBroadCast == null) {
            receiveBroadCast = new ReceiveBroadCast(actionName, function);
            IntentFilter filter = new IntentFilter();
            filter.addAction(actionName); // 只有持有相同的action的接受者才能接收此广播 receiveMessage
            registerReceiver(receiveBroadCast, filter);
        }
    }

    /**
     * 设置位置信息
     *
     * @param paramter 命令参数
     * @param function 回掉方法
     */
    protected void setLoctioninfo(String paramter, String function) {
        // 初始化页面，调js函数initlocation('省份','城市')
        String json3 = String.format("javascript:" + function + "(" + "'%s','%s')", AppConfing.LOCATION_Province, AppConfing.LOCATION_City);
        loadUrl(json3);
    }

    /**
     * 停止或者启动推送
     *
     * @param parameter 命令参数
     * @param function  回掉方法
     */
    protected void stopOrResumePush(String parameter, String function) {

    }

//    /**
//     * web View 加载新的Url
//     * @param paramter
//     * @param function
//     */
//    private void locaPage(String paramter, String function) {
//
//    }

    /**
     * 极光清除所有通知
     *
     * @param paramter 命令参数
     * @param function 回掉方法
     */
    protected void clearAllNotifications(String paramter, String function) {

    }

    /**
     * 极光获取badge值
     *
     * @param paramter 命令参数
     * @param function 回掉方法
     */
    protected void getBadgeNumber(String paramter, String function) {

    }

    /**
     * 极光设置badge值
     *
     * @param paramter 命令参数
     * @param function 回掉方法
     */
    protected void setBadgeNumber(String paramter, String function) {

    }


    /**
     * 极光 同时设置别名与标签
     *
     * @param paramter 命令参数
     * @param function 回掉方法
     */
    protected void setAliasAndTags(String paramter, String function) {

    }


    /**
     * 极光设置标签
     *
     * @param paramter 命令参数
     * @param function 回掉方法
     */
    protected void setTags(String paramter, String function) {

    }

    /**
     * 极光1. setAlias 设置别名
     *
     * @param parameter 命令参数
     * @param function  回掉方法
     */
    protected void setAlias(String parameter, String function) {

    }

    /**
     * 微信分享
     *
     * @param parameter 命令参数
     * @param function  回掉方法
     */
    protected void wechatShare(String parameter, String function) {
//        //创建下载微信分享图片
//        initTarg();
//        creatFilePath("DCIM");
//        try {
//            JSONObject jsonObject = new JSONObject(parameter);
//            JSONArray item = jsonObject.getJSONArray("item");
//            String url = item.getJSONObject(0).optString("url");// 图片url
//            String name = item.getJSONObject(0).optString("title");// 标题
//            String icon = item.getJSONObject(0).optString("icon");// 图片路径
//            String summary = item.getJSONObject(0).optString("summary");// 简介
//            showShare(url, name, icon, summary);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }

    /**
     * 是否需要 启用 关闭页面前 的check
     *
     * @param parameter 命令参数
     * @param function  回掉方法
     */
    protected void checkClose(String parameter, String function) {
        try {
            JSONObject jsonObject = new JSONObject(parameter);
            this.isNeedClose = jsonObject.getBoolean("isFinish");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示大图
     *
     * @param parameter 命令参数
     * @param function  回掉方法
     */
    protected void showImgBrowse(String parameter, String function) {
        try {
            JSONObject jsonObject = new JSONObject(parameter);
            JSONArray item = jsonObject.getJSONArray("item");
            String url = item.getJSONObject(0).optString("url");// 图片url
            String name = item.getJSONObject(0).optString("name");// 右上角菜单名称
            startActivity(new Intent(this, ImageBrowserActivty.class).putExtra(IMAGE_URL, url).putExtra(IMAGE_RIGHTMEN, name));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 当前页面可以下拉刷新
     */
    private void openRefresh() {
        isRefreshEnable = false;
        mSwipeRefreshLayout.setEnabled(true);
    }

    /**
     * 初始化登录状态
     *
     * @param function 回掉方法
     */
    private void setLoginWay(String function) {
        functionNameStr = function;
        LockPatternUtils mLockPatternUtils = new LockPatternUtils(this);
        if (mLockPatternUtils.savedPatternExists()) { // 手势登录
            String json = getFormatJavascript(functionNameStr, "2");
            loadUrl(json);
            return;
        }

        if (!SharedPreferUtil.getInstance(this).getPrefBoolean("passWordBool", false)) {// 自动（免密）登录 默认值true
            String json = getFormatJavascript(functionNameStr, "1");
            loadUrl(json);
        } else {// 输入密码登录
            String json = getFormatJavascript(functionNameStr, "3");
            loadUrl(json);
        }

    }

    /**
     * 打开手势登陆
     *
     * @param paramter 命令参数
     * @param function 回掉方法
     */
    protected void sendMyBroadcast(String paramter, String function) {
        functionNameStr = function;
        LockPatternUtils mLockPatternUtils = new LockPatternUtils(this);
        String typeStr = "";

        try {
            JSONObject mJSONObject = new JSONObject(paramter);
            typeStr = mJSONObject.optString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if ("1".equals(typeStr)) { // 免密码登录
            SharedPreferUtil.getInstance(this).setPrefBoolean("passWordBool", false);
            mLockPatternUtils.clearLock();
        } else if ("2".equals(typeStr)) { // 创建手势登录
//            Intent intent = new Intent(this, CreatGesturePasswordActivity.class);
//            intent.putExtra("showBack", "1");
//            intent.putExtra("password_MD5", "");
//            startActivityForResult(intent, INTENT_REQUEST_GESTURE);
        } else if ("3".equals(typeStr)) { // 密码登录
            SharedPreferUtil.getInstance(this).setPrefBoolean("passWordBool", true);
            mLockPatternUtils.clearLock();
        }
    }

    /**
     * 清理栈 （关闭 指定的页面）
     *
     * @param paramter 命令参数
     * @param function 回掉方法
     */
    protected void clearTask(String paramter, String function) {
        Log.i(TAG, "clearTask  paramter:" + paramter + "     function:" + function);
        if (isClearTask) { // 不能二次清除
            return;
        }
        isClearTask = true;
        try {
            JSONObject object = new JSONObject(paramter);
            final int index = object.optInt("clearTask");
            ActivityCollector.clearTask(index);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 页面不可刷新
     * 禁止刷新
     */
    public void forbidRefresh() {
        isRefreshEnable = true;
        mSwipeRefreshLayout.setEnabled(false);
    }

    /**
     * 密文解密 设置薪资数据
     *
     * @param paramter 命令参数
     * @param function 回掉方法
     */
    protected void salaryDecode(String paramter, String function) {
//        try {
//            JSONObject object = new JSONObject(paramter);
//            final String passwd = object.optString("passwd");
//            final String text = object.optString("text");
//
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("dec_text", AESDecode.decode(passwd, text));
//            String json = String.format("javascript:" + function + "(%s)", jsonObject);
//            loadUrl(json);
//            Log.i(TAG, "AESDecode:" + json);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


    }

    /**
     * 扫描二维码
     *
     * @param paramter 命令参数
     * @param function 回掉方法
     */
    protected void startQrcode(String paramter, String function) {
//        Intent intent = new Intent(this, QrcodeActivity.class);
//        intent.putExtra(PARAMTER, paramter);
//        intent.putExtra(FUNCTION, function);
//        startActivityForResult(intent, INTENT_REQUEST_QRCODE);
    }


    /**
     * 跳转搜索页
     *
     * @param paramter 命令参数
     * @param function 回掉方法
     */
    protected void gotoSearchPageAcitvity(String paramter, String function) {
        hideTitle();
//        searchFunction = function;
//        Bundle bundle = new Bundle();
//        bundle.putInt("flag", 1);
//        Intent intent=new Intent(this, SearchActivity.class);
//        intent.putExtras(bundle);
//        startActivityForResult(intent,REQUEST_CODE_SEARCH);
    }

    /**
     * 隐藏title
     */
    protected void hideTitle() {
        ll_head.setVisibility(View.GONE);
    }

    /**
     * 跳转到地图
     *
     * @param paramter 命令参数
     * @param function 回掉方法
     */
    protected void gotoAmapLocationAcitvity(String paramter, String function) {
        areaFunction = function;
//        Bundle bundle = new Bundle();
//        bundle.putInt("flag", 1);
//        Intent intent=new Intent(this, AMapLocationAcitvity.class);
//        intent.putExtras(bundle);
//        startActivityForResult(intent,REQUEST_CODE_MAP);
    }

    /**
     * 设置title菜单
     *
     * @param json js返回的title数据
     */
    protected void setCenterMenu(String json) {
        farstTitleMune = true;
        listTitleMenuItem.clear();
        TopMenu menu = Utils.json2entity(json, TopMenu.class);
        Log.e(TAG, "数据：" + menu.getItem().get(0).toString());
        //
        listTitleMenuItem.addAll(menu.getItem());
        if (listTitleMenuItem.size() > 0) ll_head_title.setClickable(true); // 可点击
        if (!TextUtils.isEmpty(menu.getItem().get(0).getName())) {// 显示标题
            mTv_head_title.setText(menu.getItem().get(0).getName());
            iv_head_title_menu.setVisibility(View.VISIBLE);
        } else { // 隐藏
            mTv_head_title.setText(urlTitle);
            iv_head_title_menu.setVisibility(View.GONE);
        }
        if (listTitleMenuItem.size() == 1) {// 当且只有一个的时候，隐藏下拉图标
            iv_head_title_menu.setVisibility(View.GONE);
        }

    }

    /**
     * 设置右上角菜单
     *
     * @param json js返回的Menu数据
     */
    public void setTopAndRightMenu(String json) {
        farstMune = true;
        listMenuItem.clear();
        TopMenu menu = Utils.json2entity(json, TopMenu.class);
        /**当返回菜单数组为空 隐藏菜单*/
        if (menu == null || menu.getItem() == null || menu.getItem().size() == 0) {
            img_right.setVisibility(View.GONE);
            tv_head_right.setVisibility(View.GONE);
            ll_right.setClickable(false);
            return;
        }
//        Log.e(TAG, "数据：" + menu.getItem().get(0).toString());
        //
        listMenuItem.addAll(menu.getItem());
        if (listMenuItem.size() > 0) ll_right.setClickable(true); // 右title可点击
        if (showModel) { //showmodle模式
            tv_head_left.setVisibility(View.VISIBLE);
            tv_head_left.setText(listMenuItem.get(0).getName());
            tv_head_left.setClickable(true); // 左title文字可点击
            tv_head_right.setVisibility(View.GONE);
            if (listMenuItem.size() < 2) return;
            img_right.setVisibility(View.GONE);
            tv_head_right.setVisibility(View.VISIBLE);
            tv_head_right.setText(listMenuItem.get(1).getName());
        } else {
            tv_head_left.setVisibility(View.GONE);
            tv_head_left.setClickable(false); // 左title文字可点击
            if (!TextUtils.isEmpty(menu.getItem().get(0).getIcon())) {// 显示图片
                tv_head_right.setVisibility(View.GONE);
                img_right.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(menu.getItem().get(0).getIcon()))
                    Picasso.with(this).load(menu.getItem().get(0).getIcon()).into(img_right);
            } else if (!TextUtils.isEmpty(menu.getItem().get(0).getName())) { // 显示菜单名称
                img_right.setVisibility(View.GONE);
                tv_head_right.setVisibility(View.VISIBLE);
                tv_head_right.setText(listMenuItem.get(0).getName());
            } else { // 隐藏
                img_right.setVisibility(View.GONE);
                tv_head_right.setVisibility(View.GONE);
                tv_head_right.setText("菜单");
            }
        }

    }


    /**
     * 转圈等待
     *
     * @param msg 提示消息
     */
    protected void setDialogForWait(String msg) {

        try {
            JSONObject object = new JSONObject(msg);
            final String content = object.optString("content");
            final String action = object.optString("action");
            Log.e(TAG, "etDialogForWait:actions: " + action + "  :content:  " + content);
            if (TextUtils.equals("hide", action)) {
                colseProgressDialog();
                return;
            }

            if (TextUtils.isEmpty(content)) {
                mProgressDialog.setMessage("正在加载...");

            } else {
                if ("加载完成，关闭..".equals(content))
                    handler.sendEmptyMessage(1);
                mProgressDialog.setMessage(content);
            }

            if ("show".equals(action)) mProgressDialog.show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭转圈等待
     */
    public void colseProgressDialog() {

//        Log.e(TAG,"colseProgressDialog");
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();

        // 创建定时器
//        final Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                mProgressDialog.dismiss();
//                timer.cancel();
//            }
////        }, 1000, 300);
//        }, 0, 50);
    }

    /**
     * 设置 底部菜单
     *
     * @param context   环境
     * @param parameter 菜单参数
     */
    public void setBottomMenu(final Context context, String parameter) {
        List<BottomItem> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(parameter);
            JSONArray item = jsonObject.optJSONArray("item");
            for (int i = 0; i < item.length(); i++) {
                BottomItem bottomItem = JSON.parseObject(item.get(i).toString(), BottomItem.class);
                list.add(bottomItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        ActionSheetDialog mActionSheetDialog = new ActionSheetDialog(context);
        mActionSheetDialog.builder()
                .setTitle("请选择操作")
                .setCancelable(false)
                .setCanceledOnTouchOutside(false);

        for (final BottomItem item : list) {
            mActionSheetDialog.addSheetItem(item.getName(), ActionSheetDialog.SheetItemColor.Blue,
                    new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
//                            ToastUtil.showLongToast(context, item.getName());
                            String json = String.format("javascript:" + item.getCallback() + "(%s)", "");
                            loadUrl(json);
                        }
                    });
        }

        mActionSheetDialog.show();

    }

    /**
     * 确认提示框
     *
     * @param context 环境
     * @param msg     js返回json
     */
    protected void setShowConfirmMessage(Context context, String msg) {
        AlertDialog mAlertDialog = new AlertDialog(context).builder();

        try {
            JSONObject object = new JSONObject(msg);
            final String title = object.optString("title");
            final String content = object.optString("content");
            final String confirm = object.optString(getString(R.string.yes_action));
            final String cancel = object.optString(getString(R.string.no_action));
            final String cancel2 = object.optString(getString(R.string.no_ation));

            if (TextUtils.isEmpty(title)) {
                mAlertDialog.setTitle("温馨提示");
            } else {
                mAlertDialog.setTitle(title);
            }

            if (TextUtils.isEmpty(content)) {
                mAlertDialog.setTitle("这是一个警告！");
            } else {
                mAlertDialog.setMsg(content);
            }

            if (!TextUtils.isEmpty(confirm))
                mAlertDialog.setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 回调js方法
                        if (confirm.startsWith("http")) { // URL请求
                            Map<String, String> param = parseUrl(confirm);
                            if (param.containsKey("action")) {
                                String value = param.get("action");
                                if (value != null) {
                                    switch (value) {
                                        case CMD.action_nextPage:
                                            onNextPage(confirm, value);
                                            break;
                                        case CMD.action_closePage:
                                            onClosePage(confirm, value);
                                            break;
                                        case CMD.action_closePageAndRefresh:
                                            onClosePageReturnMain(confirm, value);
                                            break;
                                    }
                                }
                            }
                        } else if (confirm.startsWith("kitapps")) { // 页面的功能函数
                            Map<String, String> param = parseUrl(confirm);
                            String parameter = param.get("para");
                            String function = param.get("callback");
                            Pattern p = Pattern.compile("\\//(.*?)\\?");//正则表达式，取=和|之间的字符串，不包括=和|
                            Matcher m = p.matcher(confirm);
                            String cmd = null;
                            while (m.find()) {
                                cmd = m.group();
                                break; // 菜单里的url带"?"的话会导致cmd取值不对，所以只拿第一次的cmd
                            }
                            if (cmd != null) {
                                cmd = cmd.substring(2, cmd.length() - 1);
                                onCommand(cmd, parameter, function);
                            }
                        } else if (confirm.endsWith(")")) {
                            String json = "javascript:" + confirm;
                            Log.e(TAG, "==json:" + json);
                            loadUrl(json);
                        } else if (TextUtils.equals(confirm, "close")) {// 关闭 mAlertDialog

                        } else {
                            String json = String.format("javascript:" + confirm + "(%s)", "");
                            loadUrl(json);
                        }
                    }
                });

            if (!TextUtils.isEmpty(cancel) || !TextUtils.isEmpty(cancel2)) {
                mAlertDialog.setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        ToastUtil.showLongToast(WebAppActivity.this, "取消！");
                    }
                });
            }

            mAlertDialog.show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * close 关闭页面的指令
     */
    protected void onClose(String confirm, String value) {
        ActivityCollector.removeActivity(this);
        finish();
    }

    /**
     * 强制设置全屏界面（导航栏隐藏）
     */
    public void setShowModelPage() {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                  WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        ll_head.setVisibility(View.GONE);
    }

    /**
     * 关闭全屏界面
     */
    public void setCloseModelPage() {
        ll_head.setVisibility(View.VISIBLE);
    }


    /**
     * 单选
     */
    protected void setSingleSelection() {

        List<String> list = new ArrayList<>();
        list.add("学士");
        list.add("硕士");
        list.add("博士");

        mSingleSeletPopupWindow = new SingleSeletPopupWindow(WebAppActivity.this);
        mSingleSeletPopupWindow.mSingleSelectionAdapter.setData(list);
        mSingleSeletPopupWindow.showPopupWindow(mLl_back);
        mSingleSeletPopupWindow.setOnActionClickListener(new SingleSeletPopupWindow.OnActionClickListener() {
            @Override
            public void onSingleItemClickListener(AdapterView<?> parent, View view, int position, long id) {
                lv_single_Item = position;
                mSingleSeletPopupWindow.mSingleSelectionAdapter.setIndexSelection(lv_single_Item);
                mSingleSeletPopupWindow.dismiss();
            }
        });

    }

    /**
     * 设置顶部菜单
     */
    public void setTopRightMenu() {
        mTopMenuPopWin = new TopMenuPopupWindowActivity(WebAppActivity.this);
        if (farstMune) {
            listMenuItem.remove(0);
            farstMune = false;
        }
        mTopMenuPopWin.mTopMenuAdapter.setData(listMenuItem);
        mTopMenuPopWin.showPopupWindow(tv_head_right);
        mTopMenuPopWin.setOnActionClickListener(new TopMenuPopupWindowActivity.OnActionClickListener() {
            @Override
            public void onSingleItemClickListener(AdapterView<?> parent, View view, int position, long id) {
                lv_single_Item = position;
//                mTopMenuPopWin.mSingleSelectionAdapter.setIndexSelection(lv_single_Item);
                mTopMenuPopWin.mTopMenuAdapter.setIndexSelection(lv_single_Item);
                TopMenuClick(listMenuItem, position);
                mTopMenuPopWin.dismiss();
            }
        });
    }

    /**
     * 设置title菜单
     */
    public void setTitleMenu() {
        mTitleMenuPopWin = new TitleMenuPopupWindow(WebAppActivity.this);
        if (farstTitleMune) {
            listTitleMenuItem.remove(0);
            farstTitleMune = false;
        }
        mTitleMenuPopWin.mTopMenuAdapter.setData(listTitleMenuItem);
        mTitleMenuPopWin.showPopupWindow(mLl_back);
        mTitleMenuPopWin.setOnActionClickListener(new TitleMenuPopupWindow.OnActionClickListener() {
            @Override
            public void onSingleItemClickListener(AdapterView<?> parent, View view, int position, long id) {
                lv_single_Item = position;
                mTitleMenuPopWin.mTopMenuAdapter.setIndexSelection(lv_single_Item);
                TopMenuClick(listTitleMenuItem, position);
                mTitleMenuPopWin.dismiss();
            }
        });
    }

    /**
     * 下一页
     *
     * @param url    地址
     * @param action 截取的action命令
     */
    @Override
    public boolean onNextPage(String url, String action) {
        Log.i(TAG, "onNextPage()===action:" + action + "   url:" + url);
        if (action != null) {
            // 访问URL不是http开头的,需要把登录地址前缀补上
            if (!url.startsWith("kitapps") && !url.startsWith("http")) {
                url = AppConfing.CTTQ_BASE_URL + url;
            }

            // 相同的url就不打开，避免重复打开多个页面
            if (urlStr.equals(url)) return false;
            switch (action) {
                case CMD.action_nextPage:
                case CMD.action_showModelPage:
                case CMD.action_showModelSearchPage:
//                if (url.equals("http://test.mobisoft.com.cn/qas/v10/app_Salary/html/Salarycheck.html?action=nextPage")) return false;
                    //  handler.sendEmptyMessage(1); // 薪资查询必须跳空白页加载数据才能进入，刷新后死循环(刷新是为了解决某些页面本地跳转)
                    openNextWebActivity(url, action);
                    return true;
                case CMD.action_homepage:
                case CMD.action_exit:
                    ActivityCollector.finishAll(); // 销毁所有的webactivity

                    return true;
                case CMD.action_closePageAndRefreshAndPop:
                    ToastUtil.showShortToast(this, CMD.action_closePageAndRefreshAndPop);
                    return true;
                case CMD.action_closePageAndPop:
                    ToastUtil.showShortToast(this, CMD.action_closePageAndPop);
                    return true;
            }
        }
        return false;
    }

    @Override
    public WebResourceResponse onSIRNextPage(String url, String action) {
        if (urlStr.equals(url)) return null;
        switch (action) {
            case CMD.action_nextPage:
            case CMD.action_showModelPage:
            case CMD.action_showModelSearchPage:
                openNextWebActivity(url, action);
                break;
            case CMD.action_homepage:
            case CMD.action_exit:
                ActivityCollector.finishAll(); // 销毁所有的webactivity
//                ActivityCollector.goHomePage();// 返回第一次打开的webactivity
                break;
            case CMD.action_closePageAndRefreshAndPop:
                ToastUtil.showShortToast(this, CMD.action_closePageAndRefreshAndPop);
                break;
            case CMD.action_closePageAndPop:
                ToastUtil.showShortToast(this, CMD.action_closePageAndPop);
                break;
        }
        WebResourceResponse res;
        try {
            PipedOutputStream out = new PipedOutputStream();
            PipedInputStream in = new PipedInputStream(out);
            // 返回一个错误的响应，让连接失败，webview内就不跳转了(解决本activity的webview跳转问题)
            res = new WebResourceResponse("html", "utr-8", in);
        } catch (IOException e) {
            e.printStackTrace();
            // 返回一个错误的响应，让连接失败，webview内就不跳转了(解决本activity的webview跳转问题)
            res = new WebResourceResponse("html", "utr-8", in);
            return res;
        }
        return res;
    }


    /**
     * 打开下一个WebActivity
     *
     * @param url    地址
     * @param action 截取的action命令
     */
    protected void openNextWebActivity(String url, String action) {
        url = url.replace("&action=nextPage", "");
        Bundle bunde = new Bundle();
        bunde.putString(URL, url);

        if (accountStr != null) bunde.putString(ACCOUNT, accountStr);
        if (titleColor != 0) bunde.putInt(TITLECOLOR, titleColor);
        // 是否显示沉浸式状态栏
        if (isSystemBarShow) {
            bunde.putBoolean(IS_SYSTEM_BAR_SHOW, true);
            bunde.putInt(SYSTEM_BAR_COLOR, systemBarColor);
        }
        // 是否显示转圈
        if (action.equals(CMD.action_showModelPage)) bunde.putBoolean(SHOWMOUDLE, true);
        // 搜索页面没有title模式 （非必须，有默认值）
        if (action.equals(CMD.action_showModelSearchPage))
            bunde.putBoolean(SHOWMOUDLESEARCHPAGE, true);
        /// activity是否支持打开方式 （非必须，有默认值） 转场动画
        if (isTransitionModeEnable) {
            bunde.putBoolean(IS_TRANSITION_MODE_ENABLE, true);
            bunde.putString(IS_TRANSITION_MODE, isTransitionMode);
        }
        Intent intent = new Intent();
        intent.setClass(WebAppActivity.this, WebAppActivity.class);
        intent.putExtras(bunde);
        startActivityForResult(intent, INTENT_REQUEST_CODE);
    }

    /**
     * 关闭当前页面
     *
     * @param url    地址
     * @param action 截取的action命令
     * @return false
     */
    @Override
    public boolean onClosePage(String url, String action) {
        if (action != null) {
            if (action.equals(CMD.action_closePage)) {
                ActivityCollector.removeActivity(this);
                finish();
                return true;
            }
        } else {
            finish();
        }
        return false;
    }

    /**
     * 关闭当前返回主页面
     *
     * @param url    地址
     * @param action 截取的action命令
     * @return true
     */
    @Override
    public boolean onClosePageReturnMain(String url, String action) {
        Intent mIntent = new Intent();
        mIntent.putExtra(REFRESH, true);
//        mIntent.putExtra(REFRESH_URL, url.replace("?action=closePageAndRefresh", "").trim());
        setResult(INTENT_REQUEST_CODE, mIntent);
        // loadUrl(url);
        this.finish();
        return true;
    }

    /**
     * startActivityForResult返回值
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        switch (requestCode) {
            case PICK_IMAGE_ACTIVITY_REQUEST_CODE:  // 启动系统相册获取照片
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:// 拍照 拍照
                if (resultCode == RESULT_OK && resultListener != null) {
                    resultListener.onActivityResult(WebAppActivity.this, mWebViewExten, requestCode, resultCode, data);
                }
                break;
            case INTENT_REQUEST_CODE:
                if (data.getBooleanExtra(REFRESH, false)) {
                    urlStr = TextUtils.isEmpty(data.getStringExtra(REFRESH_URL)) ? urlStr : data.getStringExtra(REFRESH_URL);
                    Log.e(TAG, urlStr);
                    // loadUrl(urlStr);
                    urlTitle = mWebViewExten.getTitle();
                    mWebViewExten.reload();  //加载刷新
                    //  refresh();
                }
                break;
            case REQUEST_CODE_MAP: // 地图
                int flag = data.getIntExtra("flag", 0);
                if (flag == 3) {// 定位失败
                    break;
                } else {// 定位成功
                    latitude = data.getDoubleExtra("latitude", 0);
                    longitude = data.getDoubleExtra("longitude", 0);
                    userLocationAddress = data.getStringExtra("address");

                    mWebViewExten.excuteJSFunction(areaFunction, Utils.IN_PARAMETER_FOR_ADDR, userLocationAddress, latitude + "", longitude + "");
                }
                break;
            case IMAGE_FROM_CAMERA:
                if (resultCode == RESULT_OK) {
                    File f = new File(Environment.getExternalStorageDirectory() + "/" + "ideaTemp" + "/" + Utils.TEMP_IMAGE_CAMERA);
                    try {
                        Uri u = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),
                                f.getAbsolutePath(), null, null));
                        Utils.copyPhotoToTemp(WebAppActivity.this, u);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case Utils.IMAGE_FROM_PHOTOS:
                if (resultCode == RESULT_OK) {
                    Utils.copyPhotoToTemp(WebAppActivity.this, data.getData());
                }
                break;
            case INTENT_REQUEST_QRCODE:
//                String json2 = getFormatJavascript(data.getStringExtra(FUNCTION), data.getStringExtra(QrcodeActivity.RESULT_STRING));
//                loadUrl(json2);
                break;
            case INTENT_REQUEST_GESTURE:
                if (data.getBooleanExtra("setGesture", false)) {
                    JSONObject myJsonObject = null;
                    try {
                        myJsonObject = new JSONObject();
                        myJsonObject.put("setGesture", "true");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String json = String.format("javascript:" + functionNameStr + "(%s)", myJsonObject);
                    loadUrl(json);
                }
                break;
            case CONTACTS_CODE:// 通讯录返回
                if (resultCode == CONTACTS_CODE) {

                    // String json1 = "{"+ "\"nickname\"" +":"+"\""+ "lixiang" + "\"" + "," + "\"username\"" + ":" + "\"" + "lixiang" + "\"" + "}";
                    //  String a = "{" + "\"base64\"" + ":" + "\""+ bytes +"\""+ "," + "\"num\"" + ":" + "\"" + selectPicNum+" \"" +"}";
                    /**
                     * js调用方法重点（ios是以字符串形势传参，这里为保持统一）
                     * "(%s)" :代表以对象形势传参
                     * "(" + "'%s')" ：代表以字符串形势传参
                     */
                    //  String json = String.format("javascript:" + data.getStringExtra("function") + "(%s)", data.getStringExtra("OrganizeBean"));
                    String json = String.format("javascript:" + data.getStringExtra("function") + "(" + "'%s')", data.getStringExtra("OrganizeBean"));
                    loadUrl(json);
                }
                break;
        }
    }


    /**
     * 上拉刷新的线程
     */
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, " handler" + msg.what);
            switch (msg.what) {
                case 0:
//                    mWebViewExten.excuteJSFunction(CMD.cmd_setFooterRefreshing, "");
                    handler.sendEmptyMessage(2);
                    break;
                case 1:
                    firstComeIn = true;
                    mSwipeRefreshLayout.setRefreshing(true);// 刷新

                    urlTitle = mWebViewExten.getTitle();
//                    loadUrl(urlStr);
//                    //mWebViewExten.reload();
//
////                    loadUrl(urlStr);
//                    mWebViewExten.reload();
//                    loadUrl(urlStr);
                    mWebViewExten.reload();


                    break;
                case 2:
                    mSwipeRefreshLayout.setRefreshing(false);// 停止刷新
                    mSwipeRefreshLayout.setLoading(false);// 停止加载
                    break;
                case 3:// 关闭当前 页面
                    boolean falg = (boolean) msg.obj;
                    if (falg) finish();
                    break;
                case 4:// 返回WebView的上一页面
                    boolean falg1 = (boolean) msg.obj;
                    if (falg1) {//goBack()表示返回WebView的上一页面
                        if (mWebViewExten.canGoBack()) {
                            mWebViewExten.goBack();
                            isNeedClose = true;
                        } else // 结束当前页面
                            finish();
                    }
                    break;
                case 5:// webView Cmd
                    String url = msg.obj.toString();
                    Map<String, String> param = parseUrl(url);
                    String parameter = param.get("para");
                    String function = param.get("callback");
                    Pattern p = Pattern.compile("\\//(.*?)\\?");//正则表达式，取=和|之间的字符串，不包括=和|
                    Matcher m = p.matcher(url);
                    String cmd = null;
                    while (m.find()) {
                        cmd = m.group();
                    }
                    if (cmd != null) {
                        cmd = cmd.substring(2, cmd.length() - 1);
//                        onCommand(cmd, parameter, function);
                        CmdrBuilder.getInstance()
                                .setContext(WebAppActivity.this)
                                .setWebView(mWebViewExten)
                                .setCmd(cmd)
                                .setParameter(parameter)
                                .setCallback(function)
                                .doMethod();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 下拉刷新上拉加载
     */
    public void refresh() {
        handler.sendEmptyMessage(1);
    }

    /**
     * 执行刷新操作
     */
    @Override
    public void onRefresh() {
        refresh();
    }

    @Override
    protected void onResume() {
//        handler.sendEmptyMessage(1);
        super.onResume();
        Intent intent = new Intent(this, ProxyService.class);
        bindService(intent, mProxyConnection, Context.BIND_AUTO_CREATE | Context.BIND_NOT_FOREGROUND);

    }


    @Override
    protected void onDestroy() {
        //魅族和三星Galaxy 5.0webView 问题Android Crash Report - Native crash at /system/lib/libc.so caused by webvi
//        mWebViewExten.clearCache(true);
        if (mWebViewExten != null) {
            mWebViewExten.clearHistory();
            if (mWebViewExten.dialog != null && mWebViewExten.dialog.isShowing())
                mWebViewExten.dialog.dismiss();

            ViewGroup parent = (ViewGroup) mWebViewExten.getParent();
            if (parent != null) {
                parent.removeView(mWebViewExten);
            }
            SettingProxy.revertBackProxy(mWebViewExten, getApplication().getClass().getName());

            mWebViewExten.removeAllViews();
            mWebViewExten.destroy();
            mWebViewExten = null;
//            if(photoInfoList!=null)photoInfoList.clear();

        }

        Log.e(TAG, "销毁页面！onDestroy");
        // 解决退出activity时 dialog未dismiss而报错的bug
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        } catch (Exception e) {
            System.out.println("myDialog取消，失败！");
        }
        //解除 广播 receiveBroadCast
        if (receiveBroadCast != null) unregisterReceiver(receiveBroadCast);

        // cleanCacheAndCookie();
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBound) {
            unbindService(mProxyConnection);
        }
    }

    // 点击
    @Override
    public void onClick(View v) {
        Log.e(TAG, "onClick: id = " + v.getId());
        if (v.getId() == R.id.ll_right) { //  右上菜单图片点击事件
            if (showModel) { // 左右菜单,右菜单点击事件
                TopMenuClick(listMenuItem, 1);
            } else {
                if (listMenuItem.size() == 1) {
                    TopMenuClick(listMenuItem, 0);
                } else {
                    setTopRightMenu();
                }
            }
        } else if (v.getId() == R.id.iv_head_left) { // 左上角 返回图标 事件
            onFinish(TYPE_ACTIVITY);
        } else if (v.getId() == R.id.tv_head_left) { // 左上角 取消 事件
            TopMenuClick(listMenuItem, 0);
        } else if (v.getId() == R.id.ll_head_title) { // title点击事件
            if (listTitleMenuItem.size() == 1) {
                TopMenuClick(listTitleMenuItem, 0);
            } else {
                setTitleMenu();
            }
        }
    }

    /**
     * 头部菜单点击事件
     *
     * @param list     菜单列表
     * @param position 点击的item的下标
     */
    protected void TopMenuClick(List<MeunItem> list, int position) {
        Log.e(TAG, "点击   +  :" + position);
        if (!TextUtils.isEmpty(list.get(position).getCallback())) {// 回调函数
            String json = String.format("javascript:" + list.get(position).getCallback() + "(%s)", "");
            loadUrl(json);
        } else if (!TextUtils.isEmpty(list.get(position).getUrl())) {// 启动新页面
            onNextPage(list.get(position).getUrl(), CMD.action_nextPage);
        }
    }

    /**
     * 开启状态栏
     *
     * @param activity 环境
     * @param on       on
     */
    @TargetApi(19)
    protected void setTranslucentStatus(Activity activity, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }

        win.setAttributes(winParams);

    }


    /**
     * 关闭页面之前调用 关闭检测方法
     * 是否关闭当前页面
     *
     * @param type TYPE_WEB（调用web View.goback()）,TYPE_ACTIVITY(调用this.finish())
     */
    private void onFinish(final String type) {
        if (Build.VERSION.SDK_INT >= 19)
            mWebViewExten.evaluateJavascript("closeAllQuestion('true')", new ValueCallback<String>() {

                @Override
                public void onReceiveValue(String value) {
                    // value true: 可以继续关闭当前页面
                    // value false: 不可以继续关闭当前页面   （停留在当前页面执行其他操作）
                    Log.e("back", "value  " + value);
                    Message message = new Message();
                    if (TextUtils.equals(TYPE_WEB, type))
                        message.what = 4;// 网页当前页面返回
                    else
                        message.what = 3;// actvity结束
                    message.obj = !TextUtils.equals(value, "false");
                    handler.sendMessage(message);

                }
            });
        else { // 4.3及其以下的暂时没有处理
            if (!isNeedClose) { // 调用 检查方法
                String json2 = String.format("javascript:closeAllQuestion" + "(" + "'%s')", false);
                loadUrl(json2);
            } else {// 直接关闭
                Message message = new Message();
                message.what = 3;
                message.obj = true;
                handler.sendMessage(message);
            }
        }
    }

    @Override
    @JavascriptInterface
    public void finish() {
        super.finish();
        if (isTransitionModeEnable) {
            if (FunctionConfig.TransitionMode.LEFT.name().equals(isTransitionMode)) {
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
            } else if (FunctionConfig.TransitionMode.RIGHT.name().equals(isTransitionMode)) {
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
            } else if (FunctionConfig.TransitionMode.TOP.name().equals(isTransitionMode)) {
                overridePendingTransition(R.anim.top_in, R.anim.top_out);
            } else if (FunctionConfig.TransitionMode.BOTTOM.name().equals(isTransitionMode)) {
                overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
            } else if (FunctionConfig.TransitionMode.SCALE.name().equals(isTransitionMode)) {
                overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
            } else if (FunctionConfig.TransitionMode.FADE.name().equals(isTransitionMode)) {
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_CAMERA:// 相机权限

                break;
            case REQUEST_CODE_GALLERY_B: // 打开相册

                break;
            case NICK_REQUEST_CAMERA_CODE: // 耐克获取相册

                takePicture();

                break;
        }
    }


    /**
     * 定义一个BroadcastReceiver广播接收类：
     */
    public class ReceiveBroadCast extends BroadcastReceiver {

        /**
         * web View回调js方法
         */
        private String mfunction;
        /**
         * 创建广播接收者的action
         */
        private String mActionName;

        /**
         * 初始化广波接收者
         *
         * @param actionName action
         * @param function   web View回调js方法
         */
        public ReceiveBroadCast(String actionName, String function) {
            super();
            this.mfunction = function;
            this.mActionName = actionName;
        }

        @Override
        public void onReceive(Context context, Intent data) {

            String actionName = data.getAction();
            String function = data.getStringExtra("function");

            Log.e("12345", "actionName" + actionName + "function" + mfunction);
            if (TextUtils.equals(mActionName, actionName)) {
                // 得到广播中得到的数据，并显示出来
                Bundle extras = data.getExtras();
                if (extras != null) {
                    String data1 = extras.getString("data");
                    loadUrl(String.format("javascript:" + mfunction + "('%s')", data1));

                }
            }
        }
    }


    /**
     * js 调用原生方法 webView.goBack()
     */
    public class JsControl {
        /**
         * html5 调用原生webView.goBack()方法
         */
        @JavascriptInterface
        public void goBackPage() {
            Message message = new Message();
            message.what = 4;// 网页当前页面返回
            message.obj = true;
            handler.sendMessage(message);

        }
    }

    /**
     * 加载轻量级 webView
     *
     * @param url    html地址
     * @param action action命令
     * @return true
     */
    @Override
    public boolean onLightweightPage(String url, String action) {
        return true;
    }

    /**
     * 是否设置过webView代理
     */
    private boolean mBound;
    private ServiceConnection mProxyConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName component) {
            mBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName component, IBinder binder) {
            IProxyCallback callbackService = IProxyCallback.Stub.asInterface(binder);
            if (callbackService != null) {
                try {
                    callbackService.getProxyPort(new IProxyPortListener.Stub() {
                        @Override
                        public void setProxyPort(final int port) throws RemoteException {
                            if (port != -1) {
                                Log.d(TAG, "Local proxy is bound on " + port);

                            } else {
                                Log.e(TAG, "Received invalid port from Local Proxy,"
                                        + " PAC will not be operational");
                            }
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            mBound = true;
        }
    };
}
