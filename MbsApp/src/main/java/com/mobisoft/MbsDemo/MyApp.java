package com.mobisoft.MbsDemo;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.mobisoft.mbswebplugin.base.ActivityManager;
import com.mobisoft.mbswebplugin.proxy.server.ProxyConfig;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.io.File;

/**
 * Author：Created by fan.xd on 2017/3/14.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class MyApp extends Application {
    public static RefWatcher getRefWatcher(Context context) {
        MyApp application = (MyApp) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;


    @Override
    public void onCreate() {
        //http://elearning.mobisoft.com.cn/mobile/cache.manifest
        //http://ainewdev.cttq.com/tianxin/app_AddressBook/cache.manifest
        super.onCreate();
        ProxyConfig.getConfig()
                .setCachePath(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                        + "AAA_1")
                .setCacheUrl("http://elearning.mobisoft.com.cn/mobile/cache.manifest")
                .setPORT(8183)
                .setShowDialog(true);
        ActivityManager.get().registerSelf(this.getApplicationContext());
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher = LeakCanary.install(this);

    }
}
