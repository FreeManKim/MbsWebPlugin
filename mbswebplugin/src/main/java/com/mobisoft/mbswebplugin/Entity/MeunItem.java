package com.mobisoft.mbswebplugin.Entity;

/**
 * Author：Created by fan.xd on 2016/8/5.
 * Email：fang.xd@mobisoft.com.cn
 * Description：菜单实体类
 */
public class MeunItem  {
    /***地址*/
    private String url;

    /**显示菜单名称**/
    private String name;
    /**图标**/
    private String icon;

    /**回掉方法**/
    private String callback;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    @Override
    public String toString() {
        return "MeunItem{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", callback='" + callback + '\'' +
                '}';
    }
}
