package com.mobisoft.mbswebplugin.Cmd;

import android.content.Context;
import android.os.Bundle;

/**
 * Author：Created by fan.xd on 2017/3/27.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public interface  HomePage {
    /**
     * 跳转到homePage
     *
     * @param context 见名思意
     * @param bundle  传递数据
     * @param url     主页的地址
     * @param action  action命令
     */
     void goHomePage(Context context, Bundle bundle, String url, String action);

}
