package com.mobisoft.mbswebplugin.proxy.server;

import android.content.Context;
import android.util.Log;

import com.mobisoft.mbswebplugin.proxy.tool.FileCache;
import com.mobisoft.mbswebplugin.proxy.DB.WebviewCaheDao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 *
 */
public class SocketConnect extends Thread {
    private static final String TAG = "SocketConnect";
    private static WebviewCaheDao DBdao;
    /**
     * 缓存路径
     */
    private String cacheDir;
    /**
     * 文件路径
     */
    private String mUrl;
    private Context mContext;


    private InputStream from;
    private OutputStream to;
    private File file;
    FileLock flout = null;

    public SocketConnect(Socket from, Socket to) throws IOException {
        this.from = from.getInputStream();
        this.to = to.getOutputStream();
        start();
    }

    /**
     * 缓存
     *
     * @param from
     * @param to
     * @throws IOException
     */
    public SocketConnect(Socket from, Socket to, String Url, String cacheDir, boolean type, Context context) throws IOException {
        this.from = from.getInputStream();

        this.to = to.getOutputStream();
        this.file = FileCache.getInstance().creatCacheFile(Url, cacheDir, context);
        this.mUrl = Url;
        this.cacheDir = cacheDir;
        this.mContext = context;
        start();

    }


    @Override
    public void run() {
        final byte[] buffer = new byte[1024];
        FileOutputStream out = null;
        //|| mUrl.contains(".html")|| mUrl.contains(".html")
        boolean b = mUrl.endsWith("cache.manifest");
        // && TextUtils.isEmpty(DBdao.getUrlPath(mUrl)
        try {
            if (file != null && b) {
                out = new FileOutputStream(file);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            out = null;
        }

        try {

            while (true) {
                int r = from.read(buffer);
                if (r < 0) {
                    break;
                }
                to.write(buffer, 0, r);
                if (out != null && b) {
                    out.write(buffer, 0, r);
                    Log.e(TAG, "每一个：" + new String(buffer, 0, r));
                }
            }
            from.close();
            to.close();
            if (out != null && b) {
                out.flush();
                out.close();
                DBdao.saveUrlPath("", mUrl, file.getAbsolutePath());
            }
//            else {
//                Log.e(TAG, "deleteWebviewList" + mUrl);
//                DBdao.deletKey(mUrl);
//            }
            // 创建cache.manifest 完成发送广播去下载，manifest里所包含得文件
         /*   if (mUrl.endsWith("cache.manifest") && file != null) {
                Intent broadcast = new Intent();
                broadcast.setAction("com.mobisoft.loachtml.Cache.CacheBroadcast");
                broadcast.putExtra("path", file.getAbsolutePath());
                broadcast.putExtra("url", mUrl);
                broadcast.putExtra("cacheDir", cacheDir);
                mContext.sendBroadcast(broadcast);
            }*/

        } catch (IOException io) {
//            io.printStackTrace();
//            DBdao.deletKey(mUrl);
        }
    }

    public static void connect(Socket first, Socket second, String urlString, String cacheDir, Context mContext) {
        Log.i(TAG, "Socket connect client:" + first.toString()
                + "");
        Log.i(TAG, "Socket connect server:" + second.toString());

        try {
            SocketConnect sc1 = new SocketConnect(first, second);
            SocketConnect sc2 = new SocketConnect(second, first);
            try {
                sc1.join();
            } catch (InterruptedException e) {
            }
            try {
                sc2.join();
            } catch (InterruptedException e) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 缓存测试
     *
     * @param first
     * @param second
     * @param url
     */
    public static void connect(Socket first, Socket second, String url, String cacheDir, Context context, WebviewCaheDao dao) {
        Log.i(TAG, "Socket connect client:" + first.toString()
                + "");
        Log.i(TAG, "Socket connect server:" + second.toString());
        try {
            Log.i(TAG, "Socket connect url:" + url+
            "\nclient:"+first.getKeepAlive()+" server :"+second.getKeepAlive());
        } catch (SocketException e) {
            e.printStackTrace();
        }
        DBdao = dao;

        try {
            SocketConnect sc1 = new SocketConnect(first, second, url, cacheDir, true, context);
            SocketConnect sc2 = new SocketConnect(second, first, url, cacheDir, false, context);
            try {
                sc1.join();
            } catch (InterruptedException e) {
            }
            try {
                sc2.join();
            } catch (InterruptedException e) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件锁
     *
     * @param cachePath
     * @param fcout
     * @return false:文件正在被占用，加锁失败  ，true：文件空闲，加锁成功
     */
    private boolean FileTryLock(String cachePath, FileChannel fcout) {
        while (true) {
            try {
                flout = fcout.tryLock();
                Log.i(TAG, "flout不为空:FileTryLock");

                break;
            } catch (Exception e) {
                Log.i(TAG, "有其他线程正在操作该文件，当前线程休眠1000毫秒:" + cachePath);
                return false;
            }
        }
        return true;
    }

}
