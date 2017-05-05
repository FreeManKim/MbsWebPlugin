package com.mobisoft.mbswebplugin.MvpMbsWeb;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.base.BaseWebActivity;
import com.mobisoft.mbswebplugin.utils.ActivityCollector;
import com.mobisoft.mbswebplugin.utils.ActivityUtils;

/***
 *  webView native 混合式开发核心组件
 */
public class MbsWebActivity extends BaseWebActivity {

    private WebPluginPresenter mbsPersenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mbs_web_01);
        ActivityCollector.addActivity(this);
        MbsWebFragment mbsWebFragment =
                (MbsWebFragment) getSupportFragmentManager().findFragmentById(R.id.content_Frame_1);

        if (mbsWebFragment == null) {
            mbsWebFragment = MbsWebFragment.newInstance(getIntent().getExtras());
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mbsWebFragment, R.id.content_Frame_1);
        }
        mbsPersenter = new WebPluginPresenter(mbsWebFragment, this, MbsWebActivity.class, getIntent().getExtras());
        mbsPersenter.onCreate();
//        mbsPersenter.setNavigationIcon(-1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mbsPersenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mbsPersenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return mbsPersenter.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
        mbsPersenter.finish();
    }

    @Override
    protected void onDestroy() {
        mbsPersenter = null;
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
