package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;

/**
 * Author：Created by fan.xd on 2017/2/27.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class ErrorMethod extends DoCmdMethod {

    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
        String msg = String.format("当前 cmd 命令：%s 没有被定义!", cmd );
//                    ToastUtil.showLongToast(context, msg);
        new Throwable(msg).printStackTrace();
        final AlertDialog.Builder builder = new AlertDialog.Builder(webView
                .getContext());
        builder.setTitle("cmd命令提示").setMessage(msg+"\n"+"params："+params+
                "\n"+"callBack："+callBack)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
