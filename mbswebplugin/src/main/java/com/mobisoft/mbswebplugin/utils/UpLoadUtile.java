package com.mobisoft.mbswebplugin.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;

//import com.lzy.okserver.upload.UploadManager;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 文件上传工具类
 *
 * @author Fan xuedong
 * @version V1.0
 *  2016年3月3日 下午12:39:21
 * UpLoadUtile上传文件
 */
public class UpLoadUtile {


    private static UpLoadUtile mLoad;
    private String url;

    /**
     * OKGo 上传文件
     */
//    private UploadManager uploadManager;


    /**
     * 获取当前上传工具类对象
     *
     * @return
     */
    public static UpLoadUtile getInstance() {

        if (mLoad == null) {
            return mLoad = new UpLoadUtile();
        } else {
            return mLoad;
        }

    }

    /**
     * 获取okGo 上传文件实例
     *
     * @return
     */
//    public UploadManager getUploadManager() {
//        if (uploadManager == null) {
//            return uploadManager = UploadManager.getInstance();
//        } else {
//            return uploadManager;
//        }
//    }

    /**
     * 图片压缩
     */
    public String compress(Context context, String srcPath) {
        DisplayMetrics dm;
        String mypath = null;
        String path = Environment.getExternalStorageDirectory() + File.separator + context.getPackageName().toString() + File.separator + "NikeHead"
                + File.separator; // 指定文件保存的路径 记得配置清单文件权限
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        float hh = dm.heightPixels;
        float ww = dm.widthPixels;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, opts);
        opts.inJustDecodeBounds = false;
        int w = opts.outWidth;
        int h = opts.outHeight;
        int size = 0;
        if (w <= ww && h <= hh) {
            size = 1;
        } else {
            double scale = w >= h ? w / ww : h / hh;
            double log = Math.log(scale) / Math.log(2);
            double logCeil = Math.ceil(log);
            size = (int) Math.pow(2, logCeil);
        }
        opts.inSampleSize = size;
        bitmap = BitmapFactory.decodeFile(srcPath, opts);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 80;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        System.out.println(baos.toByteArray().length);

