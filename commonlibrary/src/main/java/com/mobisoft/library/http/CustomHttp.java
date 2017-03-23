package com.mobisoft.library.http;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mobisoft.common.gateway.Res;
import com.mobisoft.library.log.TLog;
import com.mobisoft.library.util.JsonUtil;
import com.mobisoft.library.util.NetUtil;
import com.mobisoft.library.util.ToastPUtil;
import com.mobisoft.mbswebplugin.base.ActivityManager;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.protocol.HTTP;

public class CustomHttp {
    /**
     * 2016-1-25修改成‘非静态’版本，处理MDR问题，同时发送多个请求无法处理返回值问题。
     */
    private CustomHttp instance = null;
    private AsyncHttpClient client = new AsyncHttpClient();

    private String URL = null;
    private Context mActivity = null;
    private ProgressDialog pDialog = null;

    private boolean isShow = false;
    private final int TIMEOUT = 180000;
    private Callback interf = null;
    private String params = null;

    private CustomHttp() {
        init();
    }

    private void init() {
        client.setTimeout(TIMEOUT);
        client.setResponseTimeout(TIMEOUT);
        client.addHeader("Content-Type", "application/json");
        client.addHeader("charset", HTTP.UTF_8);
        client.setUserAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        client.setConnectTimeout(TIMEOUT);
    }

    public synchronized static CustomHttp getInstance(String url, Context mActivity) {
        // if (instance == null) {
        CustomHttp instance1 = new CustomHttp();
        // }
        instance1.URL = url;
        instance1.mActivity = mActivity;
        instance1.instance = instance1;
        return instance1;
    }

    public synchronized CustomHttp getParams(Object objs) throws UnsupportedEncodingException {
        if (instance == null) {
            instance = new CustomHttp();
        }
        BasePacket packet = new BasePacket(objs);
        params = JsonUtil.obj2Str(packet);
        params = packet.toJson();
        return instance;
    }

    public synchronized static CustomHttp getInstance(String url, Context mActivity, Object objs) {
        // if (instance == null) {
        CustomHttp instance1 = new CustomHttp();
        // }

        BasePacket packet = new BasePacket(objs);
        StringBuilder sb = new StringBuilder(url);
        sb.append("?");
        for (NameValuePair param : packet.getParams()) {
            sb.append(param.getName());
            sb.append("=");

            sb.append(param.getValue());
            sb.append("&");
        }
        instance1.URL = sb.toString().substring(0, sb.length() - 1);

        TLog.d("URL", instance1.URL);
        instance1.mActivity = mActivity;
        instance1.instance = instance1;
        return instance1;
    }

