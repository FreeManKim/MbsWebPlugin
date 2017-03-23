package com.mobisoft.amaplibrary;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Author：Created by fan.xd on 2017/3/3.
 * Email：fang.xd@mobisoft.com.cn
 * Description： 高德地图定位组件
 */

public class AmapLocation {

    private static AmapLocation instance;
    private Context context;
    /**
     * 是否初始化过定位系统
     */
    private boolean isBind = false;
    /***
     * 声明定位回调监听器
     */
    private AMapLocationListener mLocationListener;

    /**
     * 声明AMapLocationClient类对象
     */
    private AMapLocationClient mLocationClient = null;

    /**
     * 声明AMapLocationClientOption对象
     */
    private AMapLocationClientOption mLocationOption = null;


    /**
     * 声明定位回调监听器
     *
     * @param mLocationListener
     */
    public AmapLocation setmLocationListener(AMapLocationListener mLocationListener) {
        this.mLocationListener = mLocationListener;

        return instance;
    }

    /**
     * 环境
     *
     * @param context
     */
    public AmapLocation setContext(Context context) {
        this.context = context.getApplicationContext();
        return instance;
    }

    /**
     * 销毁定位客户端，同时销毁本地定位服务。
     */
    public void destroy() {
        isBind = false;
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        mLocationClient.onDestroy();
        context = null;
        instance = null;
    }

    /**
     * 初始化定位
     *
     * @return instance
     */
    public AmapLocation init() {
        if (!isBind)
            initLocation();
        return instance;
    }

    /**
     * 启动定位
     */
    public void startLocation() {
        //启动定位
        mLocationClient.startLocation();
    }


    /**
     * 获取单例
     *
     * @return
     */
    synchronized public static AmapLocation getInstance() {
        if (instance == null) {
            instance = new AmapLocation();
        }

        return instance;
    }

    public AmapLocation() {
    }

    /**
     * 初始化定位
     */
    private void initLocation() {

        //初始化定位
        mLocationClient = new AMapLocationClient(context);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);

        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。现在设置未10秒
        mLocationOption.setInterval(1000 * 10);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
//        //启动定位
//        mLocationClient.startLocation();
        isBind = true;

    }
}
