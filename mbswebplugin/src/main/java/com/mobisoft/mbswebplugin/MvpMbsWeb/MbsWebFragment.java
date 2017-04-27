package com.mobisoft.mbswebplugin.MvpMbsWeb;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mobisoft.mbswebplugin.Cmd.CMD;
import com.mobisoft.mbswebplugin.Cmd.CmdrBuilder;
import com.mobisoft.mbswebplugin.Entity.MeunItem;
import com.mobisoft.mbswebplugin.Entity.TopMenu;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebviewListener;
import com.mobisoft.mbswebplugin.MbsWeb.WebAppInterface;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Base.Preconditions;
import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.base.AppConfing;
import com.mobisoft.mbswebplugin.base.BaseApp;
import com.mobisoft.mbswebplugin.base.Recycler;
import com.mobisoft.mbswebplugin.proxy.server.ProxyConfig;
import com.mobisoft.mbswebplugin.refresh.BGAMoocStyleRefreshViewHolder;
import com.mobisoft.mbswebplugin.refresh.BGARefreshLayout;
import com.mobisoft.mbswebplugin.utils.ActivityCollector;
import com.mobisoft.mbswebplugin.utils.ToastUtil;
import com.mobisoft.mbswebplugin.utils.Utils;
import com.mobisoft.mbswebplugin.view.SingleSeletPopupWindow;
import com.mobisoft.mbswebplugin.view.TitleMenuPopupWindow;
import com.mobisoft.mbswebplugin.view.TopMenuPopupWindowActivity;
import com.squareup.leakcanary.RefWatcher;
import com.squareup.picasso.Picasso;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mobisoft.mbswebplugin.base.AppConfing.INTENT_REQUEST_CODE;
import static com.mobisoft.mbswebplugin.base.AppConfing.TITLECOLOR;
import static com.mobisoft.mbswebplugin.base.AppConfing.TYPE_ACTIVITY;
import static com.mobisoft.mbswebplugin.utils.UrlUtil.parseUrl;
import static java.lang.System.in;

/**
 *
 */
