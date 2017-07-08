package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsRequestPermissionsListener;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.utils.Utils;

/**
 * Author：Created by fan.xd on 2017/4/27.
 * Email：fang.xd@mobisoft.com.cn
 * Description： 发短信
 */

public class SendMSM extends DoCmdMethod implements MbsRequestPermissionsListener{
    private String params;
    private Context context;
    private MbsWebPluginContract.Presenter presenter;

    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
        this.context = context;
        this.params = params;
        this.presenter = presenter;
        presenter.setMbsRequestPermissionsResultListener(this);
        Utils.getSMS(context, params);



        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(permissions[0]== Manifest.permission.SEND_SMS && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Utils.doSendSMSTo(context, params);
            presenter.setMbsRequestPermissionsResultListener(null);

        }
    }
}
