package com.mobisoft.mbswebplugin.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.proxy.Setting.ProxyConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cz.msebera.android.httpclient.Header;

//import com.lzy.okserver.upload.UploadManager;

/**
 * 文件上传工具类
 *
 * @author Fan xuedong
 * @version V1.0
 *          2016年3月3日 下午12:39:21
 *          UpLoadUtile上传文件
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
    public void postFileFile(final Context context, final File file, final String mParamter, final String picFunction, final MbsWebPluginContract.View webView) {
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    sendPost(context, file, mParamter, picFunction, webView);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
        sendPost(context, file, mParamter, picFunction, webView);
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
//            String josn2 = String.format("javascript:" + picFunction + "(" + "'%s')", a);
            webView.loadUrl(UrlUtil.getFormatJs(picFunction,a));


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
     * @param view
     */
    public void sendPost(final Context context, File file, String mParamter, final String picFunction, final MbsWebPluginContract.View view) {
        if (mParamter != null) {
            try {
                JSONObject json = new JSONObject(mParamter);
                String url1 = json.optString("url");
                url = ProxyConfig.getConfig().getImageBaseUrl() + url1;
                Log.e("url", url);
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.showShortToast(context, "上传图像失败！");
            }
        }
        try {
            if (file == null || !file.exists()) {
                return;
            }
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("file", file);
            client.post(url, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();

                }

                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    if (i == 200) {
//                        String josn2 = String.format("javascript:" + picFunction + "(" + "'%s')", new String(bytes));
                        view.loadUrl(UrlUtil.getFormatJs(picFunction,new String(bytes)));
                        ToastUtil.showShortToast(context, "上传图像成功");
                    }

                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    ToastUtil.showShortToast(context, "上传图像失败！");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

//        Map<String, String> params = new HashMap<>(); params.put("id", "1");
//        //...如果有其他参数添加到这里
//         String request = uploadFile(file, url, params, "image");

//        HttpURLConnection uploadConnection = null;
//        DataOutputStream outputStream;
//        String boundary = "********";
//        String CRLF = "\r\n";
//        String Hyphens = "--";
//        int bytesRead, bytesAvailable, bufferSize;
//        int maxBufferSize = 1024 * 1024;
//        byte[] buffer;
//
//        try {
//            FileInputStream fileInputStream = new FileInputStream(file);
//            URL url = new URL(this.url);
//            uploadConnection = (HttpURLConnection) url.openConnection();
//            uploadConnection.setDoInput(true);
//            uploadConnection.setDoOutput(true);
//            uploadConnection.setRequestMethod("POST");
//
//            uploadConnection.setRequestProperty("Connection", "Keep-Alive");
//            uploadConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
//            uploadConnection.setRequestProperty("uploaded_file", file.getAbsolutePath());
//
//            outputStream = new DataOutputStream(uploadConnection.getOutputStream());
//
//            outputStream.writeBytes(Hyphens + boundary + CRLF);
//
//            outputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + file.getAbsolutePath() + "\"" + CRLF);
//            outputStream.writeBytes(CRLF);
//
//            bytesAvailable = fileInputStream.available();
//            bufferSize = Math.min(bytesAvailable, maxBufferSize);
//            buffer = new byte[bufferSize];
//            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//            while (bytesRead > 0) {
//                outputStream.write(buffer, 0, bufferSize);
//                bytesAvailable = fileInputStream.available();
//                bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//            }
//
//            outputStream.writeBytes(CRLF);
//            outputStream.writeBytes(Hyphens + boundary + Hyphens + CRLF);
//
//            InputStreamReader resultReader = new InputStreamReader(uploadConnection.getInputStream());
//            BufferedReader reader = new BufferedReader(resultReader);
//            String line = "";
//            String response = "";
//            while ((line = reader.readLine()) != null) {
//                response += line;
//            }
//
//            final String finalResponse = response;
//            if (uploadConnection.getResponseCode() == 200) {
//                String josn2 = String.format("javascript:" + picFunction + "(" + "'%s')", finalResponse);
//                view.loadUrl(josn2);
//                Log.e("url", josn2);
//                ToastUtil.showShortToast(context, "上传图像成功");
//            } else {
//                ToastUtil.showShortToast(context, "上传图像失败！");
//
//            }
//
//            fileInputStream.close();
//            outputStream.flush();
//            outputStream.close();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }


    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10 * 1000;
    //超时时间
    private static final String CHARSET = "utf-8";
    private static final String BOUNDARY = UUID.randomUUID().toString();
    //边界标识随机生成
    private static final String PREFIX = "--";
    private static final String LINE_END = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data";
    //内容类型

    /**
     * 上传文件 * @param file 文件 * @param RequestURL post地址 * @param params 除文件外其他参数 * @param uploadFieldName 上传文件key * @return
     */
    public static String uploadFile(File file, String RequestURL, Map<String, String> params, String uploadFieldName) {
        String result = null;
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charset", CHARSET);
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            StringBuffer sb = new StringBuffer();
            sb.append(getRequestData(params));
            if (file != null) {
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                sb.append("Content-Disposition: form-data; name=\"" + uploadFieldName + "\"; filename=\"" + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                sb.append(LINE_END);
            }
            dos.write(sb.toString().getBytes());
            if (file != null) {
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
            }
            dos.flush();
            int res = conn.getResponseCode();
            Log.e(TAG, "response code:" + res);
            if (res == 200) {
                Log.e(TAG, "request success");
                InputStream input = conn.getInputStream();
                StringBuffer sb1 = new StringBuffer();
                int ss;
                while ((ss = input.read()) != -1) {
                    sb1.append((char) ss);
                }
                result = sb1.toString();
                Log.i(TAG, "result : " + result);
            } else {
                Log.e(TAG, "request error");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 对post参数进行编码处理 * @param params post参数 * @return
     */
    private static StringBuffer getRequestData(Map<String, String> params) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(PREFIX);
                stringBuffer.append(BOUNDARY);
                stringBuffer.append(LINE_END);
                stringBuffer.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINE_END);
                stringBuffer.append(LINE_END);
                stringBuffer.append(URLEncoder.encode(entry.getValue(), CHARSET));
                stringBuffer.append(LINE_END);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
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
