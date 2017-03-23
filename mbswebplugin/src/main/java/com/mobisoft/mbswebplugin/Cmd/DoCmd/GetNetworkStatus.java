package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.utils.NetworkUtils;
import com.mobisoft.mbswebplugin.utils.UrlUtil;

/**
 * Author：Created by fan.xd on 2017/3/1.
 * Email：fang.xd@mobisoft.com.cn
 * Description：判断当前网络连接状态
 */

public class GetNetworkStatus extends DoCmdMethod {
    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {

        //网络可用
        //wifi
        //网络不可用
        if (NetworkUtils.isAvailable(context)) {
            if (NetworkUtils.isWifiConnected(context)) {
                String json = UrlUtil.getFormatJavascript(callBack, "WiFi");
                webView.loadUrl(json);
                //手机网络
            } else {
                //String.format("javascript:" + function + "(" + "'%s')", "3G");
                String json = UrlUtil.getFormatJavascript(callBack, "3G");
                webView.loadUrl(json);
            }
        } else {
            String json = UrlUtil.getFormatJavascript(callBack, "None");
            webView.loadUrl(json);

        }
        return null;
    }

}
