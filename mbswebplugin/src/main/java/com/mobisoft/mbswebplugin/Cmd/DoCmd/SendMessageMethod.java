package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author：Created by fan.xd on 2017/2/27.
 * Email：fang.xd@mobisoft.com.cn
 * Description： 发送广播
 * 入参：{"notificationName":"xxxx","type":"refreshBrief","message":"course","src":"news_detail","extra":1}notificationName: 界面名 和type拼接一起当做所发消息的名字然后接受消息的时候根据两个字段进行拼接去接受对应的通知，防止接受所有通知
 * type: 操作名  具体是什么类型什么字段，需要js端自己定义，并根据不同类型执行不同刷新操作。前端接收到消息会把所有参数callback返回给所有收到消息的界面
 * message：发送的消息类型
 * src: 发送消息的几面的html名字
 * extra：消息数量
 */

public class SendMessageMethod extends DoCmdMethod {
    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {

        String name;
        try {
            JSONObject jsonObject = new JSONObject(params);
            name = jsonObject.optString("notificationName");
            String type = jsonObject.optString("type");
            if (TextUtils.isEmpty(name)) { // 广播的默认action
                name = "receiveMessage";
            } else {
                // 根据js返回 拼接 广播的action
                name = name + type;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            // 广播的默认action
            name = "receiveMessage";
        }
        Intent tent = new Intent(name);// 广播的标签，一定要和需要接受的一致。
        tent.putExtra("data", params);
        tent.putExtra("function", callBack);
        context.sendBroadcast(tent);// 发送广播
        return null;
    }
}
