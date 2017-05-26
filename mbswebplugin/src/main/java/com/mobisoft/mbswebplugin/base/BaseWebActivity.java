package com.mobisoft.mbswebplugin.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.mobisoft.mbswebplugin.dao.db.WebViewDao;
import com.umeng.analytics.MobclickAgent;


public  class BaseWebActivity extends AppCompatActivity {

    public boolean isNeedClose = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 设置value 到数据库
     * @param account
     * @param key
     * @param value
     */
    protected void setKeyToDB(String account, String key, String value){
        WebViewDao mWebViewDao = new WebViewDao(getApplicationContext());

        mWebViewDao.saveWebviewJson(account,key,value);
    }

    /**
     * 根据key 从数据库得到value
     * @param account 工号
     * @param key 关键字
     * @return 根据acoutn 和 key查询据库的数据
     */
    protected String getValueFromDB(String account, String key){
        WebViewDao mWebViewDao = new WebViewDao(getApplicationContext());
        return mWebViewDao.getWebviewValuejson(account,key);
    }

    protected void deleteValueFromDB(String account, String key){
        WebViewDao mWebViewDao = new WebViewDao(getApplicationContext());

        mWebViewDao.deleteWebviewList(account,key);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

    }
}
