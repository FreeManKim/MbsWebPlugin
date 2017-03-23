package com.mobisoft.mbswebplugin.ClickEvent;

import android.app.Dialog;
import android.view.View;

/**
 * Author：Created by fan.xd on 2017/2/16.
 * Email：fang.xd@mobisoft.com.cn
 * Description： Dialog 的点击事件
 */

public class DisDialog implements View.OnClickListener {

    private final Dialog dialog;

    public DisDialog(Dialog dialog) {
        this.dialog = dialog;

    }

    @Override
    public void onClick(View v) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
