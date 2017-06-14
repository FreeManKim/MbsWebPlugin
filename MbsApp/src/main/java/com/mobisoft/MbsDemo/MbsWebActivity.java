//package com.mobisoft.MbsDemo;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.speech.SpeechRecognizer;
//import android.support.annotation.NonNull;
//import android.support.annotation.RequiresApi;
//import android.support.annotation.StyleRes;
//import android.support.v4.content.ContextCompat;
//import android.text.TextUtils;
//import android.view.KeyEvent;
//import android.view.View;
//import android.webkit.WebResourceResponse;
//
//import com.mobisoft.mbswebplugin.Cmd.CMD;
//import com.mobisoft.mbswebplugin.Entity.MeunItem;
//import com.mobisoft.mbswebplugin.MbsWeb.HybridWebApp;
//import com.mobisoft.mbswebplugin.helper.CoreConfig;
//import com.mobisoft.mbswebplugin.helper.FunctionConfig;
//import com.mobisoft.mbswebplugin.helper.ThemeConfig;
//import com.mobisoft.mbswebplugin.utils.ToastUtil;
//
//import java.util.HashSet;
//import java.util.List;
//
//import static com.mobisoft.mbswebplugin.base.AppConfing.PERMISSIONS_REQUEST_CODE;
//
///**
// * MbswebView 测试  加载h5页面
// */
//public class MbsWebActivity extends WebAppActivity {
//
//        /**
//         * 语音听写对象
//         */
//    private SpeechRecognizer mIat;
//
//
//    /**
//     * 极光推送标签
//     */
//    private HashSet<String> setStr;
//
//    /**
//     * 语音识别 回掉方法
//     */
//    private String voiceFunction;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    /**
//     * 初始化控件
//     */
//    @Override
//    protected void initViews() {
//        super.initViews();
//    }
//
//    /**
//     * 初始化 数据
//     */
//    @Override
//    protected void initData() {
//        super.initData();
////        mSwipeRefreshLayout.setEnabled(false);
//    }
//
//    /**
//     * 初始化事件
//     */
//    @Override
//    protected void initEvents() {
//        super.initEvents();
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
////    /**
////     * 打开相册 ，相机
////     *
////     * @param context
////     * @param param    0相册 1相机 2相机相册
////     * @param function 回调方法
////     */
////    @Override
////    protected void getPicDialog(Context context, String param, String function, int reqCodeCAMERA, int reqCodeGALLERY,int limtConunt,int selectCount) {
//////        super.getPicDialog(context, param, function, reqCodeCAMERA, reqCodeGALLERY,limtConunt,selectCount);
////    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        return super.onKeyDown(keyCode, event);
//    }
//    /**
//     * 设置主页面
//     *
//     * @param url
//     */
//    @Override
//    public void setMainUrl(String url) {
//        super.setMainUrl(url);
//    }
//
//    /**
//     * 获取主页面连接
//     *
//     * @return 主页的URL
//     */
//    @Override
//    public String getMainUrl() {
//        return super.getMainUrl();
//    }
//
//    /**
//     * 重新加载页面
//     */
//    @Override
//    public void reloadApp() {
//        super.reloadApp();
//    }
//
//    /**
//     * 设置页面title
//     *
//     * @param title 标题
//     */
//    @Override
//    public void onTitle(int type, String title) {
//        super.onTitle(type, title);
//    }
//
//    /**
//     * 页面加载结束
//     */
//    @Override
//    public void onWebPageFinished() {
//        super.onWebPageFinished();
//    }
//
//    /**
//     * 拦截命令
//     *
//     * @param command  命令
//     * @param parameter 参数
//     * @param function 回写函数
//     */
//    @Override
//    public void onCommand(String command, String parameter, String function) {
//        super.onCommand(command, parameter, function);
//
//    }
//
//
//    /**
//     * 语音识别
//     * @param parameter 参数
//     * @param function 回写函数
//     */
//    @Override
//    protected void speechRecognition(String parameter, final String function) {
//        super.speechRecognition(parameter, function);
//        this.voiceFunction = function;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)// 大于6.0 权限检查
//            requestVoicePermission(function, Manifest.permission.RECORD_AUDIO);
//        else
//            startSpeechRecognition(function);
//    }
//
//    /**
//     * 语音识别
//     * @param function 回写函数
//     */
//    private void startSpeechRecognition(final String function) {
//
//    }
//
//    /**
//     * 讯飞语音识别以及解析
//     *
//     * @param text  识别返回 文本
//     * @param function 回掉参数
//     */
//    private void printResult(final String text, final String function) {
////        Toast.makeText(this,text+"测试：",Toast.LENGTH_LONG).show();
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                String josn = getFormatJavascript(function, text);
//                loadUrl(josn);
//            }
//        });
//    }
//
//
//
//    /**
//     * toast
//     * @param parameter 参数
//     */
//    private void showTip(String parameter) {
//        ToastUtil.showLongToast(this, parameter);
//    }
//
//
//    /**
//     * 播放视频
//     * @param parameter   参数
//     * @param function 回掉参数
//     */
//    @Override
//    protected void playVideo(String parameter, String function) {
//        super.playVideo(parameter, function);
//    }
//
//    /**
//     * 打开通讯录
//     *
//     * @param paramter 参数
//     * @param function 回写函数
//     */
//    @Override
//    protected void openContacts(String paramter, String function) {
//        super.openContacts(paramter, function);
//    }
//
//    /**
//     * 启动二维码扫描
//     * @param parameter   参数
//     * @param function 回掉参数
//     */
//    @Override
//    protected void startQrcode(String parameter, String function) {
//        super.startQrcode(parameter, function);
//    }
//
//    /**
//     * 打开搜索页面
//     * @param parameter   参数
//     * @param function 回掉参数
//     */
//    @Override
//    protected void gotoSearchPageAcitvity(String parameter, String function) {
//        super.gotoSearchPageAcitvity(parameter, function);
//    }
//
//    /**
//     * 隐藏标题
//     */
//    @Override
//    protected void hideTitle() {
//        super.hideTitle();
//    }
//
//    /**
//     * 地图页面，（没有实现，没有调用）
//     * @param parameter   参数
//     * @param function 回掉参数
//     */
//    @Override
//    protected void gotoAmapLocationAcitvity(String parameter, String function) {
//        super.gotoAmapLocationAcitvity(parameter, function);
//    }
//
//    /**
//     * 设置title菜单
//     *
//     * @param json title
//     */
//    @Override
//    protected void setCenterMenu(String json) {
//        super.setCenterMenu(json);
//    }
//
//    /**
//     * 设置右上角菜单
//     *
//     * @param json json菜单参数
//     */
//    @Override
//    public void setTopAndRightMenu(String json) {
//        super.setTopAndRightMenu(json);
//    }
//
//    /**
//     * 设置转圈等待
//     *
//     * @param msg 提示文本
//     */
//    @Override
//    protected void setDialogForWait(String msg) {
//        super.setDialogForWait(msg);
//    }
//
//    /**
//     * 关闭进度条
//     */
//    @Override
//    public void colseProgressDialog() {
//        super.colseProgressDialog();
//    }
//
//    /**
//     * 设置设置 底部菜单
//     *
//     * @param context 环境
//     * @param parameter 菜单参数
//     */
//    @Override
//    public void setBottomMenu(Context context, String parameter) {
//        super.setBottomMenu(context, parameter);
//    }
//
//    /**
//     * 确认提示框
//     *
//     * @param context 环境
//     * @param msg  提示消息
//     */
//    @Override
//    protected void setShowConfirmMessage(Context context, String msg) {
//        super.setShowConfirmMessage(context, msg);
//    }
//
//    /**
//     * 定位
//     *
//     * @param paramter 参数
//     * @param function 回掉方法
//     */
//    @Override
//    protected void setLoctioninfo(String paramter, String function) {
////        LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
////        if(!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
////        {
////            // 未打开位置开关，可能导致定位失败或定位不准，提示用户或做相应处理
////            AlertDialog.Builder builder = new AlertDialog.Builder(this);
////            builder.setTitle("提示");
////            builder.setMessage("未打开位置开关，可能导致定位失败或定位不准！");
////            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
////                @Override
////                public void onClick(DialogInterface dialog, int which) {
////                    dialog.dismiss();
////                }
////            });
////            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
////                @Override
////                public void onClick(DialogInterface dialog, int which) {
////                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
////                    startActivity(intent);
////                }
////            });
////            builder.create().show();
////        }
//        super.setLoctioninfo(paramter, function);
//
//    }
//
//    @Override
//    protected void onClose(String confirm, String value) {
//        super.onClose(confirm, value);
//    }
//
//    @Override
//    public void setShowModelPage() {
//        super.setShowModelPage();
//    }
//
//    @Override
//    public void setCloseModelPage() {
//        super.setCloseModelPage();
//    }
//
//    @Override
//    protected void setSingleSelection() {
//        super.setSingleSelection();
//    }
//
//    @Override
//    public void setTopRightMenuList() {
//        super.setTopRightMenuList();
//    }
//
//    @Override
//    public void setTitleMenu() {
//        super.setTitleMenu();
//    }
//
//    @Override
//    public boolean onNextPage(String url, String action) {
//        return super.onNextPage(url, action);
//    }
//
//    @Override
//    public WebResourceResponse onSIRNextPage(String url, String action) {
//        return super.onSIRNextPage(url, action);
//    }
//
//    /**
//     * 下一个页面
//     *
//     * @param url
//     * @param action
//     */
//    @Override
//    protected void openNextWebActivity(String url, String action) {
//        url = url.replace("&action=nextPage", "");
//        Bundle bunde = new Bundle();
//        bunde.putString(URL, url);
//        if (accountStr != null) bunde.putString(ACCOUNT, accountStr);
//        if (titleColor != 0) bunde.putInt(TITLECOLOR, titleColor);
//        if (isSystemBarShow) {
//            bunde.putBoolean(IS_SYSTEM_BAR_SHOW, isSystemBarShow);
//            bunde.putInt(SYSTEM_BAR_COLOR, systemBarColor);
//        }
//        if (action.equals(CMD.action_showModelPage)) bunde.putBoolean(SHOWMOUDLE, true);
//        if (action.equals(CMD.action_showModelSearchPage))
//            bunde.putBoolean(SHOWMOUDLESEARCHPAGE, true);
//        if (isTransitionModeEnable) {
//            bunde.putBoolean(IS_TRANSITION_MODE_ENABLE, isTransitionModeEnable);
//            bunde.putString(IS_TRANSITION_MODE, isTransitionMode);
//        }
//        Intent intent = new Intent();
//        intent.setClass(MbsWebActivity.this, WebAppActivity.class);
//        intent.putExtras(bunde);
//        startActivityForResult(intent, INTENT_REQUEST_CODE);
//    }
//
//    @Override
//    public boolean onClosePage(String url, String action) {
//        return super.onClosePage(url, action);
//    }
//
//    @Override
//    public boolean onClosePageReturnMain(String url, String action) {
//        return super.onClosePageReturnMain(url, action);
//    }
//
//    @Override
//    public void refresh() {
//        super.refresh();
//    }
//
//    @Override
//    public void onRefresh() {
//        super.onRefresh();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//
////    @Override
////    public boolean onTouch(View v, MotionEvent event) {
////        return super.onTouch(v, event);
////    }
//
//    @Override
//    public void onClick(View v) {
//        super.onClick(v);
//    }
//
//    @Override
//    protected void TopMenuClick(List<MeunItem> list, int position) {
//        super.TopMenuClick(list, position);
//    }
//
//    @Override
//    protected void setTranslucentStatus(Activity activity, boolean on) {
//        super.setTranslucentStatus(activity, on);
//    }
//
////    @Override
////    public void initSystemBar(Activity activity, int setStatusBarTintResource) {
//////        super.initSystemBar(activity, setStatusBarTintResource);
////    }
//
//    @Override
//    public void finish() {
//        super.finish();
//    }
//
//    /**
//     * 申请运行时权限
//     *
//     * @param requestCode   requestCode
//     * @param permissions permissions
//     * @param grantResults  grantResults
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case PERMISSIONS_REQUEST_CODE:// 录音权限
//                startSpeechRecognition(voiceFunction);
//                break;
//        }
//    }
//
//    @Override
//    protected void setKeyToDB(String account, String key, String value) {
//        super.setKeyToDB(account, key, value);
//    }
//
//    @Override
//    protected String getValueFromDB(String account, String key) {
//        return super.getValueFromDB(account, key);
//    }
//    /***
//     * 检查并app版本是否需要更新
//     * @param parameter 参数
//     * @param function 回掉方法
//     */
//    @Override
//    protected void checkForUpdate(String parameter, String function) {
//        super.checkForUpdate(parameter, function);
//    }
//
//    @Override
//    public void setTheme(@StyleRes int resid) {
//        super.setTheme(resid);
//    }
//
//
//
//    @Override
//    protected void wechatShare(String paramter, String function) {
//        super.wechatShare(paramter, function);
//    }
//
//    /**
//     * 薪资解密
//     *
//     * @param paramter 参数
//     * @param function  回掉方法
//     */
//    @Override
//    protected void salaryDecode(String paramter, String function) {
//        super.salaryDecode(paramter, function);
//    }
//
//    /**
//     * 是否需要 启用 关闭页面前 的check
//     * @param paramter 参数
//     * @param function  回掉方法
//     */
//    @Override
//    protected void checkClose(String paramter, String function) {
//        super.checkClose(paramter, function);
//    }
//
//    /**
//     * 同时设置别名与标签（极光）
//     *
//     * @param paramter 参数
//     * @param function 回掉方法
//     */
//    @Override
//    protected void setAliasAndTags(String paramter, String function) {
//        super.setAliasAndTags(paramter,function);
//    }
//
//    /**
//     * 设置标签 (极光)
//     * @param paramter 参数
//     * @param function  回掉方法
//     */
//    @Override
//    protected void setTags(String paramter, String function) {
//        super.setTags(paramter,function);
//    }
//
//    /**
//     * 1. setAlias 设置别名 (极光)
//     * @param paramter 参数
//     * @param function  回掉方法
//     */
//    @Override
//    protected void setAlias(String paramter, String function) {
//        super.setAlias(paramter,function);
//    }
//
//    /**
//     * 停止或者启动推送(极光)
//     * @param paramter 参数
//     * @param function  回掉方法
//     */
//    @Override
//    protected void stopOrResumePush(String paramter, String function) {
//        super.stopOrResumePush(paramter,function);
//
//    }
//
//    /**
//     * 获取badge值 (极光)
//     * @param paramter 参数
//     * @param function  回掉方法
//     */
//    @Override
//    protected void getBadgeNumber(String paramter, String function) {
//        super.getBadgeNumber(paramter,function);
//
//    }
//
//    /**
//     * 设置badge值 (极光)
//     * @param paramter 参数
//     * @param function  回掉方法
//     */
//    @Override
//    protected void setBadgeNumber(String paramter, String function) {
//        super.setBadgeNumber(paramter,function);
//    }
//
//    /**
//     * 清除所有通知(极光)
//     * @param paramter 参数
//     * @param function  回掉方法
//     */
//    @Override
//    protected void clearAllNotifications(String paramter, String function) {
//        super.clearAllNotifications(paramter,function);
//    }
//
//    /**
//     * 显示大图
//     * @param parameter 参数
//     * @param function  回掉方法
//     */
//    @Override
//    protected void showImgBrowse(String parameter, String function) {
//        super.showImgBrowse(parameter, function);
//    }
//
//    /**
//     * =打开手势（并没发送广播）
//     * @param parameter 参数
//     * @param function  回掉方法
//     */
//    @Override
//    protected void sendMyBroadcast(String parameter, String function) {
//        super.sendMyBroadcast(parameter, function);
//    }
//
//    /**
//     * 注册广播
//     * @param parameter 参数
//     * @param function  回掉方法
//     */
//    @Override
//    protected void receiveMessage(String parameter, String function) {
//        super.receiveMessage(parameter, function);
//    }
//
//    /**
//     * 注册广播
//     * @param actionName 参数
//     * @param function  回掉方法
//     */
//    @Override
//    public void registerBoradcastReceiver(String actionName, String function) {
//        super.registerBoradcastReceiver(actionName, function);
//    }
//
//    /**
//     * 清除指定任务栈（activity list 的倒序 删除）
//     *
//     * @param paramter 参数
//     * @param function 方法名
//     */
//    @Override
//    protected void clearTask(String paramter, String function) {
//        super.clearTask(paramter, function);
//    }
//
//    /**
//     * 下拉刷新刷新
//     */
//    @Override
//    public void forbidRefresh() {
//        super.forbidRefresh();
//    }
//
//    /**
//     * 返回填json 收藏
//     *
//     * @param func  js回掉方法
//     * @param param 回掉方法填充 的 参数
//     * @return 格式话的javascript方法
//     */
//    public String getFuncJosn(String func, String param) {
//
//        if (TextUtils.isEmpty(func)) {
//            return null;
//        }
//
//        String josn = String.format("javascript:" + func + "(%s)", param);
//
//        return josn;
//
//    }
//
//
//
//    /**
//     * 加载轻量级 webView
//     *
//     * @param url    加载的html的url
//     * @param action action命令可以为空
//     * @return true；
//     */
//    @Override
//    public boolean onLightweightPage(String url, String action) {
//       CoreConfig coreConfig = new CoreConfig.Builder(
//               MbsWebActivity.this, ThemeConfig.DEFAULT, FunctionConfig.DEFAULT_ACTIVITY)
//                .setURL(url)
//                .setAccount(accountStr)//
//                .setNoAnimcation(false)
//                .build();
//        HybridWebApp.init(coreConfig).startWebActivity(this,WebAppActivity.class);
//        return true;
//    }
//
//    /**
//     * 申请录音权限
//     *
//     * @param function   回掉参数，
//     * @param permission
//     */
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void requestVoicePermission(String function, String... permission) {
//        if (ContextCompat.checkSelfPermission(this, permission[0])
//                != PackageManager.PERMISSION_GRANTED) {
//            startSpeechRecognition(function);
//        } else {
//            // Ask for one permission
//          requestPermissions(permission,
//                    PERMISSIONS_REQUEST_CODE);
//        }
//    }
//
//
//}
//
//
//
