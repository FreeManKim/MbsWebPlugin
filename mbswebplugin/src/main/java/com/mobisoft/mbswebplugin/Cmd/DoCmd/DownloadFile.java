package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.base.ActivityManager;

import org.json.JSONException;
import org.json.JSONObject;

import static com.mobisoft.mbswebplugin.base.AppConfing.PERMISSIONS_REQUEST_CODE_SD;

/**
 * Author：Created by fan.xd on 2017/3/3.
 * Email：fang.xd@mobisoft.com.cn
 * Description：下载文件
 */

public class DownloadFile extends DoCmdMethod {
    private DefaultDownloadCreator downloadCreator;

    @Override
    public String doMethod(final HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, final String params, final String callBack) {
        // TODO 下载文件
        downloadCreator = new DefaultDownloadCreator();
        downloadCreator.create(ActivityManager.get().topActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)// 大于6.0 权限检查
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                downLoad(params);
            } else {
                // Ask for one permission
                ((Activity) context).requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE_SD);
            }
        } else {
            downLoad(params);
        }

        return null;
    }

    private void downLoad(String params) {
        try {
            JSONObject object = new JSONObject(params);
            String fileUrl = object.getString("url");
            downloadCreator.downloadFile(fileUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
