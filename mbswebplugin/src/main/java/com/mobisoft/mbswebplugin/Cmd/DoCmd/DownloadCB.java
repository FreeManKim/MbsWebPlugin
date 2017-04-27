package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.app.Activity;

import java.io.File;

/**
 * Author：Created by fan.xd on 2017/4/27.
 * Email：fang.xd@mobisoft.com.cn
 * Description：进度条升级
 */

public interface DownloadCB {

    void create( Activity activity);

    void onUpdateComplete(File file);

    void onUpdateProgress(long current, long total);

    void onUpdateError(Throwable t);

}
