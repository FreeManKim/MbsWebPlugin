package com.mobisoft.mbswebplugin.proxy.Cache;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.mobisoft.mbswebplugin.base.ActivityManager;
import com.mobisoft.mbswebplugin.base.Recycler;
import com.mobisoft.mbswebplugin.proxy.server.ProxyConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * Author：Created by fan.xd on 2017/3/15.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class CacheManifest extends Thread implements Recycler.Recycleable {
    public static final String TAG = "CacheManifest";
    private Activity mContext;
    private String url;
    private String path;

    public CacheManifest(String path, String url) {
        this.url = ProxyConfig.getConfig().getCacheUrl();
        this.path = ProxyConfig.getConfig().getCachePath();
        start();
    }

    public CacheManifest() {
        this.url = ProxyConfig.getConfig().getCacheUrl();
        this.path = ProxyConfig.getConfig().getCachePath();
        mContext = ActivityManager.get().topActivity();
    }

    public void execute() {
        this.start();
    }

    @Override
    public void run() {
        try {
            URL uri = new URL(url);
            HttpURLConnection connection;
            if (url.contains("https")) {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
                connection = (HttpsURLConnection) uri.openConnection();
            } else {
                connection = (HttpURLConnection) uri.openConnection();

            }


            int code = connection.getResponseCode();
            if (code == 200) {
                InputStream in = connection.getInputStream();
                File file1 = FileCache.getInstance().creatCacheFile(url, path, mContext);
                File fileTemp = FileCache.getInstance().creatCacheFile(url + "temp", path, mContext);

                FileOutputStream out = new FileOutputStream(fileTemp);
                byte[] buffer = new byte[1024];
                while (true) {
                    int n = in.read(buffer);
                    if (n < 0)
                        break;
                    else {
                        out.write(buffer, 0, n);
                    }
                }
                out.flush();
                out.close();
                in.close();
                if (DefaultCheck.getInstance().check(file1, fileTemp)) {
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ActivityManager.get().topActivity(), "服务端与本地文件一致无需缓存", Toast.LENGTH_LONG).show();

                        }
                    });
                    return;
                } else {
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ActivityManager.get().topActivity(), "开始更新Manifest文件！", Toast.LENGTH_LONG).show();

                        }
                    });

                }
                Intent broadcast = new Intent();
                broadcast.setAction("com.mobisoft.loachtml.Cache.CacheBroadcast");
                broadcast.putExtra("path", file1.getAbsolutePath());
                broadcast.putExtra("url", url);
                broadcast.putExtra("cacheDir", path);
                mContext.sendBroadcast(broadcast);
            } else {
                new Throwable("下载失败ResponseCode：" + code + "\n 地址：" + url);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } finally {
            Recycler.release(this);
        }

    }


    @Override
    public void release() {
        Log.i(TAG, "release");
        mContext = null;
        url = null;
        path = null;
    }
}
