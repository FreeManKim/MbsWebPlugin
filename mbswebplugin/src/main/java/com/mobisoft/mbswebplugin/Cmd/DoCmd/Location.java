package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;

/**
 * Author：Created by fan.xd on 2017/3/3.
 * Email：fang.xd@mobisoft.com.cn
 * Description： 获取当前位置经纬度
 */

public class Location extends DoCmdMethod {
    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {

        //TODO 获取当前位置经纬度 待实现

        return null;
    }
}
