package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.content.Intent;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.Voide.EduMediaPlayer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author：Created by fan.xd on 2017/3/1.
 * Email：fang.xd@mobisoft.com.cn
 * Description：打开播放视频界面
 */

public class PlayVideo extends DoCmdMethod {
    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
        try {
            Intent i = new Intent(context, EduMediaPlayer.class);
            JSONObject json = new JSONObject(params);
            String url = json.optString("courseSrc");
//            Boolean studyState = json.optBoolean("studyState");
//            int currentTime = json.optInt("currentTime");
//            i.putExtra("studyState", studyState);
//            i.putExtra("currentTime", currentTime);
            i.putExtra("url", url);
            context.startActivity(i);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }
}
