package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.utils.Utils;

/**
 * Author：Created by fan.xd on 2017/3/20.
 * Email：fang.xd@mobisoft.com.cn
 * Description： 发送邮件
 */

public class SendEmail extends DoCmdMethod {
    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
        Utils.sendEmail(context, params);
        return null;
    }
}
