package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.mobisoft.mbswebplugin.BuildConfig;
import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.R;

/**
 * Author：Created by fan.xd on 2017/2/27.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class ErrorMethod extends DoCmdMethod {

    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
        String msg = String.format(context.getString(R.string.cmd_error_message), cmd);
//                    ToastUtil.showLongToast(context, msg);
        new Throwable(msg).printStackTrace();
        if (BuildConfig.DEBUG) {
            return msg;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(webView
                .getContext());
        builder.setTitle(R.string.cmd_error_title).setMessage(msg + "\n" + "params：" + params +
                "\n" + "callBack：" + callBack)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        //禁止响应按back键的事件
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        return msg;
    }
}
