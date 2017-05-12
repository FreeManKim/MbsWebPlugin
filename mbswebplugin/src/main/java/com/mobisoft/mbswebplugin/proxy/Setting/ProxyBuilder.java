package com.mobisoft.mbswebplugin.proxy.Setting;

import com.mobisoft.mbswebplugin.proxy.Cache.CacheManifest;

/**
 * Author：Created by fan.xd on 2017/3/22.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class ProxyBuilder {

    private CacheManifest cacheManifest;


    public ProxyBuilder() {
        this.cacheManifest = new CacheManifest();
    }

    public static ProxyBuilder create() {
        return new ProxyBuilder();
    }

    public void downCache() {
        if (cacheManifest != null)
            cacheManifest.execute();
        else
            new RuntimeException("cacheManifest 不能为空");
    }
}
