package com.mobisoft.mbswebplugin.MvpMbsWeb;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import com.alibaba.fastjson.JSON;
import com.mobisoft.mbswebplugin.Cmd.CMD;
import com.mobisoft.mbswebplugin.Cmd.ProxyCmd;
import com.mobisoft.mbswebplugin.Entity.BottomItem;
import com.mobisoft.mbswebplugin.IProxyCallback;
import com.mobisoft.mbswebplugin.IProxyPortListener;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebApp;
import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.base.ActivityManager;
import com.mobisoft.mbswebplugin.base.Recycler;
import com.mobisoft.mbswebplugin.helper.CoreConfig;
import com.mobisoft.mbswebplugin.helper.FunctionConfig;
import com.mobisoft.mbswebplugin.helper.ThemeConfig;
import com.mobisoft.mbswebplugin.proxy.Cache.CacheManifest;
import com.mobisoft.mbswebplugin.proxy.Setting.ProxyConfig;
import com.mobisoft.mbswebplugin.proxy.server.ProxyService;
import com.mobisoft.mbswebplugin.proxy.server.SettingProxy;
import com.mobisoft.mbswebplugin.utils.ActivityCollector;
import com.mobisoft.mbswebplugin.utils.ToastUtil;
import com.mobisoft.mbswebplugin.utils.Utils;
import com.mobisoft.mbswebplugin.view.ActionSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

import static com.mobisoft.mbswebplugin.MvpMbsWeb.Base.Preconditions.checkNotNull;
import static com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebFragment.IS_TRANSITION_MODE;
import static com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebFragment.IS_TRANSITION_MODE_ENABLE;
import static com.mobisoft.mbswebplugin.base.AppConfing.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE;
import static com.mobisoft.mbswebplugin.base.AppConfing.CONTACTS_CODE;
import static com.mobisoft.mbswebplugin.base.AppConfing.INTENT_REQUEST_CODE;
import static com.mobisoft.mbswebplugin.base.AppConfing.PICK_IMAGE_ACTIVITY_REQUEST_CODE;
import static com.mobisoft.mbswebplugin.base.AppConfing.REFRESH;
import static com.mobisoft.mbswebplugin.base.AppConfing.REFRESH_URL;
import static com.mobisoft.mbswebplugin.base.AppConfing.REQUEST_CODE_MAP;
import static com.mobisoft.mbswebplugin.utils.Utils.IMAGE_FROM_CAMERA;

