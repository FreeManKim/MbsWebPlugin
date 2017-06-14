package com.mobisoft.mbswebplugin.proxy.Work;

import android.util.Log;

import com.mobisoft.mbswebplugin.base.ActivityManager;
import com.mobisoft.mbswebplugin.utils.ToastUtil;
import com.mobisoft.mbswebplugin.utils.Utils;

/**
 * Author：Created by fan.xd on 2017/5/12.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class DefaultDownloadSacCallback implements DownloadSrcCallback {
    @Override
    public void downLoadFinish() {
        ToastUtil.showShortToast(ActivityManager.get().topActivity(), "资源文件更新完毕");
        Log.i("downLoad","downLoadFinish");


    }

    @Override
    public void downLoadStart() {

        Utils.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showShortToast(ActivityManager.get().topActivity(), "开始更新资源文件");
            }
        });
        Log.i("downLoad","downLoadStart");

    }

    @Override
    public void downLoadFailure(final String message) {
        Utils.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showShortToast(ActivityManager.get().topActivity(), message);

            }
        });
        Log.i("downLoad","downLoadFailure");

    }

    @Override
    public void noNeedUpData() {
        Utils.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showShortToast(ActivityManager.get().topActivity(), "服务端与本地文件一致无需缓存");
            }
        });
        Log.i("downLoad","noNeedUpData");

    }
}
