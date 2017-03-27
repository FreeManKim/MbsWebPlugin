package com.mobisoft.mbswebplugin.proxy.Cache;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.mobisoft.mbswebplugin.base.ActivityManager;
import com.mobisoft.mbswebplugin.base.Recycler;
import com.mobisoft.mbswebplugin.proxy.DB.WebviewCaheDao;
import com.mobisoft.mbswebplugin.proxy.server.ProxyConfig;
import com.mobisoft.mbswebplugin.utils.ToastUtil;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * Author：Created by fan.xd on 2017/3/22.
 * Email：fang.xd@mobisoft.com.cn
 * Description：缓存文件下载器
 */

public class DownloadTask extends AsyncTask<String, Integer, String> implements Recycler.Recycleable {
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
    private WebviewCaheDao webviewCaheDao;
    private File file;

    public static final String TAG = "DownloadTask";
    private ProgressDialog progressDialog;
    private Activity mContext;

    public DownloadTask(String url, WebviewCaheDao dao) {
        this.cacheDir = ProxyConfig.getConfig().getCachePath();
        this.mUrl = url;
        try {
            mContext = ActivityManager.get().topActivity();
            this.file = FileCache.getInstance().creatCacheFile(url, cacheDir, mContext);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.fileName = cacheDir + File.separator + url;
        Log.i(TAG, "fileName:" + fileName);
        this.webviewCaheDao = dao;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ToastUtil.showShortToast(mContext,"准备下载！Manifest");
        if (ProxyConfig.getConfig().isShowDialog()) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("缓存下载进度");
            progressDialog.setMax(100);
            progressDialog.show();
        }


    }

    @Override
    protected String doInBackground(String... params) {
        try {
            InputStream from = new FileInputStream(fileName);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(from));
            String str;
            String LastModified = "1";
            long le = file.length();
            int pro = 0;
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
                    }else if (!TextUtils.equals("cache.manifest", str)) {
                        cachePath = mUrl.replace("cache.manifest", str);
                    }
                    Log.e(TAG, "下载：cachePath:" + cachePath);
                    if (TextUtils.isEmpty(cachePath)) {
                        continue;
                    }
                    long progress = str.length() * 100 / le;
                    pro += progress;
                    publishProgress(pro);
                    if ((cachePath.endsWith(".jpg") || cachePath.endsWith(".html") || cachePath.endsWith(".css") || cachePath.contains(".js")
                            || cachePath.contains(".png") || cachePath.endsWith(".jpeg") || cachePath.endsWith(".JPEG")
                            || cachePath.endsWith(".ts") || cachePath.endsWith(".gif") || cachePath.endsWith(".mp3")
                            || cachePath.endsWith(".mp4") || cachePath.endsWith(".svg") || cachePath.endsWith(".woff")
                            || cachePath.endsWith(".ttf") || cachePath.endsWith(".eot") || cachePath.endsWith(".eot"))) {
                        webviewCaheDao.saveUrlPath(LastModified, cachePath, cachePath);

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

                            String responseHeader = getResponseHeader(connection);
                            Log.e(TAG, "头文件:" + responseHeader);
                            File file1 = FileCache.getInstance().creatCacheFile(cachePath, cacheDir, mContext);


                            FileOutputStream output = new FileOutputStream(file1);
                            if (!TextUtils.isEmpty(responseHeader)) {
                                ByteArrayInputStream stringInputStream = new ByteArrayInputStream(
                                        responseHeader.getBytes());
                                byte[] buffer2 = new byte[1024];

                                while (true) {
                                    int r = stringInputStream.read(buffer2);
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
                            InputStream uristream = connection.getInputStream();

                            byte[] buffer1 = new byte[1024];
//
                            if (connection.getResponseCode() == 200 && connection.getContentLength() > 0) {

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
                            } else {
                                file1.delete();
                                webviewCaheDao.deletKey(cachePath);
                            }
                            output.close();
                            uristream.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                            webviewCaheDao.deletKey(cachePath);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                            Recycler.release(this);
                        } catch (KeyManagementException e) {
                            e.printStackTrace();
                            Recycler.release(this);
                        }
                    }

                } else
                    break;
            }
        } catch (final IOException e) {
            e.printStackTrace();
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showLongToast(mContext,"IOException！"+e.getMessage());

                }
            });

            Recycler.release(this);
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (progressDialog != null)
            progressDialog.setProgress(values[0]);

    }

    @Override
    protected void onPostExecute(String s) {
        Recycler.release(this);
        Toast.makeText(ActivityManager.get().topActivity(), "onPostExecute！+下载完成",Toast.LENGTH_LONG).show();

    }

    /**
     * 读取响应头
     *
     * @param conn
     * @return
     */
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


    @Override
    public void release() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = null;
        webviewCaheDao = null;
        mContext = null;
    }
}
