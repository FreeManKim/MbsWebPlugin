package com.mobisoft.mbswebplugin.MvpMbsWeb;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import com.mobisoft.mbswebplugin.Entity.MeunItem;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Base.BasePresenter;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Base.BaseView;

import java.util.List;

/**
 * Author：Created by fan.xd on 2017/3/2.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public interface MbsWebPluginContract {

    interface View extends BaseView<Presenter> {
        /**
         * 禁止刷新
         */
        void forbiddenRefresh();

        /**
         * 获取webview
         */
        HybridWebView getWebView();

        /**
         * 加载地址
         *
         * @param url 加载地址
         */
        void loadUrl(String url);

        /**
         * 重新加载页面
         */
        void reload();

        /**
         * 获取主页地址
         *
         * @return getUrl
         */
        String getUrl();

        /**
         * 下一页
         *
         * @param url    url
         * @param action action
         */
        void openNextWebActivity(String url, String action);

        /**
         * 返回键
         *
         * @param keyCode 返回键
         * @param event   返回键
         * @return
         */
        boolean onKeyDown(int keyCode, KeyEvent event);

        /**
         * 隐藏标题栏
         */
        void hideTitle();

        /**
         * 头部菜单点击事件
         *
         * @param list     菜单列表
         * @param position 点击的item的下标
         */
        void TopMenuClick(List<MeunItem> list, int position);

        /**
         * 设置顶部菜单
         */
        void setTopRightMenu();

        /**
         * 设置title菜单
         */
        void setTitleMenu();

        /**
         * 设置标题栏背景色
         */
        void setTitleBg(String color);

        /**
         * 获取 是否清除过任务栈
         *
         * @return getIsClearTask true：已经清理过
         */
        boolean getIsClearTask();

        /**
         * 设置是否清除过任务站
         *
         * @param b true ：清理过一次
         * @return
         */
        void setIsClearTask(boolean b);

        /**
         * 设置右上角菜单
         *
         * @param json js返回的Menu数据
         */
        void setTopAndRightMenu(String json);

        /**
         *  显示进度条
         * @param action
         * @param message
         */
        void showHud(String action,String message);

        /**
         * 隐藏进度条
         */
        void hideHud();
    }

    interface Presenter extends BasePresenter {
        /**
         * onResume
         */
        void onResume();

        /**
         * 下一页
         *
         * @param url    地址
         * @param action action命令
         * @return
         */
        boolean nextPage(String url, String action);

        /**
         * 进入页面
         */
        void onCreate();

        /**
         * activity 结束调用
         */
        void finish();

        /**
         * 调用此方法结束activity
         */
        void finishActivity();

        /**
         * 页面结束
         */
        void onDestroy();

        /**
         * 申请权限 回掉
         *
         * @param requestCode  requestCode
         * @param permissions  permissions
         * @param grantResults grantResults
         */
        void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

        /**
         * onActivityResult 见名思意不多讲
         *
         * @param requestCode requestCode
         * @param resultCode  resultCode
         * @param data
         */
        void onActivityResult(int requestCode, int resultCode, Intent data);

        /**
         * 返回键
         *
         * @param keyCode keyCode
         * @param event   event
         * @return
         */
        boolean onKeyDown(int keyCode, KeyEvent event);

        /**
         * 启动页面
         *
         * @param intent      intent
         * @param requestCode requestCode
         */
        void startActivityForResult(Intent intent, int requestCode);

        /**
         * 启动页面
         *
         * @param intent
         */
        void startActivity(Intent intent);

        void onPause();

        /**
         * 甚至startActivityListener
         *
         * @param resultListener
         */
        void setResultListener(MbsResultListener resultListener);

        /**
         * 注册广播
         *
         * @param name     广播的actionName
         * @param callback webView的回掉方法
         */
        void registerBroadcastReceiver(String name, String callback);

        /**
         * 设置代理
         */
        void setProxy();

        /**
         *
         */
        void setBottomMenu(Context context, String parameter);


        /**
         * 关闭页面
         *
         * @param url    地址
         * @param action action命令
         * @return
         */
        boolean onClosePage(String url, String action);

        /**
         * 关闭页面并且返回主页
         *
         * @param url    url
         * @param action action
         * @return
         */
        boolean onClosePageReturnMain(String url, String action);

        /**
         * 隐藏头部导航栏
         *
         * @param url    地址
         * @param action action命令
         */
        boolean onLightweightPage(String url, String action);

        /**
         * 隐藏头部导航栏
         *
         * @param url    地址
         * @param action action命令
         */
        void onHomePage(String url, String action);
    }

}
