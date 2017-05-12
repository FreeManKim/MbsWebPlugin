package com.mobisoft.mbswebplugin.proxy.Work;

import com.mobisoft.mbswebplugin.base.ActivityManager;
import com.mobisoft.mbswebplugin.utils.ToastUtil;

/**
 * Author：Created by fan.xd on 2017/5/12.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class DefaultDownloadSacCallback implements DownloadSrcCallback {
    @Override
    public void downLoadFinish() {
        ToastUtil.showShortToast(ActivityManager.get().topActivity(), "资源文件更新完毕");

    }

    @Override
    public void downLoadStart() {
        ToastUtil.showShortToast(ActivityManager.get().topActivity(), "开始更新资源文件");

    }

    @Override
    public void downLoadFailure(String message) {
        ToastUtil.showShortToast(ActivityManager.get().topActivity(), message);

    }

    @Override
    public void noNeedUpData() {
        ToastUtil.showShortToast(ActivityManager.get().topActivity(), "服务端与本地文件一致无需缓存");
    }
}
