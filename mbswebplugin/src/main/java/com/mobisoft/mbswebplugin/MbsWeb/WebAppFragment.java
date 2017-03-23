package com.mobisoft.mbswebplugin.MbsWeb;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mobisoft.mbswebplugin.Entity.BottomItem;
import com.mobisoft.mbswebplugin.Entity.MeunItem;
import com.mobisoft.mbswebplugin.Entity.TopMenu;
import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.base.AppConfing;
import com.mobisoft.mbswebplugin.dao.db.WebViewDao;
import com.mobisoft.mbswebplugin.refresh.SwipeRefreshLayout;
import com.mobisoft.mbswebplugin.utils.ActivityCollector;
import com.mobisoft.mbswebplugin.Cmd.CMD;
import com.mobisoft.mbswebplugin.utils.FileUtils;
import com.mobisoft.mbswebplugin.utils.NetworkUtils;
import com.mobisoft.mbswebplugin.utils.SharedPreferUtil;
import com.mobisoft.mbswebplugin.utils.ToastUtil;
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
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mobisoft.mbswebplugin.view.ImageBrowserActivty.ImageBrowserActivty.DCIM;
import static com.mobisoft.mbswebplugin.view.ImageBrowserActivty.ImageBrowserActivty.IMAGE_RIGHTMEN;
import static com.mobisoft.mbswebplugin.view.ImageBrowserActivty.ImageBrowserActivty.IMAGE_URL;

/***
 *  用fragment的加载html
 */
