package com.mobisoft.mbswebplugin.Cmd;

import android.content.Context;

import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;

/**
 * Author：Created by fan.xd on 2017/2/24.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public abstract class DoCmdMethod {

    /**
     *
     * @param webView 核心组件 webView
     * @param context 环境
     * @param view  视图类
     * @param presenter 持有者类
     * @param cmd cmd命令
     * @param params 参数
     * @param callBack js回掉的方法
     * @return 任意
     */
    abstract public String doMethod(HybridWebView webView, Context context
            , MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter
            , String cmd, String params, String callBack);


}
