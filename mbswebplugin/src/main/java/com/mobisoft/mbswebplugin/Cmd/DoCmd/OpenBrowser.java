package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author：Created by fan.xd on 2017/4/27.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class OpenBrowser extends DoCmdMethod {
    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
        try {
            JSONObject jsonObject = new JSONObject(params);
            /** 网页地址*/
            String url = jsonObject.optString("url");
//            /*网页名称*/
//            String name=jsonObject.optString("name");
            // 启动浏览器
            Uri uri = Uri.parse(url);
            Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
            presenter.startIntent(intent2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
