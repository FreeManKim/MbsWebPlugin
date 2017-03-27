package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.os.Bundle;

import com.mobisoft.mbswebplugin.Cmd.HomePage;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebApp;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Base.Preconditions;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebActivity;
import com.mobisoft.mbswebplugin.base.Recycler;
import com.mobisoft.mbswebplugin.helper.CoreConfig;
import com.mobisoft.mbswebplugin.helper.FunctionConfig;
import com.mobisoft.mbswebplugin.helper.ThemeConfig;

/**
 * Author：Created by fan.xd on 2017/3/27.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class DefaultHomePage implements Recycler.Recycleable, HomePage {
    @Override
    public void goHomePage(Context context, Bundle bundle, String url, String action) {
        CoreConfig coreConfig =
                new CoreConfig.Builder(
                        context, ThemeConfig.DEFAULT, FunctionConfig.DEFAULT_ACTIVITY)
                        .setURL(Preconditions.checkNotNull(url))
                        .setAccount("8100458")//
                        .setNoAnimcation(false)
                        .setHideNavigation(true)
                        .build();
        HybridWebApp.init(coreConfig).startHomeActivity(context, MbsWebActivity.class);

    }

    @Override
    public void release() {

    }


}
