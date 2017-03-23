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
    public void setMainUrl(String url);

    /**
     * 获取应用的主页链接地址
     */
    public String getMainUrl();

    /**
     * 重新加载页面
     */
    public  void reloadApp();
}
