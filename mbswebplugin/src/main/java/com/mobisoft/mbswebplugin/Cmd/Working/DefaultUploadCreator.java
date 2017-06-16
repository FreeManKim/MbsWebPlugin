package com.mobisoft.mbswebplugin.Cmd.Working;

import android.app.Activity;
import android.app.ProgressDialog;
import android.text.TextUtils;
import android.util.Log;

import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.base.SafeDialogOper;
import com.mobisoft.mbswebplugin.utils.ToastUtil;
import com.mobisoft.mbswebplugin.utils.Utils;

/**
 * Author：Created by fan.xd on 2017/6/15.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class DefaultUploadCreator implements UploadCB {
    private ProgressDialog dialog;
    private Activity activity;
    private MbsWebPluginContract.View view;

    @Override
    public void create(Activity activity, MbsWebPluginContract.View view) {
        if (activity == null || activity.isFinishing()) {
            Log.e("UploadCBCreator--->", "show download dialog failed:activity was recycled or finished");
            return;
        }
        this.view = view;
        this.activity = activity;
        dialog = new ProgressDialog(activity);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMax(100);
        dialog.setProgress(0);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

    }


    @Override
    public void onUploadComplete(final String json) {
        Utils.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                SafeDialogOper.safeDismissDialog(dialog);
                if (!TextUtils.isEmpty(json) && view != null) {
                    view.loadUrl(json);
                }
            }
        });

        activity = null;
    }

    @Override
    public void onUploadStart(int total) {

        dialog.setMax(total);
        SafeDialogOper.safeShowDialog(dialog);

    }

    @Override
    public void onUploadProgress(final int current, int total) {

        Utils.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                dialog.setProgress(current);
            }
        });
    }

    @Override
    public void onUploadError(String error) {
        if (!TextUtils.isEmpty(error))
            ToastUtil.showShortToast(activity.getApplicationContext(), error);
        else
            ToastUtil.showShortToast(activity.getApplicationContext(), "上传影像失败！");

    }

    @Override
    public void onUploadFinish(String message) {
        Utils.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                SafeDialogOper.safeDismissDialog(dialog);
            }
        });


        if (!TextUtils.isEmpty(message)) {
            ToastUtil.showShortToast(activity.getApplicationContext(), message);
        }


    }
}
