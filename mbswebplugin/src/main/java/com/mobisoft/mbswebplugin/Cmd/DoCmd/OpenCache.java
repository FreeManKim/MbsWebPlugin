package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.proxy.Setting.ProxyConfig;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author：Created by fan.xd on 2017/6/14.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class OpenCache extends DoCmdMethod {
    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
        try {
            JSONObject jsonObject = new JSONObject(params);
            boolean isOpen = jsonObject.optBoolean("openProxy",false);
            ProxyConfig.getConfig().setOpenProxy(isOpen);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
