package com.mobisoft.mbswebplugin.MbsWeb;

/**
 * Created by jiangzhou on 16/4/13.
 * WebApp事件监听
 */
public interface WebAppInterface {
    /**
     * 设置应用的主页链接
     * @param url
     */
    void setMainUrl(String url);

    /**
     * 获取应用的主页链接地址
     */
    String getMainUrl();

    /**
     * 重新加载页面
     */
    void reloadApp();
}
