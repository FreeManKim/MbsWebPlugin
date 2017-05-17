package com.mobisoft.MbsDemo.cmd;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.mobisoft.MbsDemo.alipay.AuthResult;
import com.mobisoft.MbsDemo.alipay.OrderInfoUtil2_0;
import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;

import java.util.Map;

/**
 * Author：Created by fan.xd on 2017/5/16.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class aliPayAuth extends DoCmdMethod {

    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2015081400214812";

    /**
     * 支付宝账户登录授权业务：入参pid值
     */
    public static final String PID = "2088411971212525";
    /**
     * 支付宝账户登录授权业务：入参target_id值
     */   //TARGET_ID 为虚假
    public static final String TARGET_ID = "kkkkk091125";

    public static final String RSA2_PRIVATE = "";
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKGVyhD1IqGVrSSeKY74xLh24k58v1m10gC1xkrq93MPe12xZJG9P3L9nmx/" +
            "XwnDzYHHYsEt0IQpNyIOLMfpUtDaQVE9V8zCUC21/" +
            "+x3Q7xNPgYRNkv5Q5x0uxzQ4p554AePApf6IeNY+" +
            "zanAg3sSVLysvg1CAXgVQ943GKRQJOBAgMBAAECgYAUaY7AO6dnUDgyMwS++fAn+WS3U4z8sTpZOp1VI3" +
            "+k+mS0Pk+dFZIpXZ6gyCpiWmjgpyZzju0TdP1xAOdpTbFXW4P4T3vgVpwqpa86UTly7qrlosAanJ2rsEEyiIXm2GfOiYFDEtl4GUIhBoK+/rnxFA0NKQypw2/" +
            "U6vdRNb+BAQJBANCDneQHz7dQEK/8flTm+vCCOrp1EoHVgO3JOADnTzFs59ItsLxgeUGZfwFe0fABRYfSe6VqVlMb2UDh725r6XECQQDGYjV74IWEEufftJR" +
            "+dEA64TM3KTPg3TMPj2JU9kn8ZK9znZEPK7l5vH6TreL6fW3cueu4l7FXjlvjLxqTFcMRAkBXd05tEAXG7JQDbpQDw1xyNP6zplZcFhZeKLEQV/" +
            "c1UWyxEq93ZbSXmu7WwVKpLa9f+SAa1E9fz2fDusK3/BZRAkEAsCKED/9RK+lJPAJtkTSPznch7MjFSdG7QwZwLLUbDV8rs7/" +
            "jChAtNhuYWnAZxYLVpxLfad4s02sOxE+PDaZVsQJAOUruklH9jnps8FqGnTJMx8LCnQ1vHAFkirauzG2o/pmcjKOms+++W/" +
            "7ichyFVP8PdRfetu54JcmQaMaUsdA5Nw==";
    private final int SDK_AUTH_FLAG = 1;
    private Handler mHandler;

    @Override
    public String doMethod(HybridWebView hybridWebView, final Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String s, String s1, String s2) {
//        SettingProxy.revertBackProxy(hybridWebView, ((Activity)context).getApplication().getClass().getName());

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SDK_AUTH_FLAG:
                        @SuppressWarnings("unchecked")
                        AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                        String resultStatus = authResult.getResultStatus();

                        // 判断resultStatus 为“9000”且result_code
                        // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                        if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                            // 获取alipay_open_id，调支付时作为参数extern_token 的value
                            // 传入，则支付账户为该授权账户
                            Toast.makeText(context.getApplicationContext(),
                                    "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            // 其他状态值则为授权失败
                            Toast.makeText(context.getApplicationContext(),
                                    "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_LONG).show();

                        }
                        break;
                    default:
                }
            }
        };
        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * authInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
        final String authInfo = info + "&" + sign;
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask((Activity) context);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();

        return null;
    }
}
