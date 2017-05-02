package com.mobisoft.mbswebplugin.proxy.Cache;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * Author：Created by fan.xd on 2017/2/10.
 * Email：fang.xd@mobisoft.com.cn
 * Description： 创建manifest文件完成，会发送广播
 */
public class CacheBroadcast extends BroadcastReceiver {
private CacheService.CacheBinder  cacheBinder;
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            cacheBinder = (CacheService.CacheBinder) service;
            cacheBinder.startDownload();
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent!=null){
//            Log.i("CacheBroadcast",intent.getStringExtra("path"));
            intent.setClass(context,CacheService.class);
            context.startService(intent);

        }
    }
}
