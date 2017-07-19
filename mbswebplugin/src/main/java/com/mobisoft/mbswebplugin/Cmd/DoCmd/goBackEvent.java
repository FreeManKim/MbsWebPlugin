package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author：Created by fan.xd on 2017/7/12.
 * Email：fang.xd@mobisoft.com.cn
 * Description：点击按钮返回事件的拦截    ；
 */

public class goBackEvent extends DoCmdMethod {
    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
        try {
            JSONObject jsonObject  = new JSONObject(params);
            view.setBackEvent(jsonObject.optString("event"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
