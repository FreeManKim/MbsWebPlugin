package com.mobisoft.mbswebplugin.proxy.Cache;

import android.content.Context;
import android.util.Log;

import com.mobisoft.mbswebplugin.proxy.tool.NetWorkUtils;
import com.mobisoft.mbswebplugin.proxy.tool.YUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Author：Created by fan.xd on 2017/2/7.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class FileCache {

    /**
     * 当前实例
     */
    public static FileCache fileCache = null;

    public static FileCache getInstance() {
        if (fileCache == null)
            fileCache = new FileCache();
        return fileCache;
    }

    /**
     * 创建 缓存
     *
     * @param url
     * @param cacheDir
     */
    public synchronized File creatCacheFile(String url, String cacheDir, Context mContext) throws IOException {
        if (!NetWorkUtils.isNetworkConnected(mContext)) {
            return null;
        }
//        if (url.contains(".png") || url.contains(".jpg")
//                || url.contains(".js") || url.contains(".css") || url.contains(".html")
//                || url.contains(".jpeg") || url.contains(".JPEG")) {
//        String md5URL = YUtils.md5(url);
        File file = new File(cacheDir + File.separator + YUtils.getFilePath(url));
        if (!file.exists()) {
            Log.i(">>>>>>>>>", "创建缓存文件路径: " + file.getName());
            file.mkdirs();
        }

        File file2 = new File(file.getAbsolutePath() + File.separator + YUtils.getFileName(url));
//        if (!file2.exists()) {
//            file.mkdir();
//        }
//            File file = new File(cacheDir + File.separator + TextUtils.substring(url,url.lastIndexOf("/"),url.length()));

        Log.i(">>>>>>>>>", "创建缓存文件:   =  " + file2.getName() + " 地址：" + url
                + "  mimeType:" + " " + file2.getAbsolutePath() + " ");
        return file2;
//        }
//        return null;
    }

    /**
     * 读取缓存
     *
     * @param url
     * @return
     * @throws IOException
     */
    public InputStream getCache(String url, String cacheDir, Context context) throws IOException {
        // 缓存文件
//        File file = new File(cacheDir + File.separator + YUtils.getFilePath(url) + File.separator + YUtils.getFileName(url));
        File file = new File(cacheDir);
        if (file.exists()) {
            FileInputStream fileInputStream = null;
            try {

                // 没有网络连接
                if (!NetWorkUtils.isNetworkConnected(context)) {
                    fileInputStream = new FileInputStream(file);
                    Log.e(">>>>>>>>>", "读缓存:" + "没有网络连接" + "   url:" + url);
                    return fileInputStream;

                } else if (url.contains("?")) {
                    Log.e(">>>>>>>>>", "没有读取缓存:" + "url包含？号：" + "   url:" + url);
                    return null;
                } else if (url.endsWith(".jpg") || url.endsWith(".css") || url.contains(".js")
                        || url.contains(".png")  || url.endsWith(".jpeg") || url.endsWith(".JPEG")
                        || url.endsWith(".ts")   || url.endsWith(".gif") || url.endsWith(".mp3")
                        || url.endsWith(".mp4")  || url.endsWith(".svg") || url.endsWith(".woff")
                        || url.endsWith(".ttf")  || url.endsWith(".eot") || url.endsWith(".eot")
                        ||url.endsWith(".html")) {
                    fileInputStream = new FileInputStream(file);
                    // 资源文件
                    Log.e(">>>>>>>>>", "读缓存:" + "资源文件" + "   url:" + url);
                    return fileInputStream;
                } else {
                    Log.i(">>>>>>>>>", "没有读取缓存:" + "接口：" + "   url:" + url);
                    return null;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e(">>>>>>>>>", "FileNotFoundException:" + e.getMessage() + "   url:" + url);
            }
            return null;
        }
//        String md5URL = YUtils.md5(url);
//        File file = new File(cacheDir + File.separator + md5URL);
//        File file = new File(cacheDir + File.separator + url);

//        Log.i(">>>>>>>>>", "getCache:   =  " + file.getName() + " 地址：" + url);
//        if (url.contains(".png") || url.contains(".jpg")
//                || url.contains(".js") || url.contains(".css") || url.contains(".html")
//                || url.contains(".jpeg") || url.contains(".JPEG")) {
        //读取缓存的html页面

//        }

        return null;
    }
}
