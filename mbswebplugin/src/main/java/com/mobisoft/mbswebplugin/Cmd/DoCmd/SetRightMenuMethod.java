package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;

/**
 * Author：Created by fan.xd on 2017/2/27.
 * Email：fang.xd@mobisoft.com.cn
 * Description： 设置右上角菜单
 * 入参：{"item":[{“url”:”xxx”,”name”:”xxx”,”icon”:”xxx”,”callback”:”cancel”} ]}
 * 出参：{result:true}
 */

public class SetRightMenuMethod extends DoCmdMethod {
    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
//        ((WebAppActivity)context).setTopAndRightMenu(params);
        view.setTopAndRightMenu(params);
        return null;
    }
}