public class WebAppFragment extends Fragment implements View.OnClickListener, WebAppInterface,
        HybridWebviewListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String FUNCTION = "function";
    public static final String PARAMTER = "paramter";
    /**
     * 请求打开相机
     */
    protected final int REQUEST_CODE_CAMERA = 1000;
    /**
     * 请求打开相册
     */
    protected final int REQUEST_CODE_GALLERY = 1001;
    /**
     * 请求打开相机转 base64
     */
    protected final int REQUEST_CODE_CAMERA_B = 1002;
    /**
     * 请求打开相册转 base64
     */
    protected final int REQUEST_CODE_GALLERY_B = 1003;
    /**
     * 返回上一页布局
     */
    protected LinearLayout mLl_back;
    /**
     * title布局
     */
    protected LinearLayout ll_head_title;
    /**
     * 标题
     */
    protected TextView mTv_head_title;
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
    protected LinearLayout ll_head;

    protected SingleSeletPopupWindow mSingleSeletPopupWindow;

    protected TopMenuPopupWindowActivity mTopMenuPopWin; // 右上角菜单
    protected TitleMenuPopupWindow mTitleMenuPopWin; // title菜单

    public static final int INTENT_REQUEST_CODE = 3;
    public static final int REQUEST_CODE_MAP = 4;
    public static final int REQUEST_CODE_SEARCH = 5;

    public static final int INTENT_REQUEST_QRCODE = 6;

    public static final String REFRESH = "Refresh"; //是否刷新！
    protected static final String TAG = "WebAppFragment"; //log
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

    protected ProgressDialog mProgressDialog;
    protected boolean farstMune = true; // 右上角菜单标志位
    protected boolean farstTitleMune = true; // title菜单标志位
    protected boolean showModel = false; // 左右菜单
    protected boolean showModelSearchPage = false; // 没有title 的搜索
    protected int titleColor = 0; // title 颜色
    protected int animRes = 0; // avtivity进入动画
    protected int systemBarColor = 0; // 沉浸式菜单栏颜色
    protected int titleCenterTextColor = 0; // 标题中间文字颜色颜色
    protected int titleLeftTextColor = 0; // 标题左边文字颜色颜色
    protected int titleRightTextColor = 0; // 标题右边文字颜色颜色
    protected int iconBack = 0; // 标题返回图片
    protected int iconTitleCenter = 0; // 标题中间图片
    protected int iconTitleRight = 0; // 标题右边图片
    protected boolean isLeftTextShow = false; // 是否显示左边“返回”文字
    protected boolean isLeftIconShow = false; // 是否显示左边“返回”图片
    protected boolean isSystemBarShow = true; // 是否显示沉浸式菜单栏
    protected boolean isRefreshEnable = false; // 是否支持刷新

    protected String picFunction;
    protected ImageView iv_head_left;
    // 菜单图标
    protected ImageView img_right;
    // title菜单图标
    protected ImageView iv_head_title_menu;

    protected List<MeunItem> listMenuItem = new ArrayList<MeunItem>();
    protected List<MeunItem> listTitleMenuItem = new ArrayList<MeunItem>();

    //用于记录第一次onPageFinished进来的
    private boolean firstComeIn = true;
    // 根布局
    private View view;
    protected Context mContext;
    /**
     * 是否设置过 h5命令中标题 false:未设置命令标题
     */
    private boolean issetTitle = false;
    /**
     * 消息的广播接受者
     */
    protected ReceiveBroadCast receiveBroadCast;
    /**
     * 相册；路径
     */
    private StringBuilder stringBuilder;

    /**
     * 下载目标对象
     **/
    private Target target;
    /****
     * 是否需要关闭当前界面
     */
    public boolean isNeedClose = true;
    /**
     * 手势登录 会调方法
     */
    private String functionNameStr;
    /**
     * 是否 清除过任务栈 false： 没有
     ***/
    private boolean isClearTask = false;
    /**
     * ?h5携带参数
     *****/
    private String mParamter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_web_app, null);
        initViews();
        initData();
        initEvents();
        return view;
    }

    protected void initViews() {
        mLl_back = (LinearLayout) view.findViewById(R.id.ll_back);
        ll_head_title = (LinearLayout) view.findViewById(R.id.ll_head_title);
        mTv_head_title = (TextView) view.findViewById(R.id.tv_head_title);
        tv_head_left = (TextView) view.findViewById(R.id.tv_head_left);
        tv_head_right = (TextView) view.findViewById(R.id.tv_head_right);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mWebViewExten = (HybridWebView) view.findViewById(R.id.webViewExten);
        ll_right = (LinearLayout) view.findViewById(R.id.ll_right);
        ll_head = (LinearLayout) view.findViewById(R.id.ll_head);
        img_right = (ImageView) view.findViewById(R.id.img_right);
        iv_head_title_menu = (ImageView) view.findViewById(R.id.iv_head_title_menu);
        iv_head_left = (ImageView) view.findViewById(R.id.iv_head_left);
        mTv_head_title.setTextColor(Color.parseColor("#000000"));
        tv_head_left.setTextColor(Color.parseColor("#000000"));
        tv_head_left.setText("关闭");

        enableHTML5AppCache();
    }

    protected void initData() {
        mContext = getActivity();
        stringBuilder = new StringBuilder();
        urlStr = getArguments().getString(URL);
        accountStr = getArguments().getString(ACCOUNT);
        titleColor = getArguments().getInt(TITLECOLOR, 0);
        showModel = getArguments().getBoolean(SHOWMOUDLE, false);
        showModelSearchPage = getArguments().getBoolean(SHOWMOUDLESEARCHPAGE, false);
        animRes = getArguments().getInt(ANIMRES, R.anim.in_from_right);
        systemBarColor = getArguments().getInt(SYSTEM_BAR_COLOR, Color.BLUE);
        titleCenterTextColor = getArguments().getInt(TITLE_CENTER_TEXT_COLOR, Color.WHITE);
        titleLeftTextColor = getArguments().getInt(TITLE_LEFT_TEXT_COLOR, Color.WHITE);
        titleRightTextColor = getArguments().getInt(TITLE_RIGHT_TEXT_COLOR, Color.WHITE);
        iconBack = getArguments().getInt(ICON_BACK, R.drawable.back);
        iconTitleCenter = getArguments().getInt(ICON_TITLE_CENTER, R.drawable.ic_gf_triangle_arrow);
        iconTitleRight = getArguments().getInt(ICON_TITLE_RIGHT, R.drawable.ic_add_black_48dp);
        isLeftTextShow = getArguments().getBoolean(IS_LEFT_TEXT_SHOW, false);
        isLeftIconShow = getArguments().getBoolean(IS_LEFT_ICON_SHOW, false);
        isSystemBarShow = getArguments().getBoolean(IS_SYSTEM_BAR_SHOW, true);
        isRefreshEnable = getArguments().getBoolean(IS_REFRESH_ENABLE, false);

        if (showModelSearchPage) hideTitle();
        if (accountStr == null) accountStr = "null";
        if (titleColor != 0) { // 设置title颜色 和沉浸式菜单栏
            ll_head.setBackgroundColor(titleColor);
//            if (isSystemBarShow)
//                initSystemBar((Activity) mContext, systemBarColor);
        }
        if (isLeftTextShow) tv_head_left.setVisibility(View.GONE);
        iv_head_left.setImageResource(iconBack);
        if (isLeftIconShow) iv_head_left.setVisibility(View.GONE);
        // 页面不可刷新
        if (isRefreshEnable) mSwipeRefreshLayout.setEnabled(false);

        Log.i(TAG, "url:" + urlStr);
        this.setMainUrl(urlStr);
//        if (getValueFromDB()!= null){
//
//        }
//        setKeyToDB(account);
        mProgressDialog = new ProgressDialog(getContext(), ProgressDialog.THEME_HOLO_LIGHT);
        mProgressDialog.setCanceledOnTouchOutside(false);
//		mProgressDialog.setMessage("正在加载...");
//		mProgressDialog.show(); // 本地不show，拦截url响应事件
    }

    protected void initEvents() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mWebViewExten.setListener(this);
        mLl_back.setOnClickListener(this);
        ll_head_title.setOnClickListener(this);
        ll_right.setOnClickListener(this);
        ll_head_title.setClickable(false);
        ll_right.setClickable(false);
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

    private void enableHTML5AppCache() {
        /*******以下为蒋总设置webview缓存***********************/
        Log.i(TAG, "我调用了enableHTML5AppCache()");
        // 设置可以使用localStorage
        mWebViewExten.getSettings().setDomStorageEnabled(true);
        // 应用可以有缓存
        mWebViewExten.getSettings().setAppCacheEnabled(true);
        mWebViewExten.getSettings().setAllowFileAccess(true);
//        File dir = getContext().getCacheDir();
//        File dir =new File(getContext().getCacheDir().getAbsolutePath());
//        if(!dir.exists()){
//            dir.mkdirs();
//        }
//        Log.i(TAG,"路径："+dir.getPath());
//        mWebViewExten.getSettings().setAppCachePath(dir.getPath());
        mWebViewExten.getSettings().setAppCachePath(mWebViewExten.getContext().getCacheDir().getAbsolutePath());

        // 缓存模式
      //  mWebViewExten.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        mWebViewExten.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        /*******以上为蒋总设置webview缓存***********************/
    }

    /**
     * 清除viewview 的本地缓存
     */
    public void cleanCacheAndCookie(){
       /* JSONObject json = null;
        try {
            json = new JSONObject(paramter);
            String url = json.optString("cleanCacheAndCookie");
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        Log.e(TAG,"调用了缓存清理111");
        ToastUtil.showShortToast(getActivity(),"清理缓存成功");
//        Toast.makeText(getActivity(),"缓存清理成功",Toast.LENGTH_LONG);

      //  CookieManager.getInstance().removeAllCookie();
        File dir =new File(mWebViewExten.getContext().getCacheDir().getAbsolutePath());
        if (dir !=null && dir.isDirectory()){
            for (File child : dir.listFiles()){
                child.delete();
            }
            dir.delete();
        }
        Log.e(TAG,"调用了缓存清理222");
    }

    /**
     * 隐藏title
     */
    private void hideTitle() {
        ll_head.setVisibility(View.GONE);
    }

    /**
     * 设置应用的主页链接
     *
     * @param url
     */
    @Override
    public void setMainUrl(String url) {
        this.urlStr = url;
        this.mWebViewExten.loadUrl(this.urlStr);
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
     * 标题
     *
     * @param type
     * @param title 标题
     */
    @Override
    public void onTitle(int type, String title) {
        switch (type) {
            case CMD.type_h5Title:// 获取h5中的title
                if (!issetTitle)
                    this.mTv_head_title.setText(mWebViewExten.getTitle());
                break;
            case CMD.type_kitappsTitle:// 获取h5 命令中的title
                issetTitle = true;
                this.mTv_head_title.setText(title);
                break;
            default:// 默认设置 h5中自带的title
                if (!issetTitle)
                    this.mTv_head_title.setText(mWebViewExten.getTitle());
                break;
        }
    }

    /**
     * 命令回调
     *
     * @param command  命令
     * @param paramter 参数
     * @param function 回写函数
     */
    @Override
    public void onCommand(String command, String paramter, String function) {
        mParamter = paramter;
        Log.e(TAG, "onCommand()==命令command:" + command + "  参数paramter:" + paramter + "  功能function:" + function);
        switch (command) {
            case CMD.cmd_cellphone:// 打电话
                Utils.getPhone(mContext, paramter);
                break;
            case CMD.cmd_getImage:// 获取图片
//                getPicDialog(mContext, Utils.IMAGE_SELECT_CAMERA_AND_PHOTOS, function
//                        , REQUEST_CODE_CAMERA, REQUEST_CODE_GALLERY);
                break;
            case CMD.cmd_getCamera:// 打开相机
//                getPicDialog(mContext, Utils.IMAGE_SELECT_CAMERA, function, REQUEST_CODE_CAMERA, REQUEST_CODE_GALLERY);
                break;
            case CMD.cmd_getPicture:// 打开相册
//                getPicDialog(mContext, Utils.IMAGE_SELECT_PHOTOS, function, REQUEST_CODE_CAMERA, REQUEST_CODE_GALLERY);
                break;
            case CMD.cmd_setDate://  设置日期
                Utils.getTimePickerDialog(mWebViewExten, mContext, Utils.DATA_SELECT_DATA, function, paramter);
                break;
            case CMD.cmd_setTime:// 设置时间
                Utils.getTimePickerDialog(mWebViewExten, mContext, Utils.DATA_SELECT_TIME, function, paramter);
                break;
            case CMD.cmd_setCity:// 设置城市
                Utils.showAreaWindow(mWebViewExten, mLl_back, mContext, paramter, function);
                break;
            case CMD.cmd_setAddress:// 设置地址
//		gotoAmapLocationAcitvity(paramter,function);// 设置地址
                break;
            case CMD.cmd_showSelect:// 设置单选 （未实现）
                setSingleSelection();// 设置单选 （未实现）
                break;
            case CMD.cmd_setRightMenu:// 设置右上角菜单
                showModel = false;
                setTopAndRightMenu(paramter);// 设置右上角菜单
                break;

            case CMD.cmd_setLeftRightMenu:// 设置左上角菜单
                showModel = true;
                setTopAndRightMenu(paramter);// 设置左上角菜单
                break;

            case CMD.cmd_setCenterMenu:// 设置中间菜单
                setCenterMenu(paramter);// 设置中间菜单
                break;
            case CMD.cmd_setBottomMenu:// 设置底部菜单
                getBottomMenu(mContext, paramter);// 设置底部菜单
                break;

            case CMD.cmd_showMessage:// 弹出消息 toast
                ToastUtil.showLongToast(mContext, "我吐出来了paramter！");
                break;
            case CMD.cmd_pagetips:// 设置 弹窗提示
                setShowConfirmMessage(mContext, paramter);// 设置 弹窗提示
                break;

            case CMD.cmd_showHud:// 设置进度条 ProgressDialog
                setDialogForWait(paramter);
                break;

            case CMD.action_showModelPage: //  设置全屏隐藏导航栏
                setShowModelPage();//  设置全屏隐藏导航栏
                break;
            case CMD.action_showModelSearchPage:// 搜索界面
                gotoSearchPageAcitvity(paramter, function);// 搜索界面
                break;
            case CMD.action_closeModelPage: //  关闭全屏界面
                setCloseModelPage(); //  关闭全屏界面
                break;

            case CMD.cmd_scanQrcode: //扫描二维码
                startQrcode(paramter, function);// 扫描二维码
                break;
            case CMD.cmd_salary_dec:
                salaryDecode(paramter, function);// 薪资解密
                break;
            case CMD.cmd_forbidRefresh:// 禁止刷新
                noRefresh();// 不能刷新
                break;
            case CMD.cmd_openRefresh:// 开启刷新
                openRefresh();// 开启刷新
                break;
            case CMD.cmd_clearTask:// 任务栈清理
                clearTask(paramter, function);// 任务栈清理
                break;
            case CMD.cmd_uploadFile:// 上传图片
//                getPicDialog(mContext, Utils.IMAGE_SELECT_CAMERA_AND_PHOTOS, function, REQUEST_CODE_CAMERA, REQUEST_CODE_GALLERY);// 上传图片
                break;
            case CMD.cmd_GestureDrawing:
                sendMyBroadcast(paramter, function);// 发送特定广播 打开手势
                break;
            case CMD.cmd_initGesture:
                setLoginWay(paramter, function);// 初始化登录状态
                break;
            case CMD.cmd_imgBrowse:
                showImgBrowse(paramter, function);// 显示大图
                break;
            case CMD.cmd_checkClose:
                checkClose(paramter, function);// 检查 关闭
                break;
            case CMD.cmd_wechatShare:
                wechatShare(paramter, function);// 微信分享
                break;
//            case CMD.cmd_locaPage:
//                locaPage  (paramter, function);//  web View 加载新的Url
//                break;
            case CMD.cmd_setAlias:
                setAlias(paramter, function);// 极光推送 设置别名
                break;
            case CMD.cmd_setTags:
                setTags(paramter, function);// 极光推送 设置标签
                break;
            case CMD.cmd_setAliasAndTags:
                setAliasAndTags(paramter, function);// 极光推送 同时设置别名与标签
                break;
            case CMD.cmd_setBadgeNumber:
                setBadgeNumber(paramter, function);// 极光推送  设置badge值
                break;
            case CMD.cmd_getBadgeNumber:
                getBadgeNumber(paramter, function);// 极光推送 获取badge值
                break;
            case CMD.cmd_clearAllNotifications:
                clearAllNotifications(paramter, function);// 极光推送 清除所有通知
                break;
            case CMD.cmd_stopOrResumePush:
                stopOrResumePush(paramter, function);// 极光推送 清除所有通知
                break;
            case CMD.cmd_initLocation:
                setLoctioninfo(paramter, function);// 获取位置信息
                break;
            case CMD.cmd_recvMessage:
                recvMessage(paramter, function);//   注册消息广播
                break;
            case CMD.cmd_sendMessage:
                sendMessage(paramter, function); // 发送消息 （发送广播）
                break;
            case CMD.cmd_getNetworkStatus:
                getNetworkStatus(paramter, function); // 发送消息 （发送广播）
                break;
            case CMD.cmd_setTitle:
                setNewTitle(paramter, function); // 设置导航栏标题
                break;
            case CMD.cmd_showToast:
                showToast(paramter, function); // toast弹窗
                break;
            case CMD.cmd_setNavigationBgColor:
                setTitleBarBg(paramter, function); // 设置导航栏背景色
                break;
            case CMD.cmd_setDatabase:// 设置数据库
                writeDB(paramter, function); //
                break;
            case CMD.cmd_getDatabase:// 读取数据库
                readDB(paramter, function); //
                break;
            case CMD.cmd_uploadFile_Base64:// 将图片转换返回64位字符串返回给js
//                getPicDialog(mContext, Utils.IMAGE_SELECT_CAMERA_AND_PHOTOS, function, REQUEST_CODE_CAMERA_B, REQUEST_CODE_GALLERY_B);
                break;
            case CMD.cmd_cleanCacheAndCookie:
                cleanCacheAndCookie();

            case CMD.cmd_exitout :
                immediateQuit(mContext, paramter);

            default:// 其他
                ToastUtil.showLongToast(mContext, paramter);
                break;
        }
    }

    @Override
    public void onCommand(WebView view, String url) {

    }

    /**
     * 扫描二维码
     */
    protected void startQrcode(String paramter, String function) {
//        Intent intent = new Intent(mContext, QrcodeActivity.class);
//        intent.putExtra(PARAMTER, paramter);
//        intent.putExtra(FUNCTION, function);
//        startActivityForResult(intent, INTENT_REQUEST_QRCODE);
    }

    /**
     * 设置 底部菜单
     *
     * @param context
     * @param paramter 菜单参数
     */
    protected void getBottomMenu(final Context context, String paramter) {
        JSONObject jsonObject = null;
        List<BottomItem> list = new ArrayList<BottomItem>();
        try {
            jsonObject = new JSONObject(paramter);
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
                            String josn = String.format("javascript:" + item.getCallback() + "(%s)", "");
                            mWebViewExten.loadUrl(josn);
                        }
                    });
        }

        mActionSheetDialog.show();

    }

    /**
     * 读取数据库
     *
     * @param paramter
     * @param function
     */
    private void readDB(String paramter, String function) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(paramter);
            /** 存储key*/
            String getkey = jsonObject.optString("key");
            /*工号*/
            String acount = jsonObject.optString("account");
            String json = getValueFromDB(acount, getkey);
            String josn = String.format("javascript:" + function + "(" + "'%s')", json);
            mWebViewExten.loadUrl(josn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据key 从数据库得到value
     *
     * @param account
     * @param key
     * @return
     */
    protected String getValueFromDB(String account, String key) {
        WebViewDao mWebViewDao = new WebViewDao(mContext);
        return mWebViewDao.getWebviewValuejson(account, key);
    }

    /**
     * 存储数据库
     *
     * @param paramter
     * @param function
     */
    private void writeDB(String paramter, String function) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(paramter);

            /** 存储key*/
            String getkey = jsonObject.optString("key");
            /*工号*/
            String acount = jsonObject.optString("account");
            setKeyToDB(acount, getkey, paramter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置value 到数据库
     *
     * @param account
     * @param key
     * @param value
     */
    protected void setKeyToDB(String account, String key, String value) {
        WebViewDao mWebViewDao = new WebViewDao(mContext);

        mWebViewDao.saveWebviewJson(account, key, value);
    }

    /**
     * 设置导航栏 背景颜色
     *
     * @param paramter
     * @param function
     */
    private void setTitleBarBg(String paramter, String function) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(paramter);
//            JSONArray item = jsonObject.getJSONArray("item");
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
     * @param paramter
     * @param function
     */
    private void showToast(String paramter, String function) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(paramter);
//            JSONArray item = jsonObject.getJSONArray("item");
            /** js返回的标题*/
            String msg = jsonObject.optString("message");
            ToastUtil.showLongToast(mContext, msg);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置导航栏标题
     *
     * @param paramter js返回参数
     * @param function 需回掉js的方法
     */
    private void setNewTitle(String paramter, String function) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(paramter);
//            JSONArray item = jsonObject.getJSONArray("item");
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
     * @param paramter
     * @param function
     */
    public void getNetworkStatus(String paramter, String function) {
//网络可用
        if (NetworkUtils.isAvailable(mContext)) {
            //wifi
            if (NetworkUtils.isWifiConnected(mContext)) {
                String josn = String.format("javascript:" + function + "(" + "'%s')", "WiFi");
                mWebViewExten.loadUrl(josn);
                //手机网络
            } else {
                String josn = String.format("javascript:" + function + "(" + "'%s')", "3G");
                mWebViewExten.loadUrl(josn);
            }
            //网络不可用
        } else {
            String josn = String.format("javascript:" + function + "(" + "'%s')", "None");
            mWebViewExten.loadUrl(josn);

        }
    }


    /**
     * 发送消息
     *
     * @param function
     * @param paramter
     */
    private void sendMessage(String paramter, String function) {
        JSONObject jsonObject = null;
        String name = "";
        try {
            jsonObject = new JSONObject(paramter);
            name = jsonObject.optString("notificationName");
            String type = jsonObject.optString("type");
            if (TextUtils.isEmpty(name)) {
                name = "receiveMessage";
            } else {
                name = name + type;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            name = "receiveMessage";
        }
        Intent tent = new Intent(name);// 广播的标签，一定要和需要接受的一致。
        tent.putExtra("data", paramter);
        tent.putExtra("function", function);
        mContext.sendBroadcast(tent);// 发送广播
    }

    /**
     * 删除消息
     *
     * @param paramter
     * @param function
     */
    protected void recvMessage(String paramter, String function) {
        JSONObject jsonObject = null;
        String name = "";
        try {
            jsonObject = new JSONObject(paramter);
            name = jsonObject.optString("notificationName");
            String type = jsonObject.optString("type");
            if (TextUtils.isEmpty(name)) {
                name = "receiveMessage";
            } else {
                name = name + type;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            name = "receiveMessage";
        }
        registerBoradcastReceiver(name, function);
    }

    /**
     * 注册广播
     *
     * @param actionName BoradcastReceiver 的action
     * @param function
     */
    public void registerBoradcastReceiver(String actionName, String function) {
        if (receiveBroadCast == null) {
            receiveBroadCast = new ReceiveBroadCast(actionName, function);
            IntentFilter filter = new IntentFilter();
            filter.addAction(actionName); // 只有持有相同的action的接受者才能接收此广播 receiveMessage
            mContext.registerReceiver(receiveBroadCast, filter);
        }
    }

    /**
     * 设置位置信息
     *
     * @param paramter
     * @param function
     */
    protected void setLoctioninfo(String paramter, String function) {
        // 初始化页面，调js函数initlocation('省份','城市')
        String josn3 = String.format("javascript:" + function + "(" + "'%s','%s')", AppConfing.LOCATION_Province, AppConfing.LOCATION_City);
        mWebViewExten.loadUrl(josn3);
    }

    /**
     * 停止或者启动推送
     *
     * @param paramter
     * @param function
     */
    protected void stopOrResumePush(String paramter, String function) {

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
     * 清除所有通知
     *
     * @param paramter
     * @param function
     */
    public void clearAllNotifications(String paramter, String function) {

    }

    ;

    /**
     * 获取badge值
     *
     * @param paramter
     * @param function
     */
    public void getBadgeNumber(String paramter, String function) {

    }

    /**
     * 设置badge值
     *
     * @param paramter
     * @param function
     */
    public void setBadgeNumber(String paramter, String function) {

    }

    ;

    /**
     * 同时设置别名与标签
     *
     * @param paramter
     * @param function
     */
    public void setAliasAndTags(String paramter, String function) {

    }

    /**
     * 设置标签
     *
     * @param paramter
     * @param function
     */
    public void setTags(String paramter, String function) {

    }

    ;

    /**
     * 1. setAlias 设置别名
     *
     * @param paramter
     * @param function
     */
    public void setAlias(String paramter, String function) {

    }

    ;

    /**
     * 微信分享
     *
     * @param paramter
     * @param function
     */
    protected void wechatShare(String paramter, String function) {
        initTarg();
        creatFilePath("DCIM");
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(paramter);
            JSONArray item = jsonObject.getJSONArray("item");
            String url = item.getJSONObject(0).optString("url");// 图片url
            String name = item.getJSONObject(0).optString("title");// 标题
            String icon = item.getJSONObject(0).optString("icon");// 图片路径
            String summary = item.getJSONObject(0).optString("summary");// 简介
            showShare(url, name, icon, summary);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 微信分享
     *
     * @param url
     * @param name
     * @param icon
     * @param summary
     */
    private void showShare(String url, String name, String icon, String summary) {
//        ShareSDK.initSDK(mContext);
//        oks = new OnekeyShare();
//        Log.e(TAG,"url:"+url+"  "+name+"   "+icon+"  "+summary);
//        if (!TextUtils.isEmpty(icon)) {
//            Picasso.with(mContext).load(icon).into(target);
//        }
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        oks.setTitle(name);
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText(summary);
//        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl(url);
//        if (TextUtils.isEmpty(icon)) {
//            File file = new File(stringBuilder.toString());
//            if (!file.exists()) {
//                file.mkdirs();
//            }
//            // 工程根目录下的assets文件夹中存放
//            AssetManager am = this.getResources().getAssets();
//            try {
//                InputStream is = am.open("img/icon.png");
//                FileOutputStream fos = new FileOutputStream(new File(stringBuilder.toString() + "icon.png"));
//                byte[] buffer = new byte[1024];
//                int byteCount=0;
//                while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节
//                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
//                }
//                fos.flush();//刷新缓冲区
//                is.close();
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            oks.setImagePath(stringBuilder.toString() + "icon.png");//确保SDcard下面存在此张图片
//            // 启动分享GUI
//            oks.show(mContext);
//        } else {
//            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//            oks.setImagePath(stringBuilder.toString() + "logo.png");//确保SDcard下面存在此张图片
//            // 启动分享GUI
//            oks.show(mContext);
//        }

    }

    /**
     * 是否需要 启用 关闭页面前 的check
     *
     * @param paramter
     * @param function
     */
    protected void checkClose(String paramter, String function) {
//        ToastUtil.showLongToast(this, paramter);
        ((Activity) mContext).finish();
    }

    /**
     * 显示大图
     *
     * @param paramter
     * @param function
     */
    protected void showImgBrowse(String paramter, String function) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(paramter);
            JSONArray item = jsonObject.getJSONArray("item");
            String url = item.getJSONObject(0).optString("url");// 图片url
            String name = item.getJSONObject(0).optString("name");// 右上角菜单名称
            startActivity(new Intent(mContext, ImageBrowserActivty.class).putExtra(IMAGE_URL, url).putExtra(IMAGE_RIGHTMEN, name));
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
     * @param paramter
     * @param function
     */
    private void setLoginWay(String paramter, String function) {
        functionNameStr = function;
        LockPatternUtils mLockPatternUtils = new LockPatternUtils(mContext);
        Log.e("KKK", TAG + " savedPatternExists = " + mLockPatternUtils.savedPatternExists());
        if (mLockPatternUtils.savedPatternExists()) { // 手势登录
            String josn = String.format("javascript:" + functionNameStr + "('%s')", "2");
            mWebViewExten.loadUrl(josn);
            return;
        }

        if (!SharedPreferUtil.getInstance(mContext).getPrefBoolean("passWordBool", false)) {// 自动（免密）登录 默认值true
            String josn = String.format("javascript:" + functionNameStr + "('%s')", "1");
            mWebViewExten.loadUrl(josn);
        } else {// 输入密码登录
            String josn = String.format("javascript:" + functionNameStr + "('%s')", "3");
            mWebViewExten.loadUrl(josn);
        }

    }

    /**
     * 发送特定广播 打开手势
     *
     * @param paramter
     * @param function
     */
    protected void sendMyBroadcast(String paramter, String function) {
        functionNameStr = function;
        LockPatternUtils mLockPatternUtils = new LockPatternUtils(mContext);
        JSONObject mJSONObject = null;
        String typeStr = "";

        try {
            mJSONObject = new JSONObject(paramter);
            typeStr = mJSONObject.optString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if ("1".equals(typeStr)) { // 免密码登录
            SharedPreferUtil.getInstance(mContext).setPrefBoolean("passWordBool", false);
            mLockPatternUtils.clearLock();
        } else if ("2".equals(typeStr)) { // 创建手势登录
//            Intent intent = new Intent(mContext, CreatGesturePasswordActivity.class);
//            intent.putExtra("showBack", "1");
//            intent.putExtra("password_MD5", "");
//            startActivityForResult(intent, INTENT_REQUEST_GESTURE);
        } else if ("3".equals(typeStr)) { // 密码登录
            SharedPreferUtil.getInstance(mContext).setPrefBoolean("passWordBool", true);
            mLockPatternUtils.clearLock();
        }
    }

    /**
     * 清理栈 （关闭 指定的页面）
     *
     * @param paramter
     * @param function
     */
    protected void clearTask(String paramter, String function) {
        Log.i(TAG, "clearTask  paramter:" + paramter + "     function:" + function);
        if (isClearTask) { // 不能二次清除
            return;
        }
        isClearTask = true;
        JSONObject object = null;
        try {
            object = new JSONObject(paramter);
            final int index = object.optInt("clearTask");
            ActivityCollector.clearTask(index);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 页面不可刷新
     */
    protected void noRefresh() {
        isRefreshEnable = true;
        mSwipeRefreshLayout.setEnabled(false);
    }

    /**
     * 密文解密 设置薪资数据
     *
     * @param paramter
     * @param function
     */
    protected void salaryDecode(String paramter, String function) {
//        Log.i(TAG, "salaryDecode  paramter:" + paramter + "     function:" + function);
//        JSONObject object = null;
//        try {
//            object = new JSONObject(paramter);
//            final String passwd = object.optString("passwd");
//            final String text = object.optString("text");
//
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("dec_text", AESDecode.decode(passwd, text));
//            String josn = String.format("javascript:" + function + "(%s)", jsonObject);
//            mWebViewExten.loadUrl(josn);
//            Log.i(TAG, "AESDecode:" + josn);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


    }

    /**
     * 设置顶部菜单
     */
    public void setTopRightMenu() {
        mTopMenuPopWin = new TopMenuPopupWindowActivity(getContext());
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

    protected void immediateQuit(final Context context, String msg){
        try {
            JSONObject object = new JSONObject(msg);
            final String confirm = object.optString(getString(R.string.yes_action));
            if(!TextUtils.isEmpty(confirm)){
                if (confirm.equals(CMD.cmd_exitout)){
                 //   Intent mintent = new Intent(context);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 信息提示框
     * @param context
     * @param msg
     */
    protected void setShowConfirmMessage(Context context, String msg) {
        AlertDialog mAlertDialog = new AlertDialog(context).builder();

        try {
            JSONObject object = new JSONObject(msg);
            final String title = object.getString("title");
            final String content = object.getString("content");
            final String confirm = object.getString(getString(R.string.yes_action));
            final String cancel = object.getString(getString(R.string.no_action));


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

            if (!TextUtils.isEmpty(confirm)) {

                mAlertDialog.setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (confirm.startsWith("http")) { // URL请求
                            Map<String, String> param = UrlUtil.parseUrl(confirm);
                            if (param.containsKey("action")) {
                                String value = param.get("action");
                                if (value != null) {
                                    if (value.equals(CMD.action_nextPage)) {
                                        onNextPage(confirm, value);
                                    } else if (value.equals(CMD.action_closePage)) {
                                        onClosePage(confirm, value);
                                    } else if (value.equals(CMD.action_closePageAndRefresh)) {
                                        onClosePageReturnMain(confirm, value);
                                    }
                                }
                            }
                        } else if (confirm.startsWith("kitapps")) { // 页面的功能函数
                            Map<String, String> param = UrlUtil.parseUrl(confirm);
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
                        } else if (confirm.equals(CMD.action_close)) {
                            // 对话框消失
                        } else { // 回调js方法
                            if (confirm.endsWith(")")) {
                                String josn = String.format("javascript:" + confirm);
                                Log.e(TAG, "==josn:" + josn);
                                mWebViewExten.loadUrl(josn);
                            } else {
                                String josn = String.format("javascript:" + confirm + "(%s)", "");
                                mWebViewExten.loadUrl(josn);
                            }
                        }
                    }
                });
            }

            if (!TextUtils.isEmpty(cancel)) {
                mAlertDialog.setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        ToastUtil.showLongToast(mContext, "取消！");
                    }
                });
            }

            mAlertDialog.show();

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 单选
     */
    protected void setSingleSelection() {

        List<String> list = new ArrayList<String>();
        list.add("学士");
        list.add("硕士");
        list.add("博士");

        mSingleSeletPopupWindow = new SingleSeletPopupWindow(mContext);
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
     * 跳转搜索页
     */
    private void gotoSearchPageAcitvity(String paramter, String function) {
        hideTitle();
//        searchFunction = function;
//        Bundle bundle = new Bundle();
//        bundle.putInt("flag", 1);
//        Intent intent=new Intent(mContext, SearchActivity.class);
//        intent.putExtras(bundle);
//        startActivityForResult(intent,REQUEST_CODE_SEARCH);
    }

    /**
     * 关闭全屏界面
     */
    public void setCloseModelPage() {
        ll_head.setVisibility(View.VISIBLE);
    }

    /**
     * 强制设置全屏界面
     */
    public void setShowModelPage() {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                  WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        ll_head.setVisibility(View.GONE);
    }

    /**
     * 转圈等待
     */
    private void setDialogForWait(String msg) {

        try {
            JSONObject object = new JSONObject(msg);
            final String content = object.getString("content");
            final String action = object.getString("action");

            if ("hide".equals(action)) {
                if (mProgressDialog.isShowing()) colseProgressDialog();
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 关闭转圈等待
     */
    public void colseProgressDialog() {
        // 创建定时器
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mProgressDialog.dismiss();
                timer.cancel();
            }
        }, 1000, 300);
    }

    /**
     * 设置title菜单
     */
    private void setCenterMenu(String json) {
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
            mTv_head_title.setText(mWebViewExten.getTitle());
            iv_head_title_menu.setVisibility(View.GONE);
        }

    }

    /**
     * 设置右上角菜单
     */
    protected void setTopAndRightMenu(String json) {
        farstMune = true;
        listMenuItem.clear();
        TopMenu menu = Utils.json2entity(json, TopMenu.class);
        Log.e(TAG, "数据：" + menu.getItem().get(0).toString());
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
                    Picasso.with(getContext()).load(menu.getItem().get(0).getIcon()).into(img_right);
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
     * 设置title菜单
     */
    public void setTitleMenu() {
        mTitleMenuPopWin = new TitleMenuPopupWindow(getContext());
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
     * @param url
     * @param action
     */
    @Override
    public boolean onNextPage(String url, String action) {
        if (action != null) {
            Log.i(TAG, "onNextPage()===action:" + action + "   url:" + url);
            if (urlStr.equals(url)) return false;
            if (action.equals(CMD.action_nextPage) || action.equals(CMD.action_showModelPage) || action.equals(CMD.action_showModelSearchPage)) {
//                if (url.equals("http://test.mobisoft.com.cn/qas/v10/app_Salary/html/Salarycheck.html?action=nextPage")) return false;
//                handler.sendEmptyMessage(1); // 薪资查询必须跳空白页加载数据才能进入，刷新后死循环(刷新是为了解决某些页面本地跳转)
                openNextWebActivity(url, action);
                return true;
            } else if (action.equals(CMD.action_homepage)) {
                ActivityCollector.finishAll(); // 销毁所有的webactivity
//                ActivityCollector.goHomePage();// 返回第一次打开的webactivity
                return true;
            } else if (action.equals(CMD.action_closePageAndRefreshAndPop)) {
                ToastUtil.showShortToast(getContext(), CMD.action_closePageAndRefreshAndPop);
                return true;
            } else if (action.equals(CMD.action_closePageAndPop)) {
                ToastUtil.showShortToast(getContext(), CMD.action_closePageAndPop);
                return true;
            }
        }
        return false;
    }

    @Override
    public WebResourceResponse onSIRNextPage(String url, String action) {
        if (urlStr.equals(url)) return null;
        if (action.equals(CMD.action_nextPage) || action.equals(CMD.action_showModelPage) || action.equals(CMD.action_showModelSearchPage)) {
            openNextWebActivity(url, action);
        } else if (action.equals(CMD.action_homepage) || action.equals(CMD.action_exit)) {
            ActivityCollector.finishAll(); // 销毁所有的webactivity
//                ActivityCollector.goHomePage();// 返回第一次打开的webactivity
        } else if (action.equals(CMD.action_closePageAndRefreshAndPop)) {
            ToastUtil.showShortToast(getContext(), CMD.action_closePageAndRefreshAndPop);
        } else if (action.equals(CMD.action_closePageAndPop)) {
            ToastUtil.showShortToast(getContext(), CMD.action_closePageAndPop);
        }

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
    }

    /**
     * 打开下一个WebActivity
     *
     * @param url
     * @param action
     */
    protected void openNextWebActivity(String url, String action) {
        Bundle bunde = new Bundle();
        bunde.putString(URL, url);
        if (accountStr != null) bunde.putString(ACCOUNT, accountStr);
        if (titleColor != 0) bunde.putInt(TITLECOLOR, titleColor);
        if (isSystemBarShow) {
            bunde.putBoolean(IS_SYSTEM_BAR_SHOW, isSystemBarShow);
            bunde.putInt(SYSTEM_BAR_COLOR, systemBarColor);
        }
        if (action.equals(CMD.action_showModelPage)) bunde.putBoolean(SHOWMOUDLE, true);
        if (action.equals(CMD.action_showModelSearchPage))
            bunde.putBoolean(SHOWMOUDLESEARCHPAGE, true);
        bunde.putBoolean(IS_TRANSITION_MODE_ENABLE, true);
        bunde.putString(IS_TRANSITION_MODE, "RIGHT");
        Intent intent = new Intent();
        intent.setClass(getContext(), WebAppActivity.class);
        intent.putExtras(bunde);
        startActivityForResult(intent, INTENT_REQUEST_CODE);
    }

    @Override
    public boolean onClosePage(String url, String action) {
        if (action != null) {
            if (action.equals(CMD.action_closePage)) {
                this.onHiddenChanged(true);
                return true;
//                /**
//                 * 创建定时器
//                 */
//                final Timer timer = new Timer();
//                timer.schedule(new TimerTask() { // 延时关闭非栈顶的webactivity
//                    @Override
//                    public void run() {
//                        ActivityCollector.removeActivity(mContext);
//                        timer.cancel();
//                        finish();
//                    }
//                }, 500, 300);
            }
        } else {
        }
        return false;
    }

    /**
     * 返回关闭当前页面并回到原界面
     * @param url
     * @param action
     * @return
     */
    @Override
    public boolean onClosePageReturnMain(String url, String action) {
        Intent mIntent = new Intent();
        mIntent.putExtra(REFRESH, true);
        ((Activity) getContext()).setResult(INTENT_REQUEST_CODE, mIntent);
        return true;
    }

    /**
     * 关闭界面
     */
    @Override
    public void onWebPageFinished() {
        mWebViewExten.setEnabled(true);
        // 解决 android 5.0 以下多次调用onPageFinished的方法，多次调用initpage() 导致页面重复加载的问题
        if (firstComeIn) firstComeIn = false;
        else return;

        // 初始化工号
        String josn = String.format("javascript:initaccount(" + "'%s')", accountStr);
        mWebViewExten.loadUrl(josn);
        // 初始化页面，调js函数
        String josn2 = String.format("javascript:initPage(" + "'%s')", "");
        mWebViewExten.loadUrl(josn2);
//		mWebViewExten.reload();
        Log.e("出师页面：账号", "" + josn);
        handler.sendEmptyMessage(2);
        if (mProgressDialog.isShowing()) colseProgressDialog();
    }

    @Override
    public boolean onLightweightPage(String url, String action) {
        return false;
    }



    /**
     * 上拉刷新的线程
     */
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
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
                    mWebViewExten.loadUrl(urlStr);
//                    mWebViewExten.loadUrl("http://www.liaohuqiu.net/");
//                    mWebViewExten.excuteJSFunction(CMD.cmd_setHeaderRefreshing, "");
//                    mWebViewExten.excuteJSFunction(CMD.cmd_startIntercept, "");
//                    handler.sendEmptyMessage(2);
                    break;
                case 2:
                    mSwipeRefreshLayout.setRefreshing(false);// 停止刷新
                    mSwipeRefreshLayout.setLoading(false);// 停止加载
                    break;
                default:
                    break;
            }
        }

        ;
    };

    /**
     * 下拉刷新上拉加载
     */
    public void refresh() {
        handler.sendEmptyMessage(1);
    }

    /**
     * 上拉加载
     */
    private void load() {
        handler.sendEmptyMessage(0);
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    /**
     * startActivityForResult返回值
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (requestCode == WebAppActivity.INTENT_REQUEST_CODE) {
            if (data.getBooleanExtra(WebAppActivity.REFRESH, false)) refresh();
        }

        // 相机返回结果
        if (requestCode == Utils.IMAGE_FROM_CAMERA) {
            if (resultCode == ((Activity) mContext).RESULT_OK) {
                File f = new File(Environment.getExternalStorageDirectory() + "/" + "ideaTemp" + "/" + Utils.TEMP_IMAGE_CAMERA);
                try {
                    Uri u = Uri.parse(MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
                            f.getAbsolutePath(), null, null));
                    String name = Utils.copyPhotoToTemp(mContext, u);
                    if (!TextUtils.isEmpty(name)) {
//                        String[] arrayImages ={name};
//                        String str = Utility.getArrayJosn(mfunc, arrayImages);
//                        webViewExten.loadUrl(str);

                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == Utils.IMAGE_FROM_PHOTOS) {
            if (resultCode == ((Activity) mContext).RESULT_OK) {
                Log.e("oye", "==" + data.getData());
                String name = Utils.copyPhotoToTemp(mContext, data.getData());
                if (!TextUtils.isEmpty(name)) {
//                    String[] arrayImages ={name};
//                    String str = Utility.getArrayJosn(mfunc, arrayImages);
//                    Log.e("path", str + ":"+arrayImages.length);
//                    webViewExten.loadUrl(str);
                }
            }
        }

    }

    /**
     * 头部菜单点击事件
     *
     * @param position
     */
    private void TopMenuClick(List<MeunItem> list, int position) {
        Log.e(TAG, "点击   +  :" + position);
        if (!TextUtils.isEmpty(list.get(position).getCallback())) {// 回调函数
            String josn = String.format("javascript:" + list.get(position).getCallback() + "(%s)", "");
            mWebViewExten.loadUrl(josn);
        } else if (!TextUtils.isEmpty(list.get(position).getUrl())) {// 启动新页面
            onNextPage(list.get(position).getUrl(), CMD.action_nextPage);
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
            if (parent != null) parent.removeView(mWebViewExten);
            mWebViewExten.removeAllViews();
            mWebViewExten.destroy();
            mWebViewExten = null;
        }

        Log.e(TAG, "销毁页面！onDestroy");
        // 解决退出activity时 dialog未dismiss而报错的bug
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        } catch (Exception e) {
            System.out.println("myDialog取消，失败！");
        }

        super.onDestroy();
    }

    /**
     * 开启状态栏
     *
     * @param activity
     * @param on
     */
    @TargetApi(19)
    private void setTranslucentStatus(Activity activity, boolean on) {

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
            Log.e(TAG,function);
            if (TextUtils.isEmpty(function)){
               // Toast.makeText(context, "收到广播,哈哈哈", Toast.LENGTH_SHORT).show();
            }

            if (TextUtils.equals(mActionName, actionName)) {
                // 得到广播中得到的数据，并显示出来
                Bundle extras = data.getExtras();
                if (extras != null) {
                    String data1 = extras.getString("data");
                    String josn = String.format("javascript:" + function + "(" + "'%s')", data1);
                    mWebViewExten.loadUrl(josn);
                }
            }
        }
    }

    /**
     * 创建下载路径
     *
     * @param path
     */
    private void creatFilePath(String path) {
        stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory().getPath());
        stringBuilder.append("/");
        stringBuilder.append(path);
        stringBuilder.append("/QAS");
        File file = new File(stringBuilder.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        stringBuilder.append("/img/");
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    // 复制html文件到私有文件夹下面 。2016/11/11号修改
                    FileUtils.copyAssetDirToFiles(mContext, "img");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 设置下载路径
     */
    private void initTarg() {
        if (target == null)
            //Target
            target = new Target() {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    String imageName = "logo.png";

                    File dcimFile = getDCIMFile(DCIM, imageName);

                    FileOutputStream ostream = null;
                    try {
                        ostream = new FileOutputStream(dcimFile);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                        ostream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    Toast.makeText(mContext, "图片下载至:" + dcimFile, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
    }


    /**
     * 获取相册路径
     *
     * @param path
     * @param imageName
     * @return
     */
    private File getDCIMFile(String path, String imageName) {

        File image = new File(stringBuilder.toString() + imageName);

        return image;
    }

    // 退出登入
   /* public void Quit(final Context context) {

        DemoHXSDKHelper.getInstance().logout(new EMCallBack() { // 退出环信
            @Override
            public void onSuccess() {

                SharedPreferUtil.getInstance(context).clearPreference(); // 清除缓存
                getContext().getLockPatternUtils().clearLock(); // 清楚手势锁
                // 登出当前
                ((Activity) MainActivity.instance).finish();
                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

            }

            @Override
            public void onError(int i, String s) {
                ToastUtil.showShortToast(context,"退出登录失败，请稍后再试！");
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });

    }*/


}
