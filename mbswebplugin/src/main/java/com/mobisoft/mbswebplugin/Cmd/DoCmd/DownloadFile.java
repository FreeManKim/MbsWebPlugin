package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.os.Environment;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.base.ActivityManager;
import com.mobisoft.mbswebplugin.proxy.tool.YUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Author：Created by fan.xd on 2017/3/3.
 * Email：fang.xd@mobisoft.com.cn
 * Description：下载文件
 */

public class DownloadFile extends DoCmdMethod {
    private DefaultDownloadCreator downloadCreator;

    @Override
    public String doMethod(final HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, final String params, final String callBack) {
        // TODO 下载文件
        downloadCreator = new DefaultDownloadCreator();
        downloadCreator.create(ActivityManager.get().topActivity());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject(params);
                    String fileUrl = object.getString("url");
//                    String fileUrl = "http://euat.idoutec.cn/prdcpic_dbb/insbuy.apk";
                    URL url = new URL(fileUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(10000);
                    final long contentLength = connection.getContentLength();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStream = connection.getInputStream();
                        String fileName = YUtils.getFileName(fileUrl);
                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                                + File.separator + "Download" + File.separator + "MBS");
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        final File fileDown = new File(file.getAbsolutePath() + File.separator + fileName);
                        FileOutputStream fileOutputStream = new FileOutputStream(fileDown);
                        byte[] buffer = new byte[1024];
                        int length = 0;
                        long offset = 0;
                        long start = System.currentTimeMillis();

                        while ((length = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, length);
                            offset += length;
                            long end = System.currentTimeMillis();
                            if (end - start > 1000) {
                                downloadCreator.onUpdateProgress(offset, contentLength);
                            }
                        }

                        connection.disconnect();
                        fileOutputStream.close();
                        connection = null;
                        downloadCreator.onUpdateComplete(fileDown);

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    downloadCreator.onUpdateError(null);
                } catch (IOException e) {
                    e.printStackTrace();
                    downloadCreator.onUpdateError(null);
                } catch (JSONException e) {
                    e.printStackTrace();
                    downloadCreator.onUpdateError(null);
                }
            }
        }).start();
        return null;
    }

    private void sendUpdateProgress(long offset, long contentLength) {

    }
}
