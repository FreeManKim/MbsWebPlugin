package com.mobisoft.mbswebplugin.Cmd;

import android.content.Context;
import android.content.Intent;

import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;

/**
 * Author：Created by fan.xd on 2017/3/1.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public interface ResultListener {
    void onActivityResult(Context context, HybridWebView view, int requestCode, int resultCode, Intent data);
}
