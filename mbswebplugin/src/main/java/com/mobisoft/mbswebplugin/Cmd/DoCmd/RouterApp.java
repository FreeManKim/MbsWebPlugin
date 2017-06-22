package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.view.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author：Created by fan.xd on 2017/6/21.
 * Email：fang.xd@mobisoft.com.cn
 * Description： 根据
 */

public class RouterApp extends DoCmdMethod {
    @Override
    public String doMethod(HybridWebView webView, final Context context, MbsWebPluginContract.View view, final MbsWebPluginContract.Presenter presenter, String cmd, String parString, String callBack) {


        try {
            JSONObject jsonObject = new JSONObject(parString);
            final String scheme = jsonObject.optString("android");
            final String iosScheme = jsonObject.optString("ios");
            String market = jsonObject.optString("androidMarket");
            final String pkgName = jsonObject.optString("androidPkgName");
            String installed = jsonObject.optString("appMsg");
            String unInstalled = jsonObject.optString("marketMsg");
            final String appPath = jsonObject.optString("appUrl");
            boolean isPkgInstalled = isPkgInstalled(pkgName, context);

            AlertDialog mAlertDialog = new AlertDialog(context).builder();
            mAlertDialog.setTitle(context.getString(R.string.tishi));
            mAlertDialog.setCancelable(true);
            mAlertDialog.setNegativeButton(context.getString(R.string.cancel), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        ToastUtil.showLongToast(WebAppActivity.this, "取消！");
                }
            });
            if (isPkgInstalled) {
                mAlertDialog.setMsg(installed);

                mAlertDialog.setPositiveButton(context.getString(R.string.que_ren), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("uumobile://yongche/123123123")));

                    }
                });

            } else {
                mAlertDialog.setMsg(unInstalled);

                mAlertDialog.setPositiveButton(context.getString(R.string.que_ren), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://details?id=" + pkgName)); //跳转到应用市场，非Google Play市场一般情况也实现了这个接口
//存在手机里没安装应用市场的情况，跳转会包异常，做一个接收判断
                        if (intent.resolveActivity(context.getPackageManager()) != null) { //可以接收
                            context.startActivity(intent);
                        } else {
                            Uri uri = Uri.parse(appPath);
                            Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
                            presenter.startIntent(intent2);
                        }
                    }
                });
            }
            mAlertDialog.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 是否安装了某个app
     *
     * @param pkgName
     * @param context
     * @return
     */
    private boolean isPkgInstalled(String pkgName, Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }
}