/**
 * Author：Created by fan.xd on 2017/3/16.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class WebPluginPresenter implements MbsWebPluginContract.Presenter, Recycler.Recycleable {

    private MbsWebPluginContract.View mBsWebView;
    private Activity mActivity;
    private Class cls;
    private Bundle bundle;
    public static final String TAG = "WebPluginPresenter";
    /**
     * onActivityResult 监听事件
     */
    private MbsResultListener resultListener;
    /***
     * activity是否支持打开方式
     */
    protected boolean isTransitionModeEnable = true; // activity是否支持打开方式
    /***
     * activity打开方式
     */
    protected String isTransitionMode = "RIGHT"; // activity打开方式
    private ReceiveBroadCast receiveBroadCast;
    private MbsRequestPermissionsListener permissionsResultListener;

    public WebPluginPresenter(@NonNull MbsWebPluginContract.View mbsWebView,
                              Activity activity, Class cls, Bundle bundle) {
        mBsWebView = checkNotNull(mbsWebView, "mbsWebView cannot be null!");
        mBsWebView.setPresenter(this);
        this.mActivity = checkNotNull(activity);
        this.cls = checkNotNull(cls);
        this.bundle = checkNotNull(bundle);
        isTransitionModeEnable = bundle.getBoolean(IS_TRANSITION_MODE_ENABLE, true);
        isTransitionMode = bundle.getString(IS_TRANSITION_MODE);
    }


    @Override
    public void onResume() {

    }

    @Override
    public boolean nextPage(String url, String action) {
        if (action != null) {
            // 相同的url就不打开，避免重复打开多个页面
            if (mBsWebView.getUrl().equals(url)) return false;
            switch (action) {
                case CMD.action_nextPage:
                case CMD.action_showModelPage:
                case CMD.action_showModelSearchPage:
                    mBsWebView.openNextWebActivity(url, action);
                    return true;
                case CMD.action_homepage:
                    onHomePage(url, action);
                    return true;

                case CMD.action_exit:
                    ActivityCollector.finishAll(); // 销毁所有的webactivity
                    return true;
                case CMD.action_closePageAndRefreshAndPop:
                    ToastUtil.showShortToast(ActivityManager.get().topActivity(), CMD.action_closePageAndRefreshAndPop);
                    return true;
                case CMD.action_closePageAndPop:
                    ToastUtil.showShortToast(ActivityManager.get().topActivity(), CMD.action_closePageAndPop);
                    return true;
            }

        }
        return false;
    }

    @Override
    public void onCreate() {
        if (isTransitionModeEnable) {
            if (FunctionConfig.TransitionMode.LEFT.name().equals(isTransitionMode)) {
                mActivity.overridePendingTransition(R.anim.left_in, R.anim.out_to_right);
            } else if (FunctionConfig.TransitionMode.RIGHT.name().equals(isTransitionMode)) {
                mActivity.overridePendingTransition(R.anim.right_in, R.anim.out_to_left);
            } else if (FunctionConfig.TransitionMode.TOP.name().equals(isTransitionMode)) {
                mActivity.overridePendingTransition(R.anim.top_in, R.anim.top_out);
            } else if (FunctionConfig.TransitionMode.BOTTOM.name().equals(isTransitionMode)) {
                mActivity.overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
            } else if (FunctionConfig.TransitionMode.SCALE.name().equals(isTransitionMode)) {
                mActivity.overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
            } else if (FunctionConfig.TransitionMode.FADE.name().equals(isTransitionMode)) {
                mActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }
    }

    @Override
    public void finish() {
        if (isTransitionModeEnable) {
            if (FunctionConfig.TransitionMode.LEFT.name().equals(isTransitionMode)) {
                mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
            } else if (FunctionConfig.TransitionMode.RIGHT.name().equals(isTransitionMode)) {
                mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
            } else if (FunctionConfig.TransitionMode.TOP.name().equals(isTransitionMode)) {
                mActivity.overridePendingTransition(R.anim.top_in, R.anim.top_out);
            } else if (FunctionConfig.TransitionMode.BOTTOM.name().equals(isTransitionMode)) {
                mActivity.overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
            } else if (FunctionConfig.TransitionMode.SCALE.name().equals(isTransitionMode)) {
                mActivity.overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
            } else if (FunctionConfig.TransitionMode.FADE.name().equals(isTransitionMode)) {
                mActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }
    }

    @Override
    public void finishActivity() {
        ActivityCollector.removeActivity(mActivity);
        mActivity.finish();
    }

    @Override
    public void onDestroy() {
        //解除 广播 receiveBroadCast
        if (receiveBroadCast != null) {
            mActivity.unregisterReceiver(receiveBroadCast);
        }
        SettingProxy.revertBackProxy(mBsWebView.getWebView(), mActivity.getApplication().getClass().getName());
        Recycler.release(this);
    }

    @Override
    public void release() {
        Log.i(TAG, "release");
        mActivity = null;
        mBsWebView = null;
        cls = null;
        bundle = null;
        receiveBroadCast = null;
        resultListener = null;
    }

    /**
     * 设置监听事件
     *
     * @param resultListener
     */
    @Override
    public void setResultListener(MbsResultListener resultListener) {
        this.resultListener = resultListener;
    }

    @Override
    public void registerBroadcastReceiver(String actionName, String callback) {
        if (receiveBroadCast == null) {
            receiveBroadCast = new ReceiveBroadCast(actionName, callback);
            IntentFilter filter = new IntentFilter();
            filter.addAction(actionName); // 只有持有相同的action的接受者才能接收此广播 receiveMessage
            mActivity.registerReceiver(receiveBroadCast, filter, CacheManifest.PERMISSION, null);
        }
    }

    /**
     * 设置 底部菜单
     *
     * @param context   环境
     * @param parameter 菜单参数
     */
    @Override
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
                            mBsWebView.loadUrl(json);
                        }
                    });
        }

        mActionSheetDialog.show();

    }

    @Override
    public boolean onClosePage(String url, String action) {
        if (action != null) {
            if (action.equals(CMD.action_closePage)) {
                finishActivity();
                return true;
            }
        } else {
            finishActivity();
        }
        return false;
    }

    @Override
    public boolean onClosePageReturnMain(String url, String action) {
        Intent mIntent = new Intent();
        mIntent.putExtra(REFRESH, true);
//        mIntent.putExtra(REFRESH_URL, url.replace("?action=closePageAndRefresh", "").trim());
        mActivity.setResult(INTENT_REQUEST_CODE, mIntent);
        // loadUrl(url);
        finishActivity();
        return false;
    }

    @Override
    public boolean onLightweightPage(String url, String action) {
        CoreConfig coreConfig =
                new CoreConfig.Builder(
                        mActivity, ThemeConfig.DEFAULT, FunctionConfig.DEFAULT_ACTIVITY)
                        .setURL("http://elearning.mobisoft.com.cn/mobile/registe.html")
                        .setAccount("8100458")//
                        .setNoAnimcation(false)
                        .setHideNavigation(true)
                        .build();
        HybridWebApp.init(coreConfig).startWebActivity(mActivity, cls);
        return false;
    }


    @Override
    public void onHomePage(String url, String action) {
        ProxyCmd.getInstance().getHomePage().goHomePage(mActivity, null, url, action);
    }

    @Override
    public void setTitle(int type, String title) {
        mBsWebView.setTitle(type, title);
    }

    @Override
    public void setNavigationIcon(int resId) {
        mBsWebView.setNavigationIcon(resId);
    }

    @Override
    public void setMbsRequestPermissionsResultListener(MbsRequestPermissionsListener listener) {
        this.permissionsResultListener = listener;
    }

    @Override
    public void setProxy() {
        Intent intent = new Intent(mActivity, ProxyService.class);
        mActivity.startService(intent);
        SettingProxy.setProxy(mBsWebView.getWebView(), "127.0.0.1", ProxyConfig.getConfig().getPORT(), mActivity.getApplication().getClass().getName());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (permissionsResultListener != null) {
            permissionsResultListener.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case INTENT_REQUEST_CODE:
                if (data != null && data.getBooleanExtra(REFRESH, false)) {
                    String urlStr = TextUtils.isEmpty(data.getStringExtra(REFRESH_URL)) ? mBsWebView.getUrl() : data.getStringExtra(REFRESH_URL);
                    Log.e(TAG, urlStr);
                    mBsWebView.reload();
                }
                break;
            case REQUEST_CODE_MAP: // 地图
                int flag = data.getIntExtra("flag", 0);
                if (flag == 3) {// 定位失败
                    break;
                } else {// 定位成功
//                    latitude = data.getDoubleExtra("latitude", 0);
//                    longitude = data.getDoubleExtra("longitude", 0);
//                    userLocationAddress = data.getStringExtra("address");
//
//                    mWebViewExten.excuteJSFunction(areaFunction, Utils.IN_PARAMETER_FOR_ADDR, userLocationAddress, latitude + "", longitude + "");
                }
                break;
            case IMAGE_FROM_CAMERA:
                if (resultCode == RESULT_OK) {
                    File f = new File(Environment.getExternalStorageDirectory() + "/" + "ideaTemp" + "/" + Utils.TEMP_IMAGE_CAMERA);
                    try {
                        Uri u = Uri.parse(MediaStore.Images.Media.insertImage(mActivity.getContentResolver(),
                                f.getAbsolutePath(), null, null));
                        Utils.copyPhotoToTemp(mActivity, u);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case Utils.IMAGE_FROM_PHOTOS:
                if (resultCode == RESULT_OK) {
                    Utils.copyPhotoToTemp(mActivity, data.getData());
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
                    mBsWebView.loadUrl(json);
                }
                break;
            case PICK_IMAGE_ACTIVITY_REQUEST_CODE:  // 启动系统相册获取照片
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:// 拍照 拍照
            default:
                if (resultCode == RESULT_OK && resultListener != null) {
                    resultListener.onActivityResult(mActivity, mBsWebView, requestCode, resultCode, data);
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return mBsWebView.onKeyDown(keyCode, event);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        intent.setClass(mActivity, cls);
        mActivity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void startActivity(Intent intent) {
        intent.setClass(mActivity, cls);
        mActivity.startActivity(intent);
    }

    @Override
    public void startIntent(Intent intent) {
        mActivity.startActivity(intent);
    }

    @Override
    public void start() {
        Intent intent = new Intent(mActivity, ProxyService.class);
        mActivity.bindService(intent, mProxyConnection, Context.BIND_AUTO_CREATE | Context.BIND_NOT_FOREGROUND);

    }


    @Override
    public void onPause() {
        if (mBound) {
            mActivity.unbindService(mProxyConnection);
        }
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
                    mBsWebView.loadUrl(String.format("javascript:" + mfunction + "('%s')", data1));

                }
            }
        }
    }
}
