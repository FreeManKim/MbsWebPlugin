package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.text.TextUtils;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.R;

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
                return null;
            }

            if (TextUtils.isEmpty(content)) {
                view.showHud(action, context.getString(R.string.loading));

            } else {
                if (content.contains(context.getString(R.string.wancheng))) {
                    view.showHud(action, content);
                }
//                    ((WebAppActivity) context).handler.sendEmptyMessage(1);
//                ((WebAppActivity) context).mProgressDialog.setMessage(content);
            }

            if ("show".equals(action)) {
                view.showHud(action, context.getString(R.string.loading));
            }
//            ((WebAppActivity) context).mProgressDialog.show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
