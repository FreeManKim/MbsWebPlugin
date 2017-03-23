package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.utils.Utils;

/**
 * Author：Created by fan.xd on 2017/2/27.
 * Email：fang.xd@mobisoft.com.cn
 * Description： 弹出时间选择框  选择时间并返回给html5
 *              "time":  "10:15:59" 时间及格式
 *              {time:'2016-09-09',result:true}
 */

public class SetTimeMethod extends DoCmdMethod {
    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
        Utils.getTimePickerDialog(webView,context, Utils.DATA_SELECT_TIME, callBack, params);

        return null;
    }
}
