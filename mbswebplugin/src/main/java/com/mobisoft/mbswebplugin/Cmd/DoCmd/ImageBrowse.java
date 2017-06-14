package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.view.ImageBowers.ImagePagerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Created by fan.xd on 2017/6/14.
 * Email：fang.xd@mobisoft.com.cn
 * Description：预览图片
 */

public class ImageBrowse extends DoCmdMethod {
    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {

        browseImage(params,context);

        return null;
    }


    //预览图片
    public void browseImage(String param,Context context) {
        try {
            JSONObject json = new JSONObject(param);
            JSONArray array = json.getJSONArray("bigImageURLs");
            String currentIndex = json.optString("currentIndex");
            if (TextUtils.isEmpty(currentIndex)) {
                currentIndex = "0";
            }
            List<String> list = new ArrayList<>();
            list.clear();

            for (int i = 0; i < array.length(); i++) {
                list.add(array.optString(i));
            }
            ImagePagerActivity.startImagePagerActivity((Activity)context, list, Integer.parseInt(currentIndex));

        }  catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
