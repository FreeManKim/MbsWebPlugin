package com.mobisoft.MbsDemo.Pull;

import android.content.Context;
import android.view.View;

import com.mobisoft.mbswebplugin.refresh.BGARefreshViewHolder;


/**
 * Author：Created by fan.xd on 2017/4/1.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class BGA51jobRefreshViewHolder extends BGARefreshViewHolder {
    /**
     * @param context
     * @param isLoadingMoreEnabled 上拉加载更多是否可用
     */
    public BGA51jobRefreshViewHolder(Context context, boolean isLoadingMoreEnabled) {
        super(context, isLoadingMoreEnabled);
    }

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
}
