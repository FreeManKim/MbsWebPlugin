package com.mobisoft.mbswebplugin.MbsWeb;

import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;

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
    void onTitle(int type, String tile);

    /**
     * 命令回调
     * @param command 命令
     * @param paramter  参数
     * @param function  回写函数
     */
    void onCommand(String command, String paramter, String function);
    /**
     * 命令回调
     * @param  view
     * @param url 命令
     */
    void onCommand(WebView view, String url);

    /**
     * 下一页
     * @param url
     * @param action
     */
    boolean onNextPage(String url, String action);
    WebResourceResponse onSIRNextPage(String ur, String action);

    /**
     * 关闭当前页
     * @param url
     * @param action
     */
    boolean onClosePage(String url, String action);

    /**
     * 关闭当前页回到首页
     * @param url
     * @param action
     */
    boolean onClosePageReturnMain(String url, String action);

    /**
     * 网页加载完成
     */
    void onWebPageFinished();

    /**
     * 加载轻量级webView
     * @param url
     * @param action
     */
    boolean onLightweightPage(String url, String action);

    /**
     *跳转新地址
     * @param isNew 是否调用initPage();
     */
    void onHrefLocation(boolean isNew);
}
