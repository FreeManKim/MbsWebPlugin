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
 * Description： 接收消息
 * 入参：
 * {"notificationName":"xxxx"，"type":"refreshBrief"}notificationName（界面名）
 * 和 type（具体操作名） 两个字段拼接确保唯一当做消息所接受消息的名字
 */

public class RecvMessage extends DoCmdMethod {
    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
        String name;
        try {
            JSONObject jsonObject = new JSONObject(params);

            name = jsonObject.optString("notificationName");
            String type = jsonObject.optString("type");
            if (!TextUtils.isEmpty(name)) {
                name = name + type;
            } else {
                name = "receiveMessage";
            }

        } catch (JSONException e) {
            e.printStackTrace();
            name = "receiveMessage";
        }
//        ((WebAppActivity) context).registerBoradcastReceiver(name, callBack);
        presenter.registerBroadcastReceiver(name,callBack);

        return null;
    }
}
