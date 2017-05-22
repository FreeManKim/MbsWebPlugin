package com.mobisoft.mbswebplugin.MvpMbsWeb;

import android.support.annotation.NonNull;

/**
 * Author：Created by fan.xd on 2017/5/19.
 * Email：fang.xd@mobisoft.com.cn
 * Description： 权限回掉监听
 */

public interface MbsRequestPermissionsListener {
    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                    @NonNull int[] grantResults);
}
