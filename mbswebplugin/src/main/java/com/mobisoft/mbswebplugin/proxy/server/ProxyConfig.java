package com.mobisoft.mbswebplugin.proxy.server;

import com.mobisoft.mbswebplugin.proxy.Cache.CacheManifest;

/**
 * Author：Created by fan.xd on 2017/3/17.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class ProxyConfig {
    /**
     * 端口号
     */
    private int PORT;
    /**
     * 缓存路径
     */
    private String cachePath;
    /**
     * 是否弹窗
     */
    private boolean isShowDialog;
    /**
     * 缓存地址
     */
    private String cacheUrl;


    /**
     * 设置cacheManifest地址
     *
     * @param cacheUrl
     * @return
     */
    public ProxyConfig setCacheUrl(String cacheUrl) {
        this.cacheUrl = cacheUrl;
        return this;
    }

    /**
     * 设置缓存路径
     *
     * @param cachePath
     * @return
     */
    public ProxyConfig setCachePath(String cachePath) {
        this.cachePath = cachePath;
        return this;
    }

    /**
     * 设置代理的端口号
     *
     * @param PORT
     * @return
     */
    public ProxyConfig setPORT(int PORT) {
        this.PORT = PORT;
        return this;
    }

    /**
     * 设置是否弹出缓存进度窗口
     *
     * @param showDialog false ： 不显示
     * @return
     */
    public ProxyConfig setShowDialog(boolean showDialog) {
        isShowDialog = showDialog;
        return this;
    }

    public String getCacheUrl() {
        return cacheUrl;
    }

    public boolean isShowDialog() {
        return isShowDialog;
    }


    public int getPORT() {
        if (PORT < 1024) {
            PORT = 8182;
        }
        return PORT;
    }

    public String getCachePath() {
        return cachePath;
    }

    private static ProxyConfig config;

    /**
     * 获取实例
     *
     * @return
     */
    public static ProxyConfig getConfig() {
        if (config == null) {
            config = new ProxyConfig();
        }
        return config;
    }

    public void excuet() {
        new CacheManifest();
    }
}