    public synchronized CustomHttp runGet() throws Exception {

        if (mActivity != null) {
            if (!NetUtil.isConnectedByWifi(mActivity)) {
                ToastPUtil.showShortToast(mActivity, "没有网络，请连接网络");
                return instance;
            }
        }

        client.get(URL, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                if (isShow) {
                    pDialog.show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                // Toast.makeText(mActivity, throwable.getMessage(),
                // Toast.LENGTH_SHORT).show();
                ToastPUtil.showShortToast(mActivity, "连接网络超时");

                TLog.e("onFailure", throwable);
                interf.onFailure(URL, response, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                // Toast.makeText(mActivity, throwable.getMessage(),
                // Toast.LENGTH_SHORT).show();
                //	Toast.makeText(mActivity, "连接网络超时")
                Res res = JsonUtil.json2entity(errorResponse.toString(), Res.class);
                if (!TextUtils.isEmpty(res.getError())) {
					ToastPUtil.showShortToast(mActivity, res.getError() + "");
//                    Activity currt = ActivityManager.get().topActivity();
//                    DialogUtil.getInstance(currt).showDialog("提示", res.getError() + "").show();
                }
                TLog.e("onFailure", throwable);
                interf.onFailure(URL, errorResponse, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                TLog.e("onFailure", throwable);
                interf.onFailure(URL, errorResponse, throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Res res = JsonUtil.json2entity(response.toString(), Res.class);
                if (!res.getResult() && !TextUtils.isEmpty(res.getError())) {
                    Activity currt = ActivityManager.get().topActivity();
                    if (currt != null) {
                        if (TextUtils.equals("密码不正确", res.getError()))
                            interf.onFailure(URL, null, new Throwable(res.getError()));
                        else
//                            DialogUtil.getInstance(currt).showDialog("提示", res.getError() + "").show();
						ToastPUtil.showLongToast(mActivity, res.getError());
                    } else {
                        TLog.e("httpError", res.getError());
                    }
                    interf.onFailure(URL, null, null);
                } else {
                    interf.onSuccess(URL, res);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

            }

            @Override
            public void onFinish() {
                disDialog();
            }
        });
        return instance;
    }

    public synchronized CustomHttp runPost() throws Exception {
        RequestParams params = new RequestParams();
        params.put("req", this.params);
        TLog.e("req-post", this.params);
        client.post(URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                if (isShow) {
                    pDialog.show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                // Toast.makeText(mActivity, throwable.getMessage(),
                // Toast.LENGTH_SHORT).show();
                ToastPUtil.showShortToast(mActivity, "连接网络超时");
                TLog.e("onFailure", throwable);
                interf.onFailure(URL, response, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                // Toast.makeText(mActivity, throwable.getMessage(),
                // Toast.LENGTH_SHORT).show();
                //	Toast.makeText(mActivity, "连接网络超时")
                Res res = JsonUtil.json2entity(errorResponse.toString(), Res.class);
                if (!TextUtils.isEmpty(res.getError())) {
                    ToastPUtil.showShortToast(mActivity, res.getError() + "");
                }
                TLog.e("onFailure", throwable);
                interf.onFailure(URL, errorResponse, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // Toast.makeText(mActivity, throwable.getMessage(),
                // Toast.LENGTH_SHORT).show();
//				Toast.makeText(mActivity, "连接网络超时")
                Res res = JsonUtil.json2entity(errorResponse.toString(), Res.class);
                if (!TextUtils.isEmpty(res.getError())) {
                    ToastPUtil.showShortToast(mActivity, res.getError() + "");
                }
                TLog.e("onFailure", throwable);
                interf.onFailure(URL, errorResponse, throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                interf.onSuccess(URL, JsonUtil.json2entity(response.toString(), Res.class));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

            }

            @Override
            public void onFinish() {
                disDialog();
            }
        });
        return instance;
    }

    public CustomHttp postFile(File file) throws Exception {
        RequestParams params = new RequestParams();
        params.put("image", file, "application/octet-stream");
        params.put("profile_picture", file);

        client.post(URL, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                TLog.e("upload", "success");
                Res res = new Res();
                res.setResult(true);
                res.setPayload("");
                interf.onSuccess(URL, res);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                interf.onFailure(URL, responseBody, error);
            }

        });
        return instance;
    }

    public CustomHttp showMsg(boolean isShow, String msg, boolean isCancel) {
        setShow(isShow);
        if (isShow) {
            pDialog = new ProgressDialog(mActivity, ProgressDialog.THEME_HOLO_LIGHT
            );
            pDialog.setCancelable(isCancel);
            pDialog.setMessage(msg);
        } else {
            disDialog();
            pDialog = null;
        }
        return instance;
    }

    public void disDialog() {
        if (this.pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    public CustomHttp setInterf(Callback interf) {
        this.interf = interf;
        return instance;
    }

    public CustomHttp setShow(boolean isShow) {
        // CustomHttp.isShow = isShow;
        // return instance;
        this.isShow = isShow;
        return this;
    }

    /**
     * 设置超时时间
     *
     * @param timeout
     * @return
     */
    public CustomHttp setTimeout(int timeout) {
        client.setTimeout(timeout);
        client.setResponseTimeout(timeout);
        client.setConnectTimeout(timeout);
        return instance;
    }

    public interface Callback {

        public void onFailure(String url, Object request, Throwable ex);

        public void onSuccess(String url, Res response);
    }

    /**
     * 获取URL
     *
     * @return
     */
    public static String getUrl(Object objs) {
        String query = null;
        CustomHttp instance1 = new CustomHttp();
        BasePacket packet = new BasePacket(objs);
        StringBuilder sb = new StringBuilder("http://dubbo.mobisoft.com.cn:8080/microapp/mobile?req=");
        sb.append("?");
        for (NameValuePair param : packet.getParams()) {
            sb.append(param.getName());
            sb.append("=");
            try {
                query = java.net.URLEncoder.encode(param.getValue(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sb.append(param.getValue());
            sb.append("&");
        }
        TLog.e("update", "url----:" + sb.toString().substring(0, sb.length() - 1));

        return sb.toString().substring(0, sb.length() - 1);
    }
}
