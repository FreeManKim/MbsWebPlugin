package com.mobisoft.mbswebplugin.Entity;

import java.util.List;

/**
 * Author：Created by fan.xd on 2016/8/5.
 * Email：fang.xd@mobisoft.com.cn
 * Description：菜单实体类
 */
public class TopMenu {

    List<MeunItem> item;

    boolean showMsg;

    public boolean isShowMsg() {
        return showMsg;
    }

    public void setShowMsg(boolean showMsg) {
        this.showMsg = showMsg;
    }

    public List<MeunItem> getItem() {
        return item;
    }

    public void setItem(List<MeunItem> item) {
        this.item = item;
    }
}
