package com.mobisoft.mbswebplugin.Cmd;

import android.content.Context;

import com.mobisoft.mbswebplugin.Cmd.DoCmd.ErrorMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Method;

/**
 * Author：Created by fan.xd on 2017/2/24.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class CmdrBuilder {


    private static ProxyCmd proxyCmd;
    private Context mContext;
    private HybridWebView mWebView;
    /**
     * html5传递的 参数
     */
    private String parameter;
    /**
     * 回掉方法
     */
    private String function;
    /**
     * cmd 命令
     */
    private String mCmd;
    /**
     * mvp里的持有者类
     */
    private MbsWebPluginContract.Presenter presenter;

    private MbsWebPluginContract.View contractView;

    public static CmdrBuilder getInstance() {
        proxyCmd = ProxyCmd.getInstance();
        return new CmdrBuilder();
    }

    public CmdrBuilder setContext(Context context) {
        this.mContext = context;
        return this;
    }

    /**
     * mvp里的持有者类
     *
     * @param presenter mvp里的持有者类
     * @return CmdrBuilder
     */
    public CmdrBuilder setPresenter(MbsWebPluginContract.Presenter presenter) {
        this.presenter = presenter;
        return this;
    }

    /**
     * mvp里的持有 视图
     * @param view
     * @return CmdrBuilder
     */
    public CmdrBuilder setContractView(MbsWebPluginContract.View view) {
        this.contractView = view;
        return this;
    }

    public CmdrBuilder setWebView(HybridWebView webView) {
        this.mWebView = webView;
        return this;
    }

    public CmdrBuilder setParameter(String parameter) {
        this.parameter = parameter;
        return this;
    }

    /**
     * 回掉方法
     *
     * @param function 回掉方法
     * @return CmdrBuilder
     */
    public CmdrBuilder setCallback(String function) {
        this.function = function;
        return this;
    }

    /**
     * 命令
     *
     * @param cmd 命令
     * @return CmdrBuilder
     */
    public CmdrBuilder setCmd(String cmd) {
        this.mCmd = cmd.toLowerCase();
        return this;
    }

    /**
     * 获取cmd命令，并且执行
     *
     * @return
     */
    public String doMethod() {
        String var = "";
        Class aClass = proxyCmd.reflectMethod(mCmd.trim());
        Method method = null;
        try {
            method = aClass.getMethod("doMethod", HybridWebView.class, Context.class,
                    MbsWebPluginContract.View.class, MbsWebPluginContract.Presenter.class,
                    String.class, String.class, String.class);
            var = (String) method.invoke(aClass.newInstance(), mWebView, mContext,contractView,presenter, mCmd, parameter, function); //调用方法

        } catch (Exception e) {
            e.printStackTrace();
            // 友盟 错误抓取
            MobclickAgent.reportError(mContext,e);
            ErrorMethod errorMethod = new ErrorMethod();

            var = errorMethod.doMethod(mWebView, mContext,contractView,presenter, mCmd, parameter, function);
            return var;
        }


        //        DoCmdMethod method = proxyCmd.getMethod(mCmd);
//        if (method != null) {
//            var = method.doMethod(mWebView, mContext, parameter, function);
//        } else {
//            var = "没有当前CMD命令：" + mCmd;
//            ToastUtil.showLongToast(mContext,var);
//        }


        return var;
    }
}