        while (baos.toByteArray().length > 100 * 1024) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            quality -= 10;
            System.out.println(baos.toByteArray().length);
        }
        try {
            mypath = path + System.currentTimeMillis() + ".jpg";
            baos.writeTo(new FileOutputStream(mypath));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath == null ? "" : mypath;
    }

    //    朱桂飞 2016/10/8 14:07:58
    public void postFileFile(final Context context, final File file, final String mParamter, final String picFunction, final HybridWebView webView) {
        new Thread() {
            @Override
            public void run() {
                try {
                    sendPost(context, file, mParamter, picFunction, webView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    /**
     * 将照片，转换未64位字符串
     *
     * @param context
     * @param filePath    照片路径
     * @param mParamter
     * @param picFunction 回掉方法
     * @param webView     朱桂飞 2016/10/8 14:07:58
     */
    public void encodeBase64File(final Context context, final String filePath, final String mParamter, final String picFunction, final HybridWebView webView, final String selectPicNum) {
        try {

            String bytes = null;
            bytes = Base64Util.encodeBase64File(filePath);
            String a = "{" + "\"base64\"" + ":" + "\"" + bytes + "\"" + "," + "\"num\"" + ":" + "\"" + selectPicNum + " \"" + "}";
            String josn2 = String.format("javascript:" + picFunction + "(" + "'%s')", a);
            webView.loadUrl(josn2);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 上传照片
     *
     * @param context     环境
     * @param file        文件
     * @param mParamter   js返回参数
     * @param picFunction 回掉方法
     * @param webView
     */
    public void sendPost(final Context context, File file, String mParamter, final String picFunction, final HybridWebView webView) {
        if (mParamter != null) {
            try {
                JSONObject json = new JSONObject(mParamter);
                url = json.optString("url");
                Log.e("url", url);
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showShortToast(context, "上传图像失败！");
            }
        }
        HttpURLConnection uploadConnection = null;
        DataOutputStream outputStream;
        String boundary = "********";
        String CRLF = "\r\n";
        String Hyphens = "--";
        int bytesRead, bytesAvailable, bufferSize;
        int maxBufferSize = 1024 * 1024;
        byte[] buffer;

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            URL url = new URL(this.url);
            uploadConnection = (HttpURLConnection) url.openConnection();
            uploadConnection.setDoInput(true);
            uploadConnection.setDoOutput(true);
            uploadConnection.setRequestMethod("POST");

            uploadConnection.setRequestProperty("Connection", "Keep-Alive");
            uploadConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            uploadConnection.setRequestProperty("uploaded_file", file.getAbsolutePath());

            outputStream = new DataOutputStream(uploadConnection.getOutputStream());

            outputStream.writeBytes(Hyphens + boundary + CRLF);

            outputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + file.getAbsolutePath() + "\"" + CRLF);
            outputStream.writeBytes(CRLF);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(CRLF);
            outputStream.writeBytes(Hyphens + boundary + Hyphens + CRLF);

            InputStreamReader resultReader = new InputStreamReader(uploadConnection.getInputStream());
            BufferedReader reader = new BufferedReader(resultReader);
            String line = "";
            String response = "";
            while ((line = reader.readLine()) != null) {
                response += line;
            }

            final String finalResponse = response;
            if (uploadConnection.getResponseCode() == 200) {
                String josn2 = String.format("javascript:" + picFunction + "(" + "'%s')", finalResponse);
                webView.loadUrl(josn2);
                Log.e("url", josn2);
                ToastUtil.showShortToast(context, "上传图像成功");
            } else {
                ToastUtil.showShortToast(context, "上传图像失败！");

            }

            fileInputStream.close();
            outputStream.flush();
            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        PostRequest postRequest = OkGo.post(AppConfing.CTTQ_BASE_URL + url)//
//                .headers("headerKey1", "headerValue1")//
//                .headers("headerKey2", "headerValue2")//
//                .params("paramKey1", "paramValue1")//
//                .params("paramKey2", "paramValue2")//
//                .params("fileKey", file);
//        getUploadManager().addTask(file.getAbsolutePath(), postRequest, new UploadListener<String>() {
//            @Override
//            public void onProgress(UploadInfo uploadInfo) {
//
//            }
//
//            @Override
//            public void onFinish(String result) {
//                Log.e("oye", "onFinish:" + result);
//                Log.e("oye", "onSuccess:" + result);
//                String josn2 = String.format("javascript:" + picFunction + "(" + "'%s')", result);
//                webView.loadUrl(josn2);
//                Log.e("url", josn2);
//                ToastUtil.showShortToast(context, "上传图像成功");
//            }
//
//            @Override
//            public void onError(UploadInfo uploadInfo, String errorMsg, Exception ex) {
//                Log.e("oye", "onError:" + ex.getMessage());
//                ToastUtil.showShortToast(context, "上传图像失败！");
//            }
//
//            @Override
//            public String parseNetworkResponse(Response response) throws Exception {
//
//                return null;
//            }
//        });

//        HttpURLConnection urlConnection

//        // 这里的URLUtils.UPLOAD_IMG就是我们要访问的路径
//        RequestParams params = new RequestParams(AppConfing.CTTQ_BASE_URL + url);
//         // 设为true就是以表单形式上传，否则上传原始文件流
//        params.setMultipart(true);
//        // 下面就是请求网络时传递的参数
//        // params.addQueryStringParameter();
//        params.addBodyParameter("image_no", "");
//        // 这里的第一个字段是随便写的，第二个参数是要传递的图盘或者文件，第三个参数是这个图片或者文件的后缀名（由于图片有后缀名因此就传为空）
//        params.addBodyParameter("file", file, null);
//        Log.e("url","图片参数："+params.toString());
//        org.xutils.x.http().post(params, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                Log.e("oye", "onSuccess:" + result);
//                String josn2 = String.format("javascript:" + picFunction + "(" + "'%s')", result);
//                webView.loadUrl(josn2);
//                Log.e("url", josn2);
//                ToastUtil.showShortToast(context, "上传图像成功");
//
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Log.e("oye", "onError:" + ex.getMessage());
//                ToastUtil.showShortToast(context, "上传图像失败！");
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//                Log.e("oye", "onCancelled:" + cex.getMessage());
//
//            }
//
//            @Override
//            public void onFinished() {
//                Log.e("oye", "onFinished:");
//            }
//        });

    }

    /**
     * HttpUrlConnection支持所有Https免验证，不建议使用
     *
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    private StringBuffer result = null;

    public String cheCheckUpdate(String murl) {
        try {
            URL url = new URL(murl);
            Log.i("Update", murl);
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new TrustManager[]{new TrustAllManager()}, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            result = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            in.close();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            return null;
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (ProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        System.out.println("返回数据Update:" + result.toString());

        Log.e("Update", result.toString());


        return result.toString();
    }

    public class TrustAllManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub

        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }
    }

}
