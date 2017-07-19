package com.mobisoft.MbsDemo.Pull;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mobisoft.MbsDemo.R;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebviewListener;
import com.mobisoft.mbswebplugin.refresh.BGARefreshLayout;
import com.tencent.smtt.sdk.WebView;


public class PullWebActivity extends AppCompatActivity implements BGARefreshLayout.BGARefreshLayoutDelegate, HybridWebviewListener {

    private HybridWebView webView;
    private BGARefreshLayout mRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_web);
        webView = (HybridWebView) findViewById(R.id.webViewExten_01);
        webView.loadUrl("http://cathy.mobisoft.com.cn/cathay/login/login.html?action=nextPage");
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.refreshLayout);
        mRefreshLayout.setDelegate(this);
        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(getApplicationContext(), false);
        moocStyleRefreshViewHolder.setOriginalImage(R.mipmap.hua);
        moocStyleRefreshViewHolder.setUltimateColor(R.color.actionsheet_blue);
        moocStyleRefreshViewHolder.setSpringDistanceScale(200);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);
        webView.setListener(this);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        webView.reload();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    public void onTitle(int type, String tile) {

    }

    @Override
    public void onCommand(String command, String paramter, String function) {

    }

    @Override
    public void onCommand(WebView view, String url) {

    }


    @Override
    public boolean onNextPage(String url, String action) {
        return false;
    }

    @Override
    public com.tencent.smtt.export.external.interfaces.WebResourceResponse onSIRNextPage(String ur, String action) {
        return null;
    }

    @Override
    public boolean onClosePage(String url, String action) {
        return false;
    }

    @Override
    public boolean onClosePageReturnMain(String url, String action) {
        return false;
    }

    @Override
    public void onWebPageFinished() {
        mRefreshLayout.endRefreshing();
    }

    @Override
    public boolean onLightweightPage(String url, String action) {
        return false;
    }

    @Override
    public void onHrefLocation(boolean isNew) {

    }
}
