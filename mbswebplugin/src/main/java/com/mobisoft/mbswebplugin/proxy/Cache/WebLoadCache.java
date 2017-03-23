package com.mobisoft.mbswebplugin.proxy.Cache;

import android.content.Context;
import android.util.Log;

import com.mobisoft.mbswebplugin.proxy.tool.YUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Author：Created by fan.xd on 2017/1/17.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class WebLoadCache {
    //读取缓存的html页面
    public void setDate(String url, Context context){
        //todo:计算url的hash
        String md5URL = YUtils.md5(url);
        File file = new File(context.getCacheDir()+"/cache" + File.separator + md5URL);

        try {
            URL uri = new URL(url);
            URLConnection connection = uri.openConnection();
            InputStream uristream = connection.getInputStream();
            String cache = connection.getHeaderField("Ddbuild-Cache");
            String contentType = connection.getContentType();
            //text/html; charset=utf-8
            String mimeType = "";
            String encoding = "";
            if (contentType != null && !"".equals(contentType)) {
                if (contentType.indexOf(";") != -1) {
                    String[] args = contentType.split(";");
                    mimeType = args[0];
                    String[] args2 = args[1].trim().split("=");
                    if (args.length == 2 && args2[0].trim().toLowerCase().equals("charset")) {
                        encoding = args2[1].trim();
                    } else {

                        encoding = "utf-8";
                    }
                } else {
                    mimeType = contentType;
                    encoding = "utf-8";
                }
            }

            if (!"1".equals(cache)) {
                //todo:缓存uristream
                FileOutputStream output = new FileOutputStream(file);
                int read_len;
                byte[] buffer = new byte[1024];


                YUtils.writeBlock(output, mimeType);
                YUtils.writeBlock(output, encoding);
                while ((read_len = uristream.read(buffer)) > 0) {
                    output.write(buffer, 0, read_len);
                }
                output.close();
                uristream.close();

                FileInputStream fileInputStream = new FileInputStream(file);
                YUtils.readBlock(fileInputStream);
                YUtils.readBlock(fileInputStream);
                Log.i(">>>>>>>>>", "读缓存:   =  " + file.getName() + " 地址：" + url
                        + "  mimeType:" + mimeType + "   " + encoding);
            } else {
                Log.e(">>>>>>>>>", "网络加载");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
