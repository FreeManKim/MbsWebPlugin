package com.mobisoft.mbswebplugin.proxy.Cache;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.mobisoft.mbswebplugin.proxy.DB.WebviewCaheDao;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * Author：Created by fan.xd on 2017/2/10.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class DownloadManifest extends Thread {


    public static final String TAG = "DownloadManifest";
    /**
     * 缓存路径
     */
    private final String cacheDir;
    /**
     * 基本路径
     */
    private final String mUrl;
    /**
     * mainfest文件名
     */
    private final String fileName;
    /**
     * 数据库
     */
    private final WebviewCaheDao webviewCaheDao;
    private File file;
    private final Context mContext;
    /**
     * 是否开启线程
     */
    private boolean mIsRunning = false;

    FileLock flout = null;

    public DownloadManifest(String url, String cacheDir, Context context, WebviewCaheDao dao) {
        this.cacheDir = cacheDir;
        this.mUrl = url;
        try {
            this.file = FileCache.getInstance().creatCacheFile(url, cacheDir, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mContext = context;
        this.fileName = cacheDir + File.separator + url;
        Log.i(TAG, "fileName:" + fileName);
        this.webviewCaheDao = dao;
    }

    @Override
    public void run() {
        try {
            InputStream from = new FileInputStream(fileName);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(from));
            String str = null;
            String LastModified = "";
            while (true) {

                str = reader.readLine();
                // 已经存在无需缓存
                String cachePath = null;
                Log.i(TAG, "下载：readLine:" + str);
                if (str != null) {
                    // 替换下载路径中包含的引用字符 例如： ../assets/appcan/css/icons/icon-ok-act.png
                    if (str.contains("../")) {
                        cachePath = mUrl.replace("cache.manifest", str);
                        cachePath = (new URL(cachePath)).toString();
                    } else if (str.contains("Last-Modified:")) {
                        LastModified = str.substring(str.indexOf(":") + 1);
                    } else if (!TextUtils.equals("cache.manifest", str)) {
                        cachePath = mUrl.replace("cache.manifest", str);
                    }
                    Log.e(TAG, "下载：cachePath:" + cachePath);
                    if (TextUtils.isEmpty(cachePath)) {
                        return;
                    }

//                    cachePath = cachePath.replace("../", "");
                    //str.endsWith(".jpg") || str.endsWith(".css") || str.contains(".js") || str.contains(".png")\\
//                    || str.endsWith(".html")
                    if ((cachePath.endsWith(".jpg") || cachePath.endsWith(".html") || cachePath.endsWith(".css") || cachePath.contains(".js")
                            || cachePath.contains(".png") || cachePath.endsWith(".jpeg") || cachePath.endsWith(".JPEG")
                            || cachePath.endsWith(".ts") || cachePath.endsWith(".gif") || cachePath.endsWith(".mp3")
                            || cachePath.endsWith(".mp4") || cachePath.endsWith(".svg") || cachePath.endsWith(".woff")
                            || cachePath.endsWith(".ttf") || cachePath.endsWith(".eot") || cachePath.endsWith(".eot")) &&
                            TextUtils.isEmpty(webviewCaheDao.getUrlPath(LastModified, cachePath))) {
                        webviewCaheDao.saveUrlPath(LastModified, cachePath, cachePath);
                        String[] args = new String[1];
                        args[0] = cachePath;
                        try {
                            URL uri = new URL(cachePath);
                            Log.i(TAG, "下载：URL:" + cachePath);

                            HttpURLConnection connection;
                            if (cachePath.contains("https")) {
                                SSLContext sc = SSLContext.getInstance("TLS");
                                sc.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
                                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                                HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
                                connection = (HttpsURLConnection) uri.openConnection();
                            } else {
                                connection = (HttpURLConnection) uri.openConnection();
                            }

                            connection.setRequestProperty("Accept-Encoding", "identity");


//                            URLConnection connection = uri.openConnection();
                            String responseHeader = getResponseHeader(connection);
                            Log.e(TAG, "头文件:" + responseHeader);
                            File file1 = FileCache.getInstance().creatCacheFile(cachePath, cacheDir, mContext);

                            //对该文件加锁
//                            RandomAccessFile output1 = new RandomAccessFile(file1, "rw");


                            FileOutputStream output = new FileOutputStream(file1);
//                            FileChannel fcout = output.getChannel();

                            InputStream uristream = connection.getInputStream();
                            byte[] buffer1 = new byte[1024];
//                            Log.i(TAG, "cachePath:" + cachePath);
//                            Log.i(TAG, "file:" + file.getAbsolutePath());
//                            Log.i(TAG, "connection.getResponseCode():" + connection.getResponseCode());

                            if (connection.getResponseCode() == 200 && connection.getContentLength() > 0) {
                                if (!TextUtils.isEmpty(responseHeader)) {
                                    ByteArrayInputStream stringInputStream = new ByteArrayInputStream(
                                            responseHeader.getBytes());
                                    byte[] buffer2 = new byte[1024];

                                    while (true) {
                                        int r = stringInputStream.read(buffer1);
                                        if (r < 0) {
                                            break;
                                        }
                                        if (output != null) {
                                            output.write(buffer2, 0, r);
                                        }
                                    }
                                    output.close();
                                    output = new FileOutputStream(file1, true);
                                }
                                while (true) {
                                    int r = uristream.read(buffer1);
                                    if (r < 0) {
                                        break;
                                    }
                                    if (output != null) {
                                        output.write(buffer1, 0, r);
                                    }
                                }
                                Log.i(TAG, "下载完成getResponseCode:" + file1.getAbsolutePath());
                                webviewCaheDao.saveUrlPath(LastModified, cachePath, file1.getAbsolutePath());
                            } else if (connection.getResponseCode() == 200 && cachePath.endsWith(".html")) {
                                output = new FileOutputStream(file1, true);
                                while (true) {
                                    int r = uristream.read(buffer1);
                                    if (r < 0) {
                                        break;
                                    }
                                    if (output != null) {
                                        output.write(buffer1, 0, r);
                                    }
                                }
                                webviewCaheDao.saveUrlPath(LastModified, cachePath, file1.getAbsolutePath());
                            } else {
                                file1.delete();
                                webviewCaheDao.deletKey(cachePath);
                            }
//                            if(flout!=null)
//                            flout.release();
                            output.close();
                            uristream.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                            webviewCaheDao.deletKey(cachePath);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (KeyManagementException e) {
                            e.printStackTrace();
                        }
                    }

                } else
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.run();
    }

    /**
     * 文件锁
     *
     * @param cachePath
     * @param fcout
     */
    private void FileTryLock(String cachePath, FileChannel fcout) {
        while (true) {
            try {
                flout = fcout.tryLock();
                break;
            } catch (Exception e) {
                try {
                    sleep(1000);
                    Log.i(TAG, "有其他线程正在操作该文件，当前线程休眠1000毫秒:" + cachePath);
                } catch (InterruptedException e1) {
                    Log.e(TAG, "当前线程休眠1000毫秒异常:" + e1.getMessage());

                    e1.printStackTrace();
                    break;
                }
            }
        }
    }

    /**
     * 启动线程
     */
    public synchronized void startDownload() {
        mIsRunning = true;
        start();
    }

    /**
     * 结束线程
     */
    public synchronized void stopDownload() {
        mIsRunning = false;
    }

    //读取响应头
    private String getResponseHeader(URLConnection conn) {
        boolean isHtml = conn.getURL().toString().contains(".html");
        Log.e(TAG, "url地址，getResponseHeader：" + conn.getURL().toString());

        StringBuilder sbResponseHeader = new StringBuilder();

        sbResponseHeader.append("HTTP/1.1 200 OK");
        sbResponseHeader.append("\n");
        sbResponseHeader.append("Server: ");
        sbResponseHeader.append(conn.getHeaderField("Server"));
        sbResponseHeader.append("\n");

        sbResponseHeader.append("Date: ");
//        sbResponseHeader.append("Fri, 17 Feb 2017 11:29:57 GMT");
        sbResponseHeader.append(conn.getHeaderField("Date"));
        sbResponseHeader.append("\n");

        sbResponseHeader.append("Content-Type: ");
        sbResponseHeader.append(conn.getContentType());
        sbResponseHeader.append("\n");

        sbResponseHeader.append("Content-Length: ");
        sbResponseHeader.append(conn.getContentLength());
        sbResponseHeader.append("\n");


//        Calendar cd = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("EEE d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
////        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 设置时区为GMT  +8为北京时间东八区
//        String str = sdf.format(cd.getTime());
//        System.out.println(str);
        sbResponseHeader.append("Last-Modified: ");
        sbResponseHeader.append(conn.getHeaderField("Last-Modified"));
        sbResponseHeader.append("\n");

//        if (isHtml) {
//            sbResponseHeader.append("Transfer-Encoding: ");
//            sbResponseHeader.append("chunked");
//            sbResponseHeader.append("\n");
//        }


        sbResponseHeader.append("Connection: ");
        sbResponseHeader.append("close");
        sbResponseHeader.append("\n");

        sbResponseHeader.append("ETag: ");
        sbResponseHeader.append(conn.getHeaderField("ETag"));
        sbResponseHeader.append("\n");
        if (isHtml) {
            sbResponseHeader.append("Access-Control-Allow-Origin: *\n" +
                    "Access-Control-Allow-Methods: GET, POST, OPTIONS\n");
        }

//
        if (isHtml) {
            sbResponseHeader.append("Accept-Encoding: ");
            sbResponseHeader.append("identity");
        } else {
            sbResponseHeader.append("Accept-Ranges: ");
            sbResponseHeader.append("bytes");
        }
        sbResponseHeader.append("\n");
        sbResponseHeader.append("\n");


        return sbResponseHeader.toString();
    }

    /**
     * 下载html
     *
     * @param url
     * @param file
     * @return
     */
    public String getHTML(String url, File file) {
        try {
            URL newUrl = new URL(url);
            HttpURLConnection connect = (HttpURLConnection) newUrl.openConnection();
//            FileOutputStream fos = new FileOutputStream(file);
            PrintStream output = new PrintStream(new FileOutputStream(file));
//            URLConnection connect=newUrl.openConnection();
            Log.i(TAG, "getContentLength:" + connect.getContentLength());
            InputStream uristream = connect.getInputStream();
            int read_len;
            byte[] buffer1 = new byte[1024];
            while ((read_len = uristream.read(buffer1)) > 0) {
                output.write(buffer1, 0, read_len);
            }
            uristream.close();

//            connect.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
//            DataInputStream dis = new DataInputStream(connect.getInputStream());
//            BufferedReader in = new BufferedReader(new InputStreamReader(dis, "UTF-8"));//目标页面编码为UTF-8
//            String html = "";
//            String readLine = null;
//            while ((readLine = in.readLine()) != null) {
////                html=html+readLine+"\n";
//                Log.i(TAG, readLine);
//                ps.append(readLine + "\n");
//            }
//            in.close();
            return null;
        } catch (MalformedURLException me) {

            me.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

}