public class MbsWebFragment extends Fragment implements MbsWebPluginContract.View, View.OnClickListener, WebAppInterface,
        HybridWebviewListener, Recycler.Recycleable, BGARefreshLayout.BGARefreshLayoutDelegate {
    public static final String TAG = "MBS_WEBFRG";
    private WebPluginPresenter presenter;
    /**
     * 下拉刷新控件
     */
    private BGARefreshLayout bgaRefreshLayout;
    /**
     * 核心组件 webView
     */
    private HybridWebView mWebViewExten;
    /**
     * 上下文环境
     */
    private Context mContext;
    /**
     * 初次进来webview 的 url （必须要）
     */
    public static final String URL = "url";
    /**
     * 进度条
     */
    public ProgressDialog mProgressDialog;
    /**
     * 用于记录第一次onPageFinished进来的
     */
    private boolean firstComeIn = true;
    /**
     * 获取网页标题
     */
    private String urlTitle;
    /**
     * 当前页面是否需要关闭
     */
    public boolean isNeedClose = true;
    /**
     * 初始化webview时用户account （必须要）
     */
    public static final String ACCOUNT = "account";

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
     * 是否开始隐藏导航栏 （非必须，有默认值）false 为 不隐藏，true 为隐藏导航栏
     */
    public static final String IS_HIDENAVIGATION = "isHideNavigation";
    /**
     * activity是否支持打开方式 （非必须，有默认值）
     */
    public static final String IS_TRANSITION_MODE_ENABLE = "isTransitionModeEnable";
    /**
     * activity打开方式 （非必须，有默认值）
     */
    public static final String IS_TRANSITION_MODE = "isTransitionMode";
    /***
     * 下拉刷新 延时取消刷新（等待initpage方法执行结束）
     */
    public static final int DELAY_MILLIS = 200;
    /**
     * TYPE_WEB 按返回键的类型 调用web View.goback()方法
     */
    public static final String TYPE_WEB = "pageWeb";
    /**
     * 缓存路径
     */
    private static final String APP_CACAHE_DIRNAME = "/webcache";
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
    public Toolbar toolbar;

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
    /**
     * 搜索头布局
     */
    private LinearLayout ll_search;
    /**
     * seeting头布局 相关
     */
    private RadioGroup rg_all;
    private LinearLayout ll_center_normal;

    /***
     * 右边菜单 选项
     */
    protected List<MeunItem> listMenuItem = new ArrayList<>();
    /**
     * 中间菜单选项
     */
    protected List<MeunItem> listTitleMenuItem = new ArrayList<>();
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
    /***
     * 是否隐藏 导航栏 true :隐藏  false：显示
     */
    protected boolean is_hidenavigation;
    /**
     * 是否设置过 h5命令中标题 false:未设置命令标题
     */
    private boolean issetTitle = false;
    /**
     * 是否 清除过任务栈 false： 没有
     ***/
    public boolean isClearTask = false;
    /**
     * 上拉刷新的线程
     */
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i(ContentValues.TAG, " handler" + msg.what);
            switch (msg.what) {
                case 0:
                case 2: // 停止刷新
                    if (bgaRefreshLayout != null) {
                        bgaRefreshLayout.endRefreshing();
                        bgaRefreshLayout.endLoadingMore();
                    }


//                    bgaRefreshLayout.setRefreshing(false);// 停止刷新
//                    bgaRefreshLayout.setLoading(false);// 停止加载
                    break;
                case 1: //  开始刷新
                    firstComeIn = true;
                    bgaRefreshLayout.beginRefreshing();
//                    bgaRefreshLayout.setRefreshing(true);// 刷新
                    urlTitle = mWebViewExten.getTitle();
                    mWebViewExten.reload();
                    break;
                case 3:// 关闭当前 页面
                    boolean falg = (boolean) msg.obj;
                    if (falg) presenter.finishActivity();
                    break;
                case 4:// 返回WebView的上一页面
                    boolean falg1 = (boolean) msg.obj;
                    if (falg1) {//goBack()表示返回WebView的上一页面
                        if (mWebViewExten.canGoBack()) {
                            mWebViewExten.goBack();
                            isNeedClose = true;
                        } else // 结束当前页面
                            presenter.finishActivity();
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
                                .setContext(mContext)
                                .setWebView(mWebViewExten)
                                .setPresenter(presenter)
                                .setContractView(MbsWebFragment.this)
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

    public MbsWebFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MbsWebFragment.
     */
    public static MbsWebFragment newInstance(Bundle bundle) {
        MbsWebFragment fragment = new MbsWebFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");

        mContext = getContext();
        if (getArguments() != null) {
            isRefreshEnable = getArguments().getBoolean(IS_REFRESH_ENABLE, false);
            urlStr = getArguments().getString(URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_mbs_web, container, false);
        initViews(inflate);
        if (getArguments() != null)
            initData(getArguments());
        setWebSetting();
        setEvents();
        return inflate;
    }

    private void initData(Bundle bundle) {
        AppConfing.ACCOUNT = accountStr;
        titleColor = bundle.getInt(TITLECOLOR, 0);
        showModel = bundle.getBoolean(SHOWMOUDLE, false);
        showModelSearchPage = bundle.getBoolean(SHOWMOUDLESEARCHPAGE, false);
        animRes = bundle.getInt(ANIMRES, R.anim.in_from_right);
        systemBarColor = bundle.getInt(SYSTEM_BAR_COLOR, Color.parseColor(getString(R.string.man_system_bar_color)));
        titleCenterTextColor = bundle.getInt(TITLE_CENTER_TEXT_COLOR, Color.WHITE);
        titleLeftTextColor = bundle.getInt(TITLE_LEFT_TEXT_COLOR, Color.WHITE);
        titleRightTextColor = bundle.getInt(TITLE_RIGHT_TEXT_COLOR, Color.WHITE);
        iconBack = bundle.getInt(ICON_BACK, R.drawable.back);
        iconTitleCenter = bundle.getInt(ICON_TITLE_CENTER, R.drawable.ic_gf_triangle_arrow);
        iconTitleRight = bundle.getInt(ICON_TITLE_RIGHT, R.drawable.ic_add_black_48dp);
        isLeftTextShow = bundle.getBoolean(IS_LEFT_TEXT_SHOW, false);
        isLeftIconShow = bundle.getBoolean(IS_LEFT_ICON_SHOW, false);
        isSystemBarShow = bundle.getBoolean(IS_SYSTEM_BAR_SHOW, true);
        isRefreshEnable = bundle.getBoolean(IS_REFRESH_ENABLE, false);
        isTransitionModeEnable = bundle.getBoolean(IS_TRANSITION_MODE_ENABLE, true);
        isTransitionMode = bundle.getString(IS_TRANSITION_MODE);
        is_hidenavigation = bundle.getBoolean(IS_HIDENAVIGATION, false);
//        Log.e(TAG, "==isTransitionMode:" + isTransitionMode + " enmu:" + FunctionConfig.TransitionMode.LEFT.name());
//        Log.e(TAG, "is_hidenavigation:" + is_hidenavigation);
        if (is_hidenavigation || showModel || showModelSearchPage || urlStr.equals(AppConfing.CTTQ_BASE_URL + "/tianxin/aaaao20041-test/html/index.html"))
            hideTitle();
        if (TextUtils.isEmpty(accountStr)) accountStr = "error";
        if (titleColor != 0) { // 设置title颜色 和沉浸式菜单栏
            toolbar.setBackgroundColor(titleColor);
//            if (isSystemBarShow)
//                initSystemBar(WebAppActivity.this, systemBarColor);
        }
        if (isLeftTextShow) tv_head_left.setVisibility(View.GONE);
        toolbar.setNavigationIcon(iconBack);
        if (isLeftIconShow) iv_head_left.setVisibility(View.GONE);

    }

    /**
     * 设置事件
     */
    private void setEvents() {
        bgaRefreshLayout.setDelegate(this);

//        bgaRefreshLayout.setOnRefreshListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinish(TYPE_ACTIVITY);
            }
        });
        tv_head_left.setOnClickListener(this);
        tv_head_left.setClickable(false);
        ll_head_title.setOnClickListener(this);
        ll_right.setOnClickListener(this);
        ll_head_title.setClickable(false);
        ll_right.setClickable(false);
        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(mContext.getApplicationContext(), false);
        moocStyleRefreshViewHolder.setOriginalImage(ProxyConfig.getConfig().getLoadingIc());
        moocStyleRefreshViewHolder.setUltimateColor(ProxyConfig.getConfig().getLoadingBg());
        moocStyleRefreshViewHolder.setSpringDistanceScale(100);
        bgaRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);
    }

    /***
     * 初始化view
     *
     * @param inflate 视图
     */
    protected void initViews(View inflate) {
        bgaRefreshLayout = (BGARefreshLayout) inflate.findViewById(R.id.swipeRefreshLayout);
        mWebViewExten = new HybridWebView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebViewExten.setLayoutParams(params);
        bgaRefreshLayout.addWebView(mWebViewExten);
//        mWebViewExten = (HybridWebView) inflate.findViewById(R.id.webViewExten);
        mWebViewExten.setListener(this);
        this.loadUrl(urlStr);

        toolbar = (Toolbar) inflate.findViewById(R.id.web_tool_bar);

        mLl_back = (LinearLayout) toolbar.findViewById(R.id.ll_back);
        ll_head_title = (LinearLayout) toolbar.findViewById(R.id.ll_head_title);
        mTv_head_title = (TextView) toolbar.findViewById(R.id.tv_head_title);
        tv_head_left = (TextView) toolbar.findViewById(R.id.tv_head_left);
        tv_head_right = (TextView) toolbar.findViewById(R.id.tv_head_right);
        ll_right = (LinearLayout) toolbar.findViewById(R.id.ll_right);
        iv_head_left = (ImageView) toolbar.findViewById(R.id.iv_head_left);
        img_right = (ImageView) toolbar.findViewById(R.id.img_right);
        iv_head_title_menu = (ImageView) toolbar.findViewById(R.id.iv_head_title_menu);

        //nike
        ll_search = (LinearLayout) inflate.findViewById(R.id.search_ll);


//        mProgressDialog = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
//        mProgressDialog.setCanceledOnTouchOutside(false);
//        mProgressDialog.setMessage("正在加载...");
//        mProgressDialog.show(); // 本地不show，拦截url响应事件
        presenter.setProxy();
    }

    /**
     * 设置webview
     */
    private void setWebSetting() {
        // 设置可以使用localStorage
        mWebViewExten.getSettings().setDomStorageEnabled(true);
        // 应用可以有缓存
        mWebViewExten.getSettings().setAppCacheEnabled(true);
        mWebViewExten.getSettings().setAllowFileAccess(true);
        mWebViewExten.getSettings().setAppCachePath(mWebViewExten.getContext().getCacheDir().getAbsolutePath());
        // 缓存模式
        mWebViewExten.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        mWebViewExten.setVerticalScrollbarOverlay(true); //指定的垂直滚动条有叠加样式
        mWebViewExten.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        mWebViewExten.getSettings().setUseWideViewPort(true);//扩大比例的缩放 //为图片添加放大缩小功能
        mWebViewExten.getSettings().setLoadWithOverviewMode(true);
        mWebViewExten.getSettings().setDisplayZoomControls(false);
        mWebViewExten.getSettings().setSupportZoom(true); // 支持缩放
        mWebViewExten.getSettings().setBuiltInZoomControls(true);// 设置出现缩放工具
        mWebViewExten.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);// 默认缩放模式
        enableHTML5AppCache();
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
        String cacheDirPath = mContext.getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
        Log.i(ContentValues.TAG, "cacheDirPath=" + cacheDirPath);
        //设置数据库缓存路径
        mWebViewExten.getSettings().setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        mWebViewExten.getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        mWebViewExten.getSettings().setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        Log.e(TAG, "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "onDetach");
    }


    /**
     * 禁止刷新
     */
    @Override
    public void forbiddenRefresh() {
        bgaRefreshLayout.setPullDownRefreshEnable(false);
    }

    /**
     * 加载url
     *
     * @param url 地址
     */
    @Override
    public void loadUrl(String url) {
        mWebViewExten.loadUrl(url);
    }

    @Override
    public String getUrl() {
        return urlStr;
    }

    @Override
    public void setPresenter(MbsWebPluginContract.Presenter presenter) {
        this.presenter = (WebPluginPresenter) Preconditions.checkNotNull(presenter);
    }

    /**
     * 下一个
     *
     * @param url    地址
     * @param action 命令
     */
    @Override
    public void openNextWebActivity(String url, String action) {
        url = url.replace("&action=nextPage", "");
        Bundle bunde = new Bundle();
        bunde.putString(URL, url);

        if (accountStr != null) bunde.putString(ACCOUNT, accountStr);
        if (titleColor != 0) bunde.putInt(TITLECOLOR, titleColor);
        // 是否显示沉浸式状态栏
//        if (isSystemBarShow) {
//            bunde.putBoolean(IS_SYSTEM_BAR_SHOW, true);
//            bunde.putInt(SYSTEM_BAR_COLOR, systemBarColor);
//        }
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
        intent.putExtras(bunde);
//        intent.setClass(mContext, MbsWebActivity.class);
        presenter.startActivityForResult(intent, INTENT_REQUEST_CODE);
//        startActivityForResult(intent, INTENT_REQUEST_CODE);
    }

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
        return false;
    }

    @Override
    public void hideTitle() {
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void TopMenuClick(List<MeunItem> list, int position) {
        Log.e(ContentValues.TAG, "点击   +  :" + position);
        if (!TextUtils.isEmpty(list.get(position).getCallback())) {// 回调函数
            String json = String.format("javascript:" + list.get(position).getCallback() + "(%s)", "");
            loadUrl(json);
        } else if (!TextUtils.isEmpty(list.get(position).getUrl())) {// 启动新页面
            presenter.nextPage(list.get(position).getUrl(), CMD.action_nextPage);
        }
    }

    @Override
    public void setTopRightMenu() {
        mTopMenuPopWin = new TopMenuPopupWindowActivity(mContext);
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

    @Override
    public void setTitleMenu() {
        mTitleMenuPopWin = new TitleMenuPopupWindow(mContext);
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

    @Override
    public void setTitleBg(String color) {
        toolbar.setBackgroundColor(Color.parseColor(TextUtils.isEmpty(color) ? "#0089F6" : color));
        mTv_head_title.setTextColor(Color.parseColor(TextUtils.isEmpty(color) ? "#FFFFFF" : "#000000"));
    }

    @Override
    public boolean getIsClearTask() {
        return isClearTask;
    }

    @Override
    public void setIsClearTask(boolean b) {
        this.isClearTask = b;
    }

    @Override
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
                    Picasso.with(mContext).load(menu.getItem().get(0).getIcon()).into(img_right);
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

    @Override
    public void showHud(String action, String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.setMessage(message);

        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
//        mProgressDialog.show(); // 本地不show，拦截url响应事件
    }

    @Override
    public void hideHud() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
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
    public void onDestroy() {

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
            presenter.onDestroy();
            mWebViewExten.removeAllViews();
            mWebViewExten.destroy();
            Recycler.release(this);
            super.onDestroy();

//            if(photoInfoList!=null)photoInfoList.clear();

        }

        Log.e(ContentValues.TAG, "销毁页面！onDestroy");
        // 解决退出activity时 dialog未dismiss而报错的bug
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        } catch (Exception e) {
            System.out.println("myDialog取消，失败！");
        }

        // cleanCacheAndCookie();
        RefWatcher refWatcher = BaseApp.getRefWatcher(getActivity());
        refWatcher.watch(this);

    }

    @Override
    public HybridWebView getWebView() {
        return mWebViewExten;
    }

    @Override
    public void onClick(View v) {
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
        } else if (v.getId() == R.id.web_tool_bar) { // 左上角 返回图标 事件
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

    @Override
    public void setTitle(int type, String title) {
        onTitle(type, title);
    }

    @Override
    public void onCommand(String command, String paramter, String function) {

    }

    @Override
    public void onCommand(WebView view, String url) {
        Message msg = new Message();
        msg.what = 5;
        msg.obj = url;
        handler.sendMessage(msg);
    }

    /**
     * 下一页
     *
     * @param url    地址
     * @param action 截取的action命令
     */
    @Override
    public boolean onNextPage(String url, String action) {

        return presenter.nextPage(url, action);
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
                presenter.onHomePage(url, action);
                break;
            case CMD.action_exit:
                ActivityCollector.finishAll(); // 销毁所有的webactivity
                break;
            case CMD.action_closePageAndRefreshAndPop:
                ToastUtil.showShortToast(mContext, CMD.action_closePageAndRefreshAndPop);
                break;
            case CMD.action_closePageAndPop:
                ToastUtil.showShortToast(mContext, CMD.action_closePageAndPop);
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


    @Override
    public boolean onClosePage(String url, String action) {

        return presenter.onClosePage(url, action);
    }

    @Override
    public boolean onClosePageReturnMain(String url, String action) {
        return presenter.onClosePageReturnMain(url, action);
    }

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
        Log.e(ContentValues.TAG, json2);
        loadUrl(json2);
//        mProgressDialog.dismiss();
        handler.sendEmptyMessageDelayed(0, DELAY_MILLIS);
    }

    @Override
    public boolean onLightweightPage(String url, String action) {
        return presenter.onLightweightPage(url, action);
    }

    @Override
    public void setMainUrl(String url) {
        loadUrl(url);
    }

    @Override
    public String getMainUrl() {
        return urlStr;
    }

    @Override
    public void reloadApp() {
        handler.sendEmptyMessage(1);
    }

    @Override
    public void reload() {
        handler.sendEmptyMessage(1);
//        mWebViewExten.reload();
    }

//    /**
//     * 下拉刷新
//     */
//    @Override
//    public void onRefresh() {
//        handler.sendEmptyMessage(1);
//    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
        Log.i(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
        Log.i(TAG, "onPause");

    }

    @Override
    public void release() {
        Log.i(TAG, "release");
        presenter = null;
        mWebViewExten = null;
        mContext = null;
        mTopMenuPopWin = null;
        mTitleMenuPopWin = null;
        mSingleSeletPopupWindow = null;
        mProgressDialog = null;
        bgaRefreshLayout = null;
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        tv_head_right.setText("");
        handler.sendEmptyMessage(1);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
