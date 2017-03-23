package com.mobisoft.mbswebplugin.proxy.Cache;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.mobisoft.mbswebplugin.proxy.DB.WebviewCaheDao;


public class CacheService extends Service {
    private CacheBinder downloadBinder = new CacheBinder();
    public static final String TAG = "CacheService";
    private DownloadManifest downloadManifest;
    private WebviewCaheDao dao;
    private DownloadTask task;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            String url = intent.getStringExtra("url");
            String cacheDir = intent.getStringExtra("cacheDir");
            //getCacheDir().getAbsolutePath()
            dao = new WebviewCaheDao(getApplicationContext());
            Log.d(TAG, "onStartCommand() executed" + url + "\n" + cacheDir);
//            downloadManifest = new DownloadManifest(url, cacheDir, this, dao);
//            downloadManifest.start();
            task = new DownloadTask(url, dao);
            task.execute();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (downloadManifest != null)
            downloadManifest.stopDownload();

        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return downloadBinder;
    }

    /**
     *
     */
    class CacheBinder extends Binder {

        public void startDownload() {
            Log.d("TAG", "startDownload() executed");
            // 执行具体的下载任务
        }

    }
}
