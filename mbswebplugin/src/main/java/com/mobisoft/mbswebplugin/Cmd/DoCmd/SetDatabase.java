package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.dao.db.WebViewDao;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author：Created by fan.xd on 2017/3/1.
 * Email：fang.xd@mobisoft.com.cn
 * Description：存储数据库
 * 入参：
 * {"account":"8101099","key":"xxx"，"data":"xxx"}account:工号
 * key:存取key值
 * data:所存的数据
 */

public class SetDatabase extends DoCmdMethod {
    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {

        try {
            JSONObject jsonObject = new JSONObject(params);
            /** 存储key*/
            String getkey = jsonObject.optString("key");
            /*工号*/
            String acount = jsonObject.optString("account");
            setKeyToDB(context,acount, getkey, params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 存储到数据库
     * @param context
     * @param account 工号
     * @param key   key
     * @param value json字符串
     */
    private void setKeyToDB(Context context, String account, String key, String value) {
        WebViewDao mWebViewDao = new WebViewDao(context.getApplicationContext());

        mWebViewDao.saveWebviewJson(account,key,value);
    }
}
