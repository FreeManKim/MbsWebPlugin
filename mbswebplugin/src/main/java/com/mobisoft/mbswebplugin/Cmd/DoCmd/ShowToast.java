package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author：Created by fan.xd on 2017/2/24.
 * Email：fang.xd@mobisoft.com.cn
 * Description：toast弹窗操作
 */

public class ShowToast extends DoCmdMethod {

    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
        try {
            JSONObject jsonObject = new JSONObject(params);
            /** js返回的标题*/
            String msg = jsonObject.optString("message");
            ToastUtil.showLongToast(context, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
