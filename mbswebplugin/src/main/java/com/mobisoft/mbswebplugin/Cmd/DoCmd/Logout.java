package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.utils.ActivityCollector;

/**
 * Author：Created by fan.xd on 2017/3/1.
 * Email：fang.xd@mobisoft.com.cn
 * Description： 当前app登出操作
 */

public class Logout extends DoCmdMethod {
    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
        //  TODO 登出操作 需要完善
        ActivityCollector.finishAll();
        System.exit(0);
        return null;
    }
}
