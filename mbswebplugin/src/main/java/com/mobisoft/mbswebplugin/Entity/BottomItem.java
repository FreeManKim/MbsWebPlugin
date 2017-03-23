package com.mobisoft.mbswebplugin.Entity;

/**
 * Author：Created by fan.xd on 2016/10/20.
 * Email：fang.xd@mobisoft.com.cn
 * Description：底部弹出多级菜单 实体类
 */

public class BottomItem {
    /***地址*/
    private String url;

    /**显示菜单名称**/
    private String name;
    /**图标**/
    private String icon;

    /**回掉方法**/
    private String callback;

    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setIcon(String icon){
        this.icon = icon;
    }
    public String getIcon(){
        return this.icon;
    }
    public void setCallback(String callback){
        this.callback = callback;
    }
    public String getCallback(){
        return this.callback;
    }

}