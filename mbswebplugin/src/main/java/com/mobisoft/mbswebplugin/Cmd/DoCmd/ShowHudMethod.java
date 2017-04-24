package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.text.TextUtils;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author：Created by fan.xd on 2017/2/27.
 * Email：fang.xd@mobisoft.com.cn
 * Description： 转圈等待进度条
 */

public class ShowHudMethod extends DoCmdMethod {
    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
        JSONObject object = null;
        try {
            object = new JSONObject(params);
            final String content = object.optString("content");
            final String action = object.optString("action");
            if (TextUtils.equals("hide", action)) {
                view.hideHud();
            }

            if (TextUtils.isEmpty(content)) {
                view.showHud(action, "正在加载...");

            } else {
                if (content.contains("完成")) {
                    view.showHud(action, content);
                }
//                    ((WebAppActivity) context).handler.sendEmptyMessage(1);
//                ((WebAppActivity) context).mProgressDialog.setMessage(content);
            }

            if ("show".equals(action)) {
                view.showHud(action, "正在加载...");
            }
//            ((WebAppActivity) context).mProgressDialog.show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
