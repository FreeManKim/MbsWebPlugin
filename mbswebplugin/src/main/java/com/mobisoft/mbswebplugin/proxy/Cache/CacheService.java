package com.mobisoft.mbswebplugin.proxy.Cache;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.mobisoft.mbswebplugin.proxy.DB.WebviewCaheDao;
import com.mobisoft.mbswebplugin.proxy.tool.FileCache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class CacheService extends Service {
    private CacheBinder downloadBinder = new CacheBinder();
    public static final String TAG = "CacheService";
    private WebviewCaheDao dao;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    // We want at least 2 threads and at most 4 threads in the core pool,
    // preferring to have 1 less than the CPU count to avoid saturating
    // the CPU with background work
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
//    private ExecutorService customerExecutorService = new ThreadPoolExecutor(CORE_POOL_SIZE, Integer.MAX_VALUE, 0, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>());
    public ExecutorService customerExecutorService = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
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
            String checkMD5 = intent.getStringExtra("checkMD5");
            //getCacheDir().getAbsolutePath()
            dao = new WebviewCaheDao(getApplicationContext());
            Log.d(TAG, "onStartCommand() executed" + url + "\n" + cacheDir);
//            downloadManifest = new DownloadManifest(url, cacheDir, this, dao);
//            downloadManifest.start();
//            task = new DownloadTask(url, dao);
//            task.execute();

            InputStream from = null;
            File file;
            try {
                from = new FileInputStream(cacheDir + File.separator + url);
                file = FileCache.getInstance().creatCacheFile(url, cacheDir, this);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(from));
                String str;
                while (true) {
                    str = reader.readLine();
                    // 已经存在无需缓存
                    String cachePath = null;
                    Log.i(TAG, "下载：readLine:" + str);
                    if (str != null) {
                        if (str.contains("../")) {
                            cachePath = url.replace("cache.manifest", str);
                            cachePath = (new URL(cachePath)).toString();
                        } else if (!TextUtils.equals("cache.manifest", str)) {
                            cachePath = url.replace("cache.manifest", str);
                        }
                        if (TextUtils.isEmpty(cachePath)) {
                            continue;
                        }
                        if ((cachePath.endsWith(".jpg") || cachePath.endsWith(".html") || cachePath.endsWith(".css") || cachePath.contains(".js")
                                || cachePath.contains(".png") || cachePath.endsWith(".jpeg") || cachePath.endsWith(".JPEG")
                                || cachePath.endsWith(".ts") || cachePath.endsWith(".gif") || cachePath.endsWith(".mp3")
                                || cachePath.endsWith(".mp4") || cachePath.endsWith(".svg") || cachePath.endsWith(".woff")
                                || cachePath.endsWith(".ttf") || cachePath.endsWith(".eot") || cachePath.endsWith(".eot"))) {
                            dao.saveUrlPath("", cachePath, cachePath);
                            Log.e(TAG, "下载：cachePath:" + cachePath);


                            File file1 = FileCache.getInstance().creatCacheFile(cachePath, cacheDir, this);

//                            1、THREAD_POOL_EXECUTOR
//                            多个任务可以在线程池中异步并发执行。
//                            2、SERIAL_EXECUTOR
//                            把多个线程按串行的方式执行，所以是同步执行的。
//                            也就是说，只有当一个线程执行完毕之后，才会执行下个线程。

                            new DownLoadSrcTask(dao).executeOnExecutor(customerExecutorService, cachePath, file1.getAbsolutePath(), "");

                        }
                    } else {
                        new DownLoadSrcTask(dao).executeOnExecutor(customerExecutorService,null , checkMD5, "");
                        break;
                    }

                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
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
