package com.mobisoft.MbsDemo.Pull;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import com.mobisoft.MbsDemo.R;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebviewListener;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

import static java.lang.Thread.sleep;

public class PullWebActivity extends AppCompatActivity {

    private HybridWebView webView;
    private BGARefreshLayout mRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_web);
        webView = (HybridWebView) findViewById(R.id.webViewExten_01);
        webView.setListener(new HybridWebviewListener() {
            @Override
            public void onTitle(int type, String tile) {
                setTitle(tile);
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
            public WebResourceResponse onSIRNextPage(String ur, String action) {
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

            }

            @Override
            public boolean onLightweightPage(String url, String action) {
                return false;
            }
        });
        webView.loadUrl("https://elearning.mobisoft.com.cn/mobile/my.html");
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_modulename_refresh);
        mRefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sleep(4*1000);
                            mRefreshLayout.endRefreshing();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sleep(4*1000);
                            mRefreshLayout.endRefreshing();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                return true;
            }
        });

        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGARefreshViewHolder(this,false) {
            @Override
            public View getRefreshHeaderView() {
                return null;
            }

            @Override
            public void handleScale(float scale, int moveYDistance) {

            }

            @Override
            public void changeToIdle() {

            }

            @Override
            public void changeToPullDown() {

            }

            @Override
            public void changeToReleaseRefresh() {

            }

            @Override
            public void changeToRefreshing() {

            }

            @Override
            public void onEndRefreshing() {

            }
        };
        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);


        // 为了增加下拉刷新头部和加载更多的通用性，提供了以下可选配置选项  -------------START
        // 设置正在加载更多时不显示加载更多控件
        // mRefreshLayout.setIsShowLoadingMoreView(false);
        // 设置正在加载更多时的文本
        refreshViewHolder.setLoadingMoreText("加载很多。。");
        // 设置整个加载更多控件的背景颜色资源 id
        refreshViewHolder.setLoadMoreBackgroundColorRes(R.color.colorAccent);
        // 设置整个加载更多控件的背景 drawable 资源 id
        refreshViewHolder.setLoadMoreBackgroundDrawableRes(R.drawable.actionsheet_bottom_pressed);
        // 设置下拉刷新控件的背景颜色资源 id
        refreshViewHolder.setRefreshViewBackgroundColorRes(R.color.color_light_orange);
        // 设置下拉刷新控件的背景 drawable 资源 id
        refreshViewHolder.setRefreshViewBackgroundDrawableRes(R.drawable.actionsheet_bottom_normal);
//        // 设置自定义头部视图（也可以不用设置）     参数1：自定义头部视图（例如广告位）， 参数2：上拉加载更多是否可用
//        mRefreshLayout.setCustomHeaderView(mBanner, false);
        // 可选配置  -------------END
    }
}
