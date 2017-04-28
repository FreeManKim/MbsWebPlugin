package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.mobisoft.mbswebplugin.base.SafeDialogOper;
import com.mobisoft.mbswebplugin.utils.ToastUtil;
import com.mobisoft.mbswebplugin.utils.Utils;

import java.io.File;

public class DefaultDownloadCreator implements DownloadCB {
    private ProgressDialog dialog;
private  Activity activity;
    @Override
    public void onUpdateProgress(final long current, final long total) {
        Utils.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                int percent = (int) (current * 1.0f / total * 100);
                dialog.setProgress(percent);            }
        });

    }

    @Override
    public void onUpdateError(Throwable t) {
        SafeDialogOper.safeDismissDialog(dialog);

    }

    @Override
    public void create(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            Log.e("DownDialogCreator--->", "show download dialog failed:activity was recycled or finished");
            return;
        }
       this.activity = activity;
        dialog = new ProgressDialog(activity);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMax(100);
        dialog.setProgress(0);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        SafeDialogOper.safeShowDialog(dialog);
    }

    @Override
    public void onUpdateComplete(final File file) {

        Utils.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                SafeDialogOper.safeDismissDialog(dialog);
                if(null!=file)
                ToastUtil.showShortToast(activity,"已经下载至："+file.getAbsolutePath());
                activity = null;
            }
        });

    }
}