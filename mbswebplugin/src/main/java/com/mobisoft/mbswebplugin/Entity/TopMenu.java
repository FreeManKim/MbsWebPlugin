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
    String msgNum;
    /**
     * 显示类型  HORIZONTAL 水平显示，VERTICAL ：垂直显示
     * 默认 VERTICAL
     */
    String orientation;

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

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

    public String getMsgNum() {
        return msgNum;
    }

    public void setMsgNum(String msgNum) {
        this.msgNum = msgNum;
    }
}
