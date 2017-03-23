package com.mobisoft.mbswebplugin.MvpMbsWeb;

import android.content.Context;
import android.content.Intent;

/**
 * Author：Created by fan.xd on 2017/3/1.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public interface MbsResultListener {
    void onActivityResult(Context context, MbsWebPluginContract.View view ,int requestCode, int resultCode, Intent data);
}
