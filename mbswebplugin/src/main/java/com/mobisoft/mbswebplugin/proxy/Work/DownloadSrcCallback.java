package com.mobisoft.mbswebplugin.proxy.Work;

import android.support.annotation.WorkerThread;

/**
 * Author：Created by fan.xd on 2017/5/12.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public interface DownloadSrcCallback {
    /**
     * 下载完成
     */
    void downLoadFinish();

    /**
     * 开始下载
     */
    @WorkerThread
    void downLoadStart();

    /**
     * 下载失败
     *
     * @param message
     */
    @WorkerThread
    void downLoadFailure(String message);

    /**
     * 无需更新缓存文件
     */
    @WorkerThread
    void noNeedUpData();
}
