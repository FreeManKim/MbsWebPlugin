package com.mobisoft.mbswebplugin.MbsWeb;

import android.webkit.WebResourceResponse;
import android.webkit.WebView;

/**
 * Created by jiangzhou on 16/4/13.
 *  web View的监听事件获取title onCommand命令等
 *
 */
public interface HybridWebviewListener {

    /**
     * 标题
     * @param tile 标题
     * @param type 设置了类型  0：获取网页  1：获取命令中的标题
     */
    public void onTitle(int type, String tile);

    /**
     * 命令回调
     * @param command 命令
     * @param paramter  参数
     * @param function  回写函数
     */
    public void onCommand(String command, String paramter, String function);
    /**
     * 命令回调
     * @param  view
     * @param url 命令
     */
    public void onCommand(WebView view,String url);

    /**
     * 下一页
     * @param url
     * @param action
     */
    public boolean onNextPage(String url, String action);
    public WebResourceResponse onSIRNextPage(String ur, String action);

    /**
     * 关闭当前页
     * @param url
     * @param action
     */
    public boolean onClosePage(String url, String action);

    /**
     * 关闭当前页回到首页
     * @param url
     * @param action
     */
    public boolean onClosePageReturnMain(String url, String action);

    /**
     * 网页加载完成
     */
    public void onWebPageFinished();

    /**
     * 加载轻量级webView
     * @param url
     * @param action
     */
    public boolean onLightweightPage(String url, String action);
}
